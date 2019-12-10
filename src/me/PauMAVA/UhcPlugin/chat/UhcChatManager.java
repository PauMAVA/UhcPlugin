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

package me.PauMAVA.UhcPlugin.chat;

import java.util.List;
import java.util.Random;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.teams.UhcTeamsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UhcChatManager {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	
	public static void dispatchPlayerMessage(String msg, Player player) {
		if(msg.charAt(0) == '!') {
			globalDispatcher(msg);
		} else {
			teamDispatcher(msg, player);
		}
	}
	
	public static void dispatchAdvancementEvent(String advancement) {
		plugin.getPluginLogger().info("THE ADVANCEMENT IS " + advancement);
		String random = randomString();
		for(Player p: Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.GRAY + "" + ChatColor.MAGIC + random + ChatColor.RESET + "has made the advancement " + ChatColor.GREEN + "[" + advancement + "]");
		}
	}
	
	private static String randomString() {
		Random rand = new Random();
		int randomInt = rand.nextInt(7) + 6;
		String randomString = "";
		for(int i = 0; i < randomInt; i++) {
			randomString = randomString.concat("-");
		}
		randomString = randomString.concat(" ");
		return randomString;
	}
	
	private static void globalDispatcher(String msg) {
		String random = randomString();
		msg = msg.substring(1);
		msg = ChatColor.GRAY + "" + ChatColor.MAGIC + random + ChatColor.RESET + msg;
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(msg);
		}
		return;
	}
	
	private static void teamDispatcher(String msg, Player player) {
		String playerName = player.getName();
		String playersTeam = UhcTeamsManager.getPlayerTeam(playerName);
		msg = ChatColor.YELLOW + playerName + " " + ChatColor.RESET + msg;
		/* Checks if the player doesn't belong to a team */
		if(playersTeam == null) {
			for(Player checkPlayer: Bukkit.getServer().getOnlinePlayers()) {
				if(checkPlayer.getName().contentEquals(playerName)) {
					checkPlayer.sendMessage(msg);
				}
			}
			return;
		}
		List<String> recievers = UhcTeamsManager.getTeamMembers(playersTeam);
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			if(recievers.contains(p.getName())) {
			p.sendMessage(msg);
			}
		}
 		return;
	}
	
}
