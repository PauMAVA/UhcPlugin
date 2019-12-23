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

package me.PauMAVA.UhcPlugin.match;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UhcScoreboardManager {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	static final ScoreboardManager scManager = plugin.getServer().getScoreboardManager();
	static Scoreboard uhcScoreboard = scManager.getNewScoreboard();
	private static Objective infoObjective;
	private static Objective lifeObjective;
	private static Score timer, totalTime, episodeScore, fullLine;
	private static List<Score> blankLines = new ArrayList<Score>();

	static String sPrefix,mPrefix = "";
	private static int chapterLength = plugin.getConfig().getInt("chapter_length");
	
	public static void setUp() {
		uhcScoreboard = scManager.getNewScoreboard();
		infoObjective = uhcScoreboard.registerNewObjective("Info", "dummy", ChatColor.GOLD + "" + ChatColor.BOLD + "UHC S" + plugin.getConfig().get("season"));
		lifeObjective = uhcScoreboard.registerNewObjective("Health", "health", "health", RenderType.HEARTS);
		lifeObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		updateHealth();
		for(Player player: Bukkit.getOnlinePlayers()) {
			player.setScoreboard(uhcScoreboard);
		}
	}
	
	/* Updates the health on the list scoreboard */
	public static void updateHealth() {
		for(Player player: Bukkit.getOnlinePlayers()) {
			Score lifeScore = lifeObjective.getScore(player.getName());
			lifeScore.setScore((int) player.getHealth());
			lifeObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		}
	}

	/* Refreshes the scoreboard sidebar
	* @param minutes - the minutes that will be displayed in the sidebar
	* @param seconds - the seconds that will be displayed in the sidebar
	* @param episode - the episode that will be displayed in the sidebar
	* @param total - the total time that the match has been going on */
	static void refreshSidebar(String minutes, String seconds, String episode, String total) {
		if(timer != null && episodeScore != null && totalTime != null) {
			uhcScoreboard.resetScores(timer.getEntry());
			uhcScoreboard.resetScores(episodeScore.getEntry());
			uhcScoreboard.resetScores(totalTime.getEntry());
		}
		infoObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		timer = infoObjective.getScore( ChatColor.GRAY + "  »  " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Time left: " + ChatColor.GRAY + minutes + ":" + seconds);
		totalTime = infoObjective.getScore( ChatColor.GRAY + "  »  " + ChatColor.GREEN + ChatColor.BOLD + "Total time: " + ChatColor.GRAY + total);
		episodeScore = infoObjective.getScore( ChatColor.GRAY + "  »  " + ChatColor.RED + ChatColor.BOLD + "Episode: " + ChatColor.GRAY + episode);
		fullLine = infoObjective.getScore(ChatColor.GRAY + "§m                                ");
		blankLines.addAll(Arrays.asList(infoObjective.getScore(""), infoObjective.getScore(" "), infoObjective.getScore("  "), infoObjective.getScore("   "), infoObjective.getScore("    "), infoObjective.getScore("     ")));
		blankLines.get(0).setScore(8);
		episodeScore.setScore(7);
		blankLines.get(1).setScore(6);
		timer.setScore(5);
		blankLines.get(2).setScore(4);
		fullLine.setScore(3);
		blankLines.get(3).setScore(2);
		totalTime.setScore(1);
		blankLines.get(3).setScore(0);
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			p.setScoreboard(uhcScoreboard);
		}
	}

	/* Changes the sidebar title
	* @param newTitle - the new title of the sidebar objective */
	static void refreshSidebarTitle(String newTitle) {
		infoObjective.setDisplayName(newTitle);
	}

	public static void rmPlayer(Player player) {
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

}
