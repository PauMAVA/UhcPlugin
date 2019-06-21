package me.PauMAVA.UhcPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class UhcStartCmd {
	private static long counter;
	private static int taskID;
	
	/*Main command method*/
	public static void start(String[] args) {
		counter = Integer.parseInt(args[1]);
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(UhcPluginCore.getInstance(), new Runnable() {
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
					for(Player player: Bukkit.getOnlinePlayers()) {
						player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
						player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "UHC Tx",ChatColor.AQUA + "" + ChatColor.BOLD +  "STARTS NOW!", 0, 5*20, 1*20);
					}
					Bukkit.getScheduler().cancelTask(taskID);
				}
			}
		}, 0L, 20L);
		return;
	}
}
