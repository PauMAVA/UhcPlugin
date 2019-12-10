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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UhcConfigCmd {
	
	private static UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static CommandSender staticSender;
	private static final List<String> canBeNegative = Arrays.asList("season");
	private static final List<String> allConfigOptions = Arrays.asList("get","season","chapter_length","border_radius","difficulty","closable_border","final_radius","border_closing_episode");
	private static final List<String> legalDifficulties = Arrays.asList("peaceful","easy","normal","hard");
	
	public static FileConfiguration getConfig() {
		return plugin.getConfig();
	}
	
	public static void config(CommandSender theSender, String[] args) {
		staticSender = theSender;
		boolean legalArgs = checkForValidArgs(theSender, args.length);
		if(args[1].equalsIgnoreCase("get")) {
			Set<String> keySet = printAllConfig();
			sendMessage(staticSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The current config is: ");
			for(String key: keySet) {
				sendMessage(staticSender, ChatColor.BLUE + " - " + key + ": " + ChatColor.AQUA + plugin.getConfig().get(key));
			}
			return;
		}
		if(legalArgs == false) {
			int newLength = args.length;
			sendMessage(theSender, ChatColor.RED + "Argument number mismatch! Expected two arguments and recieved " + newLength--);
			sendMessage(theSender, ChatColor.RED + "Usage: /uhc config <option> <value>");
			return;
		}
		String option = args[1];
		String value = args[2];
		switch(option) {
			case "season": {
				setInt(option, value);
				break;
			}
			case "chapter_length": {
				setInt(option, value);
				break;
			}
			case "border_radius": {
				setInt(option, value);
				break;
			}
			case "difficulty": {
				if(legalDifficulties.contains(value.toLowerCase()) == true) {
					setString(option, value);
				} else {
					sendMessage(staticSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The option " + option + " cannot handle the value \'" + value + "\'. It only accepts the following values: " + legalDifficulties.toString());
				}
				break;
			}
			case "closable_border": {
				if(value.equalsIgnoreCase("true") || option.equalsIgnoreCase("false")) {
					setBoolean(option, value);
				} else {
					sendMessage(staticSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The option " + option + " can only handle boolean values (true or false)! Please introduce a valid value.");
				}
				break;
			}
			case "final_radius": {
				setInt(option, value);
				break;
			}
			case "border_closing_episode": {
				setInt(option, value);
				break;
			}
			default: {
				sendMessage(staticSender,ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The option you introduced does not exist! Please use one of this configuration options: " + allConfigOptions);
				break;
			}
		}
	}
	
	private static boolean checkForValidArgs(CommandSender theSender, int length) {
		if(length != 3) {
			return false;
		}
		return true;
	}
	
	private static void sendMessage(CommandSender theSender, String msg) {
		if(theSender instanceof Player) {
			Player p = ((Player) theSender).getPlayer();
			p.sendMessage(msg);
		} else {
			plugin.getPluginLogger().info(msg);
		}
		return;
	}
	
	private static void setInt(String path, String data) {
		try {
			int value = Integer.parseInt(data);
			if(value < 0 && canBeNegative.contains(path) == true) {
				sendMessage(staticSender, ChatColor.GOLD + "[Warning] " + ChatColor.YELLOW + "You've set a negative " + path + " value! (This shouldn't cause any errors).");
			} 
			if(value < 0 && canBeNegative.contains(path) == false) {
				sendMessage(staticSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "You've set a negative " + path + " value, which is not supported. No changes were made!");
				return;
			}
			plugin.getConfig().set(path, value);
		} catch(NumberFormatException e) {
			sendMessage(staticSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The parameter " + path + " only accepts integer values!");
			return;
		} catch(NullPointerException e) {
			sendMessage(staticSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "You must provide a value for the parameter " + path);
			return;
		}
		plugin.saveConfig();
		sendMessage(staticSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The parameter " + path + " has been assigned the value " + data + " successfully!");
		return;
	}
	
	private static void setString(String path, String data) {
		plugin.getConfig().set(path, data);
		plugin.saveConfig();
		sendMessage(staticSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "the parameter " + path + " has been assigned the value " + data + " successfully!");
		return;
	}
	
	private static void setBoolean(String path, String data) {
		boolean bool = Boolean.parseBoolean(data);
		plugin.getConfig().set(path, bool);
		plugin.saveConfig();
		sendMessage(staticSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "the parameter " + path + " has been assigned the value " + data + " successfully!");
		return;
	}
	
	public static Difficulty getDifficultyObject() {
		String plainValue = plugin.getConfig().getString("difficulty");
		switch(plainValue) {
			case "peaceful": {
				return Difficulty.PEACEFUL;
			}
			case "easy": {
				return Difficulty.EASY;
			}
			case "normal": {
				return Difficulty.NORMAL;
			}
			case "hard": {
				return Difficulty.HARD;
			}
			default: {
				return Difficulty.HARD;
			}
		}
	}
	
	private static Set<String> printAllConfig() {
		return plugin.getConfig().getKeys(false);
	}
	
}
