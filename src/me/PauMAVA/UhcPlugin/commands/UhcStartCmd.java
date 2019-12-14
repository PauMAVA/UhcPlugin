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

package me.PauMAVA.UhcPlugin.commands;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.match.UhcMatchHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

class UhcStartCmd {
	private static long counter;
	private static BukkitTask task;

	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	
	/* initiates uhc start countdown
	* @param args - the command arguments */
	public static <T> void start(String[] args) {
		Bukkit.getScheduler().cancelTasks(plugin);
		try {
			counter = Integer.parseInt(args[1]);
		} catch(ArrayIndexOutOfBoundsException e) {
			counter = 10;
		}
		task = Bukkit.getScheduler().runTaskTimer(UhcPluginCore.getInstance(), new Runnable() {
			@Override
			public void run() {
				ChatColor numberColor;
				float pitch;
				switch((int) counter) {
					case 1:
					case 2:
					case 3: {
						numberColor = ChatColor.RED;
						pitch = 2;
						break;
					}
					case 4:
					case 5: {
						numberColor = ChatColor.YELLOW;
						pitch = 1.5F;
						break;
					}
					default: {
						numberColor = ChatColor.GREEN;
						pitch = 1;
						break;
					}
				}
				for(Player player: Bukkit.getOnlinePlayers()) { 
					player.sendMessage(ChatColor.GOLD + "UHC STARTING IN " + numberColor + counter);
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, pitch);
				}
				counter--;
				if(counter <= 0) {
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch(InterruptedException e) {
						plugin.getPluginLogger().warning("An error ocurred on Uhc Countdown task!");
					}
					for(Player player: Bukkit.getOnlinePlayers()) {
						player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
						player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "UHC T" + plugin.getConfig().getInt("season"),ChatColor.AQUA + "" + ChatColor.BOLD +  "STARTS NOW!", 0, 5*20, 1*20);
					}
					UhcPluginCore.getInstance().getMatchHandler().start();
					Bukkit.getScheduler().cancelTask(task.getTaskId());
				}
			}
		}, 0L, 20L);
	}
}
