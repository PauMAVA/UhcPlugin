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

package me.PauMAVA.UhcPlugin.teams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.teams.TeamsFile;
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
	
	public static TeamsFile getTeamsManagementFile() {
		return teamsConfig;
	}
	
	public static void cmdReciever(CommandSender theSender,String[] args) {
		boolean legalArgs = checkForLegalArgs(theSender, args);
		if(!legalArgs) {
			return;
		}
		switch(args[0].toLowerCase()) {
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
				int code = teamsConfig.addPlayer(args[1], args[2]);
				exitCodesHandler(theSender, code, args, "add");
				teamsConfig.saveConfig();
				break;
			}
			case "kick": {
				int code = teamsConfig.kickPlayer(args[1], args[2]);
				exitCodesHandler(theSender, code, args, "kick");
				teamsConfig.saveConfig();
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
			case "setmaxsize": {
				int newSize = Integer.parseInt(args[2]);
				int actualSize = teamsConfig.getTeamMembers(args[1]).size();
				if(newSize == actualSize) {
					sendMessage(theSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The team " + args[1] + " already had a size of " + newSize + "players. No changes were made!");
					break;
				}
				if(newSize < actualSize) {
					List<String> ls = teamsConfig.settleNewSize(args[1] , actualSize - newSize);
					sendMessage(theSender, ChatColor.GOLD + "[Warning] " + ChatColor.YELLOW + "The team size you specified was smaller than the previous one, so the following players were autokicked: ");
					for(String kickedPlayer: ls) {
						sendMessage(theSender, ChatColor.YELLOW + "  - " + kickedPlayer);
					}
					sendMessage(theSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The team size has been succesfully set to " + args[1] + ".");
					teamsConfig.saveConfig();
					break;
				}
				boolean check = teamsConfig.setMaxTeamSize(args[1],args[2]);
				teamsConfig.saveConfig();
				if(check == true) {
					sendMessage(theSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The team size for " + args[1] +  " has been succesfully set to " + args[2] + ".");
				} else {
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The team size value couldn't be updated due to an unexpected error. Maybe the file is not reachable!");
				}
				break;
			}
			/* If the user provides no argument to the command get, then list all teamNames and integrants + teamSize. If he provides a string arg then print all users in team + the teamSize*/
			case "get": {
				/* If args[1] is null then it will raise ArrayIndexOutOfBoundsException so we better handle that using a try-catch statement. 
				 * If the ArrayIndexOutOfBoundsException is raised, then we will send null as an argument as getTeamCm() method accepts null arguments (@Nullable) */
				int exitCode;
				try {
					String teamName = args[1];
					exitCode = getTeamCmd(theSender, teamName);
				} catch(ArrayIndexOutOfBoundsException e) {
					exitCode = getTeamCmd(theSender, null);
				}				
				if(exitCode == 1) {
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The team you specified doesn't exist! Please register it using the register option!");
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
	 * delete <teamName>
	 * setMaxSize <number> */
	private static boolean checkForLegalArgs(CommandSender theSender, String[] args) {
		int length = args.length;
		switch(args[0].toLowerCase()) {
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
			case "setmaxsize": {
				if(length != 3 || isNotInt(args[2])) {
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Invalid arguments for the option setMaxSize in the command /uhc teams <option>!");
					sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Usage of option setMaxSize: /uhc teams setMaxSize <teamName> <number>");
					return false;
				}
				break;
			}
			case "get": {
				/* Arguments are optional so no check is required */
				return true;
			}
			default: {
				sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "No such option available for the command /uhc teams <option>!");
				sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "Available options are: register, add, kick, delete, setMaxSize.");
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
	
	private static boolean isNotInt(String s) {
		try {
			int i = Integer.parseInt(s);
			return false;
		} catch(NumberFormatException e) {
			return true;
		}
	}

	public static String getPlayerTeam(String playerName) {
		Collection<String> teamsLs = teamsConfig.getTeams(); // returns object of Collection interface so a cast to List child interface is needed
		for(String teamName: teamsLs) {
			List<String> localTeamList = teamsConfig.getTeamMembers(teamName);
			if(localTeamList.contains(playerName)) {
				return teamName;
			}
		}
		return null;
	}
	
	private static int getTeamCmd(CommandSender theSender, @Nullable String teamName) {
		if(teamName == null) {
			sendMessage(theSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "You didn't specify the team you want to fetch, so all teams and respective integrants will be printed!");
			List<String> teamsLs = new ArrayList<String>(teamsConfig.getTeams());
			if(teamsLs.isEmpty()) {
				sendMessage(theSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "There are no teams registered yet!");
				return 0;
			}
			for(String name: teamsLs) {
				printTeamInfo(theSender, name);
			}
			return 0;
		}
		if(!teamsConfig.checkTeamExistance(teamName)) {
			return 1;
		}
		printTeamInfo(theSender, teamName);
		return 0;
	}
	
	private static void printTeamInfo(CommandSender theSender, String name) {
		int localTeamMaxSize = teamsConfig.getTeamMaxSize(name);
		sendMessage(theSender, ChatColor.BLUE + name + ": ");
		sendMessage(theSender, ChatColor.AQUA + "\'" + name + "\' maximum size: " + localTeamMaxSize);
		List<String> playersLs = teamsConfig.getTeamMembers(name);
		if(playersLs.isEmpty()) {
			sendMessage(theSender, ChatColor.AQUA + "The team " + name + " is empty!");
		} else {
			int freeSpots = localTeamMaxSize - playersLs.size();
			sendMessage(theSender, ChatColor.AQUA + "The team " + name + " has " + playersLs.size() + " integrants. There are " + freeSpots + " spots free!");
			for(String playerName: playersLs) {
				sendMessage(theSender, ChatColor.AQUA + "  - " + playerName);
			}
		}
		return;
	}
	
	public static List<String> getTeamMembers(String teamName) {
		return teamsConfig.getTeamMembers(teamName);
	}
	
	private static void exitCodesHandler(CommandSender theSender, int code, String[] args, String operation) {
		/* Exit codes:
		 * - 0 --> Successfull operation
		 * - 1 --> The specified team doesn't exist
		 * - 2 --> The player doesn't exist in that team
		 * - 3 --> Operation failed as the team is full */
		switch(code) {
			case 0: {
				sendMessage(theSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The player " + args[1] + " has been "  + operation + "ed to the team " + args[2]);
				break;
			}
			case 1: {
				sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The team \'" + args[2] + "\' doesn't exist. register it via the register option!");
				break;
			}
			case 2: {
				sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The player you specified wasn't found on the team \'" + args[2] + "\'");
				break;
			}
			case 3: {
				sendMessage(theSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The player couldn't be added to the team as it is full. Please increase its maximum size with the setMaxSize option!");
				break;
			}
		}
		return;
	}
}
