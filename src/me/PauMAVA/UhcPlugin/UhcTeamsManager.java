package me.PauMAVA.UhcPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UhcTeamsManager {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	protected static TeamsFile teamsConfig = null;
	
	public static void createTeamsFile() {
			/*Null parameter stands for default FileName (teams.yml)*/
			teamsConfig = new TeamsFile(null);
			return;
	}
	
	public static void cmdReciever(CommandSender theSender,String[] args) {
		boolean legalArgs = checkForLegalArgs(theSender, args);
		if(!legalArgs) {
			return;
		}
		switch(args[0]) {
			case "register": {
				boolean exists = !teamsConfig.registerTeam(args[1]);
				teamsConfig.saveConfig();
				if(exists == true) {
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The team '" + args[1] + "' already exists! Use the add option to add players to this team.");
				} else {
					sendMessage(theSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The team '" + args[1] + "' was registered successfully!");
				}
				break;
			} 
			case "add": {
				break;
			}
			case "kick": {
				break;
			}
			case "delete": {
				boolean deleted = teamsConfig.deleteTeam(args[1]);
				teamsConfig.saveConfig();
				if(deleted == true) {
					sendMessage(theSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The team '" + args[1] + "' has been deleted successfully!");
				} else {
					sendMessage(theSender, ChatColor.GOLD + "[Warning] " + ChatColor.YELLOW + "The team '" + args[1] + "' couldn't be deleted! Perhaps it didn't exist.");
				}
				break;
			}
		}
		return;
	}
	
	/*Options for teams SubCommand are: 
	 * register <teamName>
	 * add <player> <teamName>
	 * kick <player> <teamName>
	 * delete <teamName>*/
	private static boolean checkForLegalArgs(CommandSender theSender, String[] args) {
		int length = args.length;
		switch(args[0]) {
			case "register": {
				if(length != 2) {
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Invalid arguments for the option register in the command /uhc teams <option>!");
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Usage of option register: /uhc teams register <teamName>");
					return false;
				}
				break;
			} 
			case "add": {
				if(length != 3) {
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Invalid arguments for the option add in the command /uhc teams <option>!");
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Usage of option add: /uhc teams add <player> <teamName>");
					return false;
				}
				break;
			}
			case "kick": {
				if(length != 3) {
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Invalid arguments for the option kick in the command /uhc teams <option>!");
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Usage of option kick: /uhc teams kick <player> <teamName>");
					return false;
				}
				break;
			}
			case "delete": {
				if(length != 2) {
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Invalid arguments for the option delete in the command /uhc teams <option>!");
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Usage of option delete: /uhc teams delete <teamName>");
					return false;
				}
				break;
			}
			default: {
				sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "No such option available for the command /uhc teams <option>!");
				sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Available options are: register, add, kick, and delete.");
				return false;
			}
		}
		return true;
	}
	
	private static void sendMessage(CommandSender theSender, String message) {
		if(theSender instanceof Player) {
			Player player = (Player) theSender;
			player.sendMessage(message);
		} else {
			plugin.getPluginLogger().info(message);
		}
		return;
	}

	
}
