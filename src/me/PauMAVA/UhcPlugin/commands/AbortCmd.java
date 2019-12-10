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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*Further development needed:
 * - Create permissions.yml and create a permission to execute this command.
 * - Players must not be able to add themselves to this permission so always check that the permission add request is
 * created by the console.
 * - Use clickable text to confirm the abort action.
 * - Commit Bukkit.getScheduler().cancelTasks();
 * The permissions module must be developed before impkementing this command!
 * */



public class AbortCmd implements CommandExecutor {
	
	UhcPluginCore directPlugin;
	public AbortCmd(UhcPluginCore passedPlugin) {
		this.directPlugin = passedPlugin;
	}
	
	public boolean onCommand(CommandSender theSender, Command cmd, String label, String[] args) {
		
		/*Check if the sender has permission to commit the command*/
		if(theSender instanceof Player) {
			theSender.sendMessage(ChatColor.RED + "Are you sure you want to abort UHC? This will stop the game, even if it has started!");
		}
		
		return true;
	}
}
