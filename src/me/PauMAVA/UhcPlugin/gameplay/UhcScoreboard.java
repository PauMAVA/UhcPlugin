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

package me.PauMAVA.UhcPlugin.gameplay;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.world.UhcWorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class UhcScoreboard {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	static final ScoreboardManager scManager = plugin.getServer().getScoreboardManager();
	static Scoreboard uhcScoreboard = scManager.getNewScoreboard();
	static String sPrefix,mPrefix = "";
	private static int chapterLength = plugin.getConfig().getInt("chapter_length");
	
	public static void setUp() {
			timerSidebar();
			lifeList();
			for(Player player: Bukkit.getOnlinePlayers()) {
				player.setScoreboard(uhcScoreboard);
			}
		return;
	}
	
	private static void timerSidebar() {
		//TODO FIX NUMBERS SMALLER THAN 10
		//TODO CONFIG NUMBER OF CHAPTERS AND DURATION OF EACH ONE BEFORE SCHEDULING THE ASYNC TASK
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			int seconds = 59;
			int minutes = chapterLength - 1;
			int episode = 1;
			
			@Override
			public void run() {
				if(seconds == 40 && minutes == 0) {
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						p.playSound(p.getLocation(), Sound.MUSIC_DISC_FAR, 20.0F, 1.0F);
					}
				}
				if(seconds <= 0) {
					seconds = 59;
					minutes --;
					if(minutes < 0) {
						minutes =  chapterLength - 1;
						episode ++;
						Bukkit.broadcastMessage(ChatColor.GOLD + "[Game] " + ChatColor.YELLOW + "The episode " + episode + " begins now!");
						UhcWorldBorder.refreshBorder(episode);
						refresh(seconds,minutes,episode);
						return;
					}
					refresh(seconds,minutes,episode);
					return;
				}
				seconds --;
				//TODO EPISODE HANDLING
				switch(episode) {}
				refresh(seconds,minutes,episode);
				return;
			}
		}, 0L, 20L);
		return;
	}
	
	private static void lifeList() {
		Objective lifeObjective = uhcScoreboard.registerNewObjective("Health", "health", "health", RenderType.HEARTS);
		lifeObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		for(Player player: Bukkit.getOnlinePlayers()) {
			updateHealth(player,lifeObjective);
		}
		return;
	}
	
	public static void updateHealth(Player player, Objective lifeObjective) {
		Score lifeScore = lifeObjective.getScore(player.getName());
		lifeScore.setScore((int) player.getHealth());
		lifeObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		return;
	}
	
	public static void refresh(int seconds, int minutes, int episode) {
		if(seconds < 10) {
			sPrefix = "0";
		} else {
			sPrefix = "";
		}
		if(minutes < 10) {
			mPrefix = "0";
		} else {
			mPrefix = "";
		}
		uhcScoreboard = scManager.getNewScoreboard();
		Objective timerObjective = uhcScoreboard.registerNewObjective("timer", "dummy", ChatColor.BOLD + "" + ChatColor.UNDERLINE + ChatColor.GOLD + "UHC T" + plugin.getConfig().getInt("season"));
		Score timerScore = timerObjective.getScore(ChatColor.RED + "Time left: " + ChatColor.RESET + "" + ChatColor.GREEN + mPrefix + minutes + ":" + sPrefix + seconds);
		Score episodeScore = timerObjective.getScore(ChatColor.BLUE + "Episode: " + episode);
		timerScore.setScore(1);
		episodeScore.setScore(0);
		timerObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		lifeList();
		for(Player player: Bukkit.getOnlinePlayers()) {
			player.setScoreboard(uhcScoreboard);
		}
	}
	
}
