package me.PauMAVA.UhcPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
			int minutes = 24;
			int episode = 1;
			
			@Override
			public void run() {
				if(seconds < 0) {
					seconds = 59;
					minutes --;
					if(minutes < 0) {
						minutes = 24;
						episode --;
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
		uhcScoreboard = scManager.getNewScoreboard();
		Objective timerObjective = uhcScoreboard.registerNewObjective("timer", "dummy", ChatColor.BOLD + "" + ChatColor.UNDERLINE + ChatColor.GOLD + "UHC T" + plugin.getConfig().getInt("season"));
		Score timerScore = timerObjective.getScore(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + ChatColor.DARK_RED + minutes + ":" + seconds);
		timerScore.setScore(0);
		timerObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		lifeList();
		for(Player player: Bukkit.getOnlinePlayers()) {
			player.setScoreboard(uhcScoreboard);
		}
	}
	
}
