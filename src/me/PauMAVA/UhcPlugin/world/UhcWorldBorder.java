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

package me.PauMAVA.UhcPlugin.world;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.chat.Prefix;
import me.PauMAVA.UhcPlugin.commands.UhcConfigCmd;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class UhcWorldBorder {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static FileConfiguration config = UhcConfigCmd.getConfig();
	private static int taskID;
	private static List<Player> warnedPlayers = new ArrayList<Player>();


	public static void refreshBorder(Integer episode) {
		if(!configIsSet()) {
			setConfig();
		}
		if(!isClosable()) {
			/* Nothing has to be done then */
			return;
		}
		if(episode.intValue() == getBorderClosingEpisode().intValue()) {
			List<World> dimensions = plugin.getServer().getWorlds();
			for(World dimension: dimensions) {
				dimension.getWorldBorder().setSize(getFinalRadius(), (10 - getBorderClosingEpisode()) * config.getInt("chapter_length") * 60);
			}
			float velocity = (getOriginalBorderRadius() - getFinalRadius()) / ((10 - getBorderClosingEpisode())* (float) config.getInt("chapter_length") * 60);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				p.sendMessage(Prefix.INGAME_UHC + "" + ChatColor.YELLOW + "The border is closing as you have reached episode " + getBorderClosingEpisode() + "!");
				p.sendMessage(Prefix.INGAME_UHC + "" + ChatColor.YELLOW + "It is time for you to reach 0,0. Get ready for the fight. Good luck!");
				p.sendMessage(Prefix.INGAME_UHC + "" + ChatColor.AQUA + "The border is closing at a velocity of " + velocity + " blocks/second.");
			}
		}
		return;
	}
	
	private static Boolean isClosable() {
		return config.getBoolean("closable_border");
	}
	
	private static Integer getFinalRadius() {
		return config.getInt("final_radius");
	}
	
	private static Integer getBorderClosingEpisode() {
		return config.getInt("border_closing_episode");
	}
	
	private static Integer getOriginalBorderRadius() {
		return config.getInt("border_radius");
	}
	
	private static Boolean configIsSet() {
		if(config.isSet("border_radius") && config.isSet("closable_border") && config.isSet("final_radius") && config.isSet("border_closing_episode")) {
			return true;
		}
		return false;
	}
	
	private static void setConfig() {
		config.set("border_radius", 1500);
		config.set("closable_border", true);
		config.set("final_radius", 150);
		config.set("border_closing_episode", 8);
		return;
	}

	public static void startWarningTask() {
		new BukkitRunnable(){
			@Override
			public void run() {
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					int distance = getDistance(player);
					if(distance <= 50) {
						sendActionBarMessage(player, ChatColor.YELLOW + "The border is " + distance + " blocks away from you!");
						if(!warnedPlayers.contains(player)) {
							warnedPlayers.add(player);
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 1);
						}
					} else {
						warnedPlayers.remove(player);
					}
				}
			}
		}.runTaskTimerAsynchronously(UhcPluginCore.getInstance(), 0L, 20L);
	}

	private static int getDistance(Player player) {
		int borderSize = (int) player.getWorld().getWorldBorder().getSize() / 2;
		Location playerLocation = player.getLocation();
		return Math.min(Math.min(borderSize - (int) playerLocation.getZ(), (int) playerLocation.getX() + borderSize), Math.min(borderSize - (int) playerLocation.getX(), (int) playerLocation.getZ() + borderSize));
	}

	public static void stopWarningTask() {
		Bukkit.getScheduler().cancelTask(taskID);
	}

	private static void sendActionBarMessage(Player player, String message) {
		PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(new ChatComponentText(message), ChatMessageType.a((byte) 2));
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutChat);
	}
}
