package me.PauMAVA.UhcPlugin;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class UhcStartCmd {
	private static long counter;
	private static BukkitTask task;
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	
	/*Main command method*/
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
					UhcScoreboard.setUp();
					UhcWorldConfig.setBorder(plugin.getConfig().getDouble("border_radius"));
					/* THIS IS ARBITRAY NOW, BUT IT IS ECPECTED TO BE ADDED TO THE CONFIG.YML FILE SO THAT THE USER CAN SPECIFY THE CUSTOM SETTIGNS FOR THEIR MATCH */
					UhcWorldConfig.setRules(UhcWorldConfig.getRules());
					UhcWorldConfig.setDifficulty(Difficulty.HARD);
					//TODO RANDOM TELEPORT
					Bukkit.getScheduler().cancelTask(task.getTaskId());
				}
			}
		}, 0L, 20L);
		return;
	}
}
