package me.PauMAVA.UhcPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UhcStartCmd implements CommandExecutor {
	
	/*Use constructors only for command execution*/
	UhcPluginCore directPlugin;
	public UhcStartCmd(UhcPluginCore passedPlugin) {
		this.directPlugin = passedPlugin;
	}
	
	private static long counter;
	private static int taskID;
	
	/*Main command method*/
	public boolean onCommand(CommandSender theSender, Command cmd, String label, String[] args) {
		counter = Integer.parseInt(args[0]);
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(directPlugin, new Runnable() {
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
		return true;
	}
}
