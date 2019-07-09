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
			/*Override parent method to print all packets before letting the client/server to read them
			 * This enables to block any packet or modify it before sending it to the client or server*/
			@Override
			public void channelRead(ChannelHandlerContext context, Object packet) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + packet.toString());
				try {
					super.channelRead(context, packet);
				} catch (Exception e) {
					plugin.getPluginLogger().warning(ChatColor.DARK_RED + "An error occurred while reading a packet!");
					e.printStackTrace();
				}
			}
			
			@Override
			public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + packet.toString());
				try {
					if(packet instanceof PacketPlayOutChat) {
						PacketPlayOutChat chatPacket = (PacketPlayOutChat) packet;
						chatPacket = UhcChatManager.manageOutPacket(chatPacket);
					}
					super.write(context, packet, promise);
				} catch(Exception e) {
					plugin.getPluginLogger().warning(ChatColor.DARK_RED + "An error occured while writing a packet!");			
				}
			}
			
		};
		
		ChannelPipeline pipe = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
		pipe.addBefore("packet_handler", player.getName(), channelDuplexHandler);
	}
	
}
