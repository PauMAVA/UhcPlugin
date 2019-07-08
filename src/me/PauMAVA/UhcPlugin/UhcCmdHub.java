package me.PauMAVA.UhcPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UhcCmdHub implements CommandExecutor {

	public static UhcPluginCore plugin = UhcPluginCore.getInstance();
	
	@Override
	public boolean onCommand(CommandSender theSender, Command cmd, String label, String[] args) {
		/*Handle the situation when the player doesn't provide a subcommand*/
		try {
			final String subCmd = args[0].toLowerCase();
			/*The use of the switch makes it easier to implement new subcommands in the future*/
			switch(subCmd) {
				case "start": {
					/*Launch Uhc Initalitzation sequence*/
					UhcStartCmd.start(args);
					break;
				}
				case "config": {
					UhcConfigCmd.config(theSender,args);
					break;
				}
				case "permissions": {
					/*TODO Redirect to permission class*/
					break;
				}
				case "teams": {
					List<String> argsList = new ArrayList<String>(Arrays.asList(args));
					argsList.remove(0);
					args = argsList.toArray(new String[0]);
					UhcTeamsManager.cmdReciever(theSender, args);
					break;
				}
				default: {
					if(theSender instanceof Player) {
						((Player) theSender).getPlayer().sendMessage(ChatColor.RED + subCmd + " is not  a vaild option for /uhc <subCmd> [args]");
					} else {
						plugin.getPluginLogger().warning(ChatColor.RED + subCmd + " is not  a vaild option for /uhc <subCmd> [args]");
					}
					return false;
				}
			}
			return true;
		} catch(ArrayIndexOutOfBoundsException e) {
			/*TODO Show a help list or command*/
			return false;
		}
	}
	
}
