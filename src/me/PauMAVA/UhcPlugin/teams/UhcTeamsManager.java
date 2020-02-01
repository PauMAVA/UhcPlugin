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

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.chat.Prefix;
import me.PauMAVA.UhcPlugin.lang.PluginStrings;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UhcTeamsManager {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	protected static TeamsFile teamsConfig = null;
	
	public static void createTeamsFile() {
			/*Null parameter stands for default FileName (teams.yml)*/
			teamsConfig = new TeamsFile(null);
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
				if(exists) {
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_THE_TEAM.toString() + "\'" + args[1] + "\'" + PluginStrings.TEAMS_ALREADY_EXISTS.toString());
				} else {
					sendMessage(theSender, PluginStrings.INFO_PREFIX.toString() +  PluginStrings.TEAMS_THE_TEAM.toString() + "\'" + args[1] + "\'"  + PluginStrings.TEAMS_REGISTER_SUCCESS.toString());
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
				if(deleted) {
					sendMessage(theSender, PluginStrings.INFO_PREFIX.toString() + PluginStrings.TEAMS_THE_TEAM.toString() + "\'" + args[1] + "\'" + PluginStrings.TEAMS_DELETE_SUCCESS.toString());
				} else {
					sendMessage(theSender, PluginStrings.WARNING_PREFIX.toString() + PluginStrings.TEAMS_THE_TEAM.toString() + "\'" + args[1] + "\'" + PluginStrings.TEAMS_NOT_EXISTS.toString());
				}
				break;
			}	
			case "setmaxsize": {
				int newSize = Integer.parseInt(args[2]);
				int actualSize = teamsConfig.getTeamMembers(args[1]).size();
				if(newSize == actualSize) {
					sendMessage(theSender, PluginStrings.INFO_PREFIX.toString() + PluginStrings.TEAMS_THE_TEAM.toString() + args[1] + PluginStrings.TEAMS_SAME_SIZE.toString() + newSize + PluginStrings.TEAMS_NO_CHANGES.toString());
					break;
				}
				if(newSize < actualSize) {
					List<String> ls = teamsConfig.settleNewSize(args[1] , actualSize - newSize);
					sendMessage(theSender, PluginStrings.WARNING_PREFIX.toString() + PluginStrings.TEAMS_AUTOKICK_NOTICE.toString());
					for(String kickedPlayer: ls) {
						sendMessage(theSender, ChatColor.YELLOW + "  - " + kickedPlayer);
					}
					sendMessage(theSender, PluginStrings.INFO_PREFIX.toString() + PluginStrings.TEAMS_SIZE_CHANGE_SUCCESS.toString() + args[1] + ".");
					teamsConfig.saveConfig();
					break;
				}
				boolean check = teamsConfig.setMaxTeamSize(args[1],args[2]);
				teamsConfig.saveConfig();
				if(check) {
					sendMessage(theSender, PluginStrings.INFO_PREFIX.toString() + args[1] +  PluginStrings.TEAMS_SIZE_CHANGE_SUCCESS2.toString() + args[2] + ".");
				} else {
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_UNEXPECTED_ERROR.toString());
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
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_GET_NOT_EXISTS.toString());
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
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_REGISTER_NOTICE.toString());
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_REGISTER_USAGE.toString());
					return false;
				}
				break;
			} 
			case "add": {
				if(length != 3) {
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_ADD_NOTICE.toString());
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_ADD_USAGE.toString());
					return false;
				}
				break;
			}
			case "kick": {
				if(length != 3) {
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_KICK_NOTICE.toString());
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_KICK_USAGE.toString());
					return false;
				}
				break;
			}
			case "delete": {
				if(length != 2) {
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_DELETE_NOTICE.toString());
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_DELETE_USAGE.toString());
					return false;
				}
				break;
			}
			case "setmaxsize": {
				if(length != 3 || isNotInt(args[2])) {
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_MAXSIZE_NOTICE.toString());
					sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_MAXSIZE_USAGE.toString());
					return false;
				}
				break;
			}
			case "get": {
				/* Arguments are optional so no check is required */
				return true;
			}
			default: {
				sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_NO_SUCH_OPTION.toString());
				sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_LEGAL_ARGS_AVAILABLE_OPTIONS.toString());
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
		Collection<String> teamsLs = teamsConfig.getTeams();
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
			sendMessage(theSender, PluginStrings.INFO_PREFIX.toString() + PluginStrings.TEAMS_FETCH_ALL.toString());
			List<String> teamsLs = new ArrayList<String>(teamsConfig.getTeams());
			if(teamsLs.isEmpty()) {
				sendMessage(theSender, PluginStrings.INFO_PREFIX.toString() + PluginStrings.TEAMS_NO_REGISTERED_TEAMS.toString());
				return 0;
			}
			for(String name: teamsLs) {
				printTeamInfo(theSender, name);
			}
			return 0;
		}
		if(!teamsConfig.teamExists(teamName)) {
			return 1;
		}
		printTeamInfo(theSender, teamName);
		return 0;
	}
	
	private static void printTeamInfo(CommandSender theSender, String name) {
		int localTeamMaxSize = teamsConfig.getTeamMaxSize(name);
		sendMessage(theSender, ChatColor.BLUE + name + ": ");
		sendMessage(theSender, ChatColor.AQUA + PluginStrings.TEAMS_MAX_SIZE.toString() + localTeamMaxSize);
		List<String> playersLs = teamsConfig.getTeamMembers(name);
		if(playersLs.isEmpty()) {
			sendMessage(theSender, ChatColor.AQUA + PluginStrings.TEAMS_THE_TEAM.toString() + name + PluginStrings.TEAMS_IS_EMPTY.toString());
		} else {
			int freeSpots = localTeamMaxSize - playersLs.size();
			sendMessage(theSender, ChatColor.AQUA + PluginStrings.TEAMS_THE_TEAM.toString() + name + PluginStrings.TEAMS_CONNECTOR + playersLs.size() + PluginStrings.TEAMS_INTEGRANTS.toString() + freeSpots + PluginStrings.TEAMS_FREE_SPOTS.toString());
			for(String playerName: playersLs) {
				sendMessage(theSender, ChatColor.AQUA + "  - " + playerName);
			}
		}
	}
	
	public static List<String> getTeamMembers(String teamName) {
		return teamsConfig.getTeamMembers(teamName);
	}
	
	private static void exitCodesHandler(CommandSender theSender, int code, String[] args, String operation) {
		/* Exit codes:
		 * - 0 --> Successful operation
		 * - 1 --> The specified team doesn't exist
		 * - 2 --> The player doesn't exist in that team
		 * - 3 --> Operation failed as the team is full */
		switch(code) {
			case 0: {
				sendMessage(theSender, PluginStrings.INFO_PREFIX.toString() + PluginStrings.TEAMS_OPERATION_SUCCESS.toString());
				break;
			}
			case 1: {
				sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_TEAM_NOT_EXISTS.toString());
				break;
			}
			case 2: {
				sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_PLAYER_NOT_FOUND.toString() + "\'" + args[2] + "\'");
				break;
			}
			case 3: {
				sendMessage(theSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.TEAMS_FULL_TEAM.toString());
				break;
			}
		}
	}

	public static UhcTeam getTeamObject(String teamName) {
		List<Player> players = new ArrayList<Player>();
		for(String playerName: getTeamMembers(teamName)) {
			players.add(Bukkit.getServer().getPlayer(playerName));
		}
		return new UhcTeam(teamName, players);
	}

	public static void eliminate(Player player) {
		UhcTeam playerTeam = plugin.getMatchHandler().getUhcTeam(player);
		playerTeam.markPlayerAsDead(player);
		String playerTeamName = playerTeam.getName();
		if(plugin.getMatchHandler().getUhcTeam(player).alive().size() == 0) {
			Bukkit.broadcastMessage(Prefix.INGAME_UHC + "" +  ChatColor.AQUA + player.getName() + PluginStrings.TEAMS_LAST_MEMBER.toString() + playerTeamName + "!" );
			Bukkit.getServer().broadcastMessage(Prefix.INGAME_UHC + "" + ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "------ " + ChatColor.RESET + ChatColor.GOLD + PluginStrings.TEAMS_THE_TEAM.toString() + playerTeamName + PluginStrings.TEAMS_ELIMINATED.toString() + ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + " ------");
			playerTeam.markPlayerAsDead(player);
			if(plugin.getMatchHandler().remainingTeams() <= 1) {
				/* TODO Winning team name */
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + plugin.getMatchHandler().getRemainingTeams().get(0).getName(),PluginStrings.TEAMS_WIN_SUBTITLE.toString(), 0, 5*20, 1*20);
				}
				UhcPluginCore.getInstance().getMatchHandler().end();
			}
		} else {
			Bukkit.broadcastMessage(Prefix.INGAME_UHC + "" + PluginStrings.TEAMS_THE_PLAYER.toString() + player.getName() + PluginStrings.TEAMS_PART_OF_TEAM.toString()  + playerTeamName + PluginStrings.TEAMS_CONNECTOR2.toString() + playerTeam.alive().size() + PluginStrings.TEAMS_PLAYERS_LEFT.toString());
		}
	}

	public static void revive(Player invoker, Block block) {
		UhcTeam team = plugin.getMatchHandler().getUhcTeam(invoker);
		invoker.getWorld().playEffect(block.getLocation(), Effect.DRAGON_BREATH, 100);
		new BukkitRunnable(){
			@Override
			public void run() {
				if(team.dead().size() == 0 || plugin.getMatchHandler().getTimer().getEpisode() >= 10 || team.hasBeenRevived(team.dead().get(0))) {
					invoker.sendMessage(Prefix.INGAME_UHC + "" + PluginStrings.TEAMS_DARK_CRYSTAL_WASTE.toString());
				} else {
					invoker.getWorld().strikeLightningEffect(block.getLocation());
					invoker.getWorld().spawn(new Location(invoker.getWorld(), block.getX(), block.getY() + 1, block.getZ()), EnderCrystal.class);
					block.setType(Material.AIR);
					Player revived = team.dead().get(0);
					team.markPlayerAsAlive(revived);
					team.markPlayerAsRevived(revived);
					revived.setGameMode(GameMode.SURVIVAL);
					revived.setExp(0);
					revived.setFoodLevel(20);
					revived.setHealth(20);
					revived.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 750, 1));
					revived.teleport(invoker);
					Bukkit.getServer().broadcastMessage(Prefix.INGAME_UHC + "" + PluginStrings.TEAMS_DARK_CRYSTAL_USE.toString());
				}
			}
		}.runTaskLater(plugin, 40L);
	}
}