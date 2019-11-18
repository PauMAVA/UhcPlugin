
package me.PauMAVA.UhcPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_14_R1.PacketPlayInAdvancements;
import net.minecraft.server.v1_14_R1.PacketPlayInChat;
import net.minecraft.server.v1_14_R1.PacketPlayOutAdvancements;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;


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
					if(packet instanceof PacketPlayInChat && plugin.getMatchStatus()) {
						Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + packet.toString());
						String stringMsg= ((PacketPlayInChat) packet).b();
						if(stringMsg.charAt(0) == '/') {
							plugin.getPluginLogger().info("Chat packet block override! User prompted command: " + stringMsg);
							super.channelRead(context, packet);
							return;
						}
						UhcChatManager.dispatchPlayerMessage(stringMsg, player);
						return;
					}
					if(packet instanceof PacketPlayInAdvancements) {
						Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + packet.toString());
						String key = ((PacketPlayInAdvancements) packet).d().getKey();
						plugin.getPluginLogger().info("Advancement made: " + key);
						
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
					if((packet instanceof PacketPlayOutChat || packet instanceof PacketPlayOutAdvancements) && plugin.getMatchStatus()) {
						Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + packet.toString());
					}
					if(packet instanceof PacketPlayOutAdvancements) {
						PacketPlayOutAdvancements advancementPacket = (PacketPlayOutAdvancements) packet;
						//TODO Solve the We need to go deeper advancement bug. Only happens when a player enters the nether for the first time. It is unloaded!
						/* TEST ZONE */
						String a;
						if(advancementPacket.a()) {
							a = "true";
						} else {
							a = "false";
						}
						Bukkit.getServer().getConsoleSender().sendMessage("The a() method on " + advancementPacket.toString() + " returns " + a);
						Bukkit.getServer().getConsoleSender().sendMessage("The has code is: " + advancementPacket.hashCode());
						/* TEST ZONE */
					}
					super.write(context, packet, promise);
				} catch(Exception e) {
					plugin.getPluginLogger().warning(e.getMessage());
					plugin.getPluginLogger().warning(ChatColor.DARK_RED + "An error occured while writing a packet!");			
				}
			}
			
		};
	
		ChannelPipeline pipe = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
		pipe.addBefore("packet_handler", player.getName(), channelDuplexHandler);
	}
	
}
