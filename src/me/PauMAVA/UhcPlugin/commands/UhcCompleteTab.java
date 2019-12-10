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

package me.PauMAVA.UhcPlugin.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.teams.UhcTeamsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class UhcCompleteTab implements TabCompleter {
	
	public static UhcPluginCore plugin = UhcPluginCore.getInstance();
	
	@Override
	public List<String> onTabComplete(CommandSender theSender, Command cmd, String label, String[] args) {
		if(theSender instanceof Player && label.equalsIgnoreCase("uhc")) {
			if(args.length == 1) {
				return basicCommands();
			}
			if(args.length == 2) {
				return firstArg(args[0]);
			}
			if(args.length == 3) {
				return secondArg(args[1]);
			}
			if(args.length == 4) {
				return thirdArg(args[1]);
			}
		} 
		return null;
	}
	
	private List<String> basicCommands() {
		ArrayList<String> subCommands = new ArrayList<String>();
		subCommands.addAll(Arrays.asList("start","config","teams"));
		return subCommands;
	}
	
	private List<String> firstArg(String subCommand) {
		if(subCommand.equalsIgnoreCase("config")) {
			return Arrays.asList("border_closing_episode", "border_radius", "chapter_length", "closable_border", "difficulty", "final_radius", "get","season");

		}
		if(subCommand.equalsIgnoreCase("teams")) {
			return Arrays.asList("add","delete","get","kick","register");
		}
		return new ArrayList<String>();
	}
	
	private List<String> secondArg(String firstArg) {
		if(firstArg.equalsIgnoreCase("closable_border ")) {
			return Arrays.asList("true","false");
		}
		if(firstArg.equalsIgnoreCase("add") || firstArg.equalsIgnoreCase("kick")) {
			return playerNamesList();
		}
		if(firstArg.equalsIgnoreCase("register") || firstArg.equalsIgnoreCase("delete")) {
			return new ArrayList<String>(UhcTeamsManager.getTeamsManagementFile().getTeams());
		}
		return new ArrayList<String>();
	}
	
	private List<String> thirdArg(String firstArg) {
		if(firstArg.equalsIgnoreCase("add") || firstArg.equalsIgnoreCase("kick")) {
			return new ArrayList<String>(UhcTeamsManager.getTeamsManagementFile().getTeams());
		}
		return new ArrayList<String>();
	}
	
	private List<String> playerNamesList() {
		List<String> returnList = new ArrayList<String>();
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			returnList.add(p.getName());
		}
		return returnList;
	}
}
