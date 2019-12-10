/*
 * UhcPlugin
 * Copyright (c) 2019  Pau Machetti Vallverdú
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
import me.PauMAVA.UhcPlugin.world.UhcWorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class UhcScoreboardManager {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	static final ScoreboardManager scManager = plugin.getServer().getScoreboardManager();
	static Scoreboard uhcScoreboard = scManager.getNewScoreboard();
	static String sPrefix,mPrefix = "";
	private static int chapterLength = plugin.getConfig().getInt("chapter_length");
	
	public static void setUp() {
			lifeList();
			for(Player player: Bukkit.getOnlinePlayers()) {
				player.setScoreboard(uhcScoreboard);
			}
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
	
	/* Updates the health on the list scoreboard
	* @param player - the player from which the health will be updated
	* @param lifeObjective - the objective of the  */
	static void updateHealth(Player player, Objective lifeObjective) {
		Score lifeScore = lifeObjective.getScore(player.getName());
		lifeScore.setScore((int) player.getHealth());
		lifeObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		return;
	}

	/* Sets up the scoreboard sidebar
	* @param player - the player to whom the sidebar will be applied
	* @param minutes - the minutes that will be displayed in the sidebar
	* @param seconds - the seconds that will be displayed in the sidebar
	* @param episode - the episode that will be displayed in the sidebar
	* @param total - the total time that the match has been going on */
	void setSidebar(Player player, int minutes, int seconds, int episode, int total) {

	}
}
