package me.PauMAVA.UhcPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class UhcScoreboard {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	static final ScoreboardManager scManager = plugin.getServer().getScoreboardManager();
	public static final Scoreboard uhcScoreboard = scManager.getNewScoreboard();
	static Objective lifeObjective = uhcScoreboard.registerNewObjective("health", "health&quot", "health");
	
	public static void setUp() {
		timerSidebar();
		lifeList();
		for(Player player: Bukkit.getOnlinePlayers()) {
			player.setScoreboard(uhcScoreboard);
		}
		return;
	}
	
	private static void timerSidebar() {
		//TODO CONFIG NUMBER OF CHAPTERS AND DURATION OF EACH ONE BEFORE SCHEDULING THE ASYNC TASK
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				//TODO UPDATE SCOREBOARD
				return;
			}
		}, 0L, 20L);
		return;
	}
	
	private static void lifeList() {
		lifeObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		for(Player player: Bukkit.getOnlinePlayers()) {
			updateHealth(player);
		}
		return;
	}
	
	//TODO FIX --> SCORE SHOWN IS THE PREVIOUS LIFE STATE, NOT THE ACTUAL ONE!
	public static void updateHealth(Player player) {
		Score lifeScore = lifeObjective.getScore(player.getName());
		lifeScore.setScore((int) player.getHealth());
		lifeObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		return;
	}
	
}
