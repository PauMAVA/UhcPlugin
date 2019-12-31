/*
 * UhcPlugin
 * Copyright (c) 2019  Pau Machetti Vallverdu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.PauMAVA.UhcPlugin.util;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.chat.UhcChatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_15_R1.PacketPlayInAdvancements;
import net.minecraft.server.v1_15_R1.PacketPlayInChat;
import net.minecraft.server.v1_15_R1.PacketPlayOutAdvancements;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;


public class PacketIntercepter {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	
	public static void rmPlayer(Player player) {
		Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
		channel.eventLoop().submit(() -> {
			channel.pipeline().remove(player.getName());
			return null;
		});
	}
	
	
	public static void injectPlayer(Player player) {
		ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
			/* Override parent method to print all packets before letting the client/server to read them
			 This enables to block any packet or modify it before sending it to the client or server */
			@Override
			public void channelRead(ChannelHandlerContext context, Object packet) {
				try {
					if(packet instanceof PacketPlayInChat && plugin.getMatchHandler().getMatchStatus()) {
						String stringMsg = ((PacketPlayInChat) packet).b();
						if(stringMsg.charAt(0) == '/' || !plugin.getMatchHandler().getMatchStatus()) {
							super.channelRead(context, packet);
							return;
						}
						UhcChatManager.dispatchPlayerMessage(stringMsg, player);
						return;
					}
					if(packet instanceof PacketPlayInAdvancements) {
						String key = ((PacketPlayInAdvancements) packet).d().getKey();
						
					}
					super.channelRead(context, packet);
				} catch (Exception e) {
					plugin.getPluginLogger().warning(ChatColor.DARK_RED + "An error occurred while reading a packet!");
					e.printStackTrace();
				}
			}
			
			@Override
			public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) {
				try {
					super.write(context, packet, promise);
				} catch(Exception e) {
					plugin.getPluginLogger().warning(e.getMessage());
					plugin.getPluginLogger().warning(ChatColor.DARK_RED + "An error occured while writing a packet!");			
				}
			}
			
		};
	
		ChannelPipeline pipe = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
		try{
			pipe.addBefore("packet_handler", player.getName(), channelDuplexHandler);
		} catch (IllegalArgumentException ignored) {}
	}
	
}
