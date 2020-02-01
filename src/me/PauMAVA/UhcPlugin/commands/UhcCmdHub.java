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

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.lang.PluginStrings;
import me.PauMAVA.UhcPlugin.teams.UhcTeamsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
						((Player) theSender).getPlayer().sendMessage(ChatColor.RED + subCmd + PluginStrings.UNSUPPORTED_CMD.toString());
					} else {
						plugin.getPluginLogger().warning(ChatColor.RED + subCmd + PluginStrings.UNSUPPORTED_CMD.toString());
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
