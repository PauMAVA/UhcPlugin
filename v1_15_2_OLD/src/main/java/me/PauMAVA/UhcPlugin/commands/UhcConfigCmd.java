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
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UhcConfigCmd {
	
	private static UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static CommandSender staticSender;
	private static final List<String> canBeNegative = Arrays.asList("season");
	private static final List<String> allConfigOptions = Arrays.asList("get","season","chapter_length","border_radius","difficulty","closable_border","final_radius","border_closing_episode","rotate_skins");
	private static final List<String> legalDifficulties = Arrays.asList("peaceful","easy","normal","hard");
	
	public static FileConfiguration getConfig() {
		return plugin.getConfig();
	}
	
	public static void config(CommandSender theSender, String[] args) {
		staticSender = theSender;
		boolean legalArgs = checkForValidArgs(theSender, args.length);
		if(args[1].equalsIgnoreCase("get")) {
			Set<String> keySet = printAllConfig();
			sendMessage(staticSender, PluginStrings.CURRENT_CONFIG_HEADER.toString());
			for(String key: keySet) {
				sendMessage(staticSender, ChatColor.BLUE + " - " + key + ": " + ChatColor.AQUA + plugin.getConfig().get(key));
			}
			return;
		}
		if(!legalArgs) {
			int newLength = args.length;
			sendMessage(theSender, PluginStrings.CONFIG_ARGUMENT_NUMBER_MISMATCH.toString() + newLength--);
			sendMessage(theSender, PluginStrings.CONFIG_USAGE.toString());
			return;
		}
		String option = args[1];
		String value = args[2];
		switch(option) {
			case "season": {
				setString(option, value);
				break;
			}
			case "chapter_length":
			case "final_radius":
			case "border_closing_episode":
			case "border_radius": {
				setInt(option, value);
				break;
			}
			case "difficulty": {
				if(legalDifficulties.contains(value.toLowerCase())) {
					setString(option, value);
				} else {
					sendMessage(staticSender, PluginStrings.ERROR_PREFIX.toString() + option + PluginStrings.CONFIG_BAD_VALUE.toString() + "\'" + value + "\'." + PluginStrings.CONFIG_ACCEPTED_VALUE.toString() + legalDifficulties.toString());
				}
				break;
			}
			case "rotate_skins":
			case "closable_border": {
				if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
					setBoolean(option, value);
				} else {
					sendMessage(staticSender, PluginStrings.ERROR_PREFIX.toString() + option + PluginStrings.CONFIG_ONLYBOOLEAN.toString());
				}
				break;
			}
			default: {
				sendMessage(staticSender,PluginStrings.ERROR_PREFIX.toString() + ChatColor.RED + PluginStrings.CONFIG_NO_OPTION.toString() + allConfigOptions);
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
			if(value < 0 && canBeNegative.contains(path)) {
				sendMessage(staticSender, PluginStrings.WARNING_PREFIX.toString() + PluginStrings.CONFIG_THE_VALUE.toString() + path + PluginStrings.CONFIG_NEGATIVE_NO_PROBLEM.toString());
			} 
			if(value < 0 && !canBeNegative.contains(path)) {
				sendMessage(staticSender, PluginStrings.ERROR_PREFIX.toString() + ChatColor.RED + PluginStrings.CONFIG_THE_VALUE.toString() + path + PluginStrings.CONFIG_NEGATIVE_PROBLEM.toString());
				return;
			}
			plugin.getConfig().set(path, value);
		} catch(NumberFormatException e) {
			sendMessage(staticSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.CONFIG_PARAMETER.toString() + path + PluginStrings.CONFIG_ONLYINT.toString());
			return;
		} catch(NullPointerException e) {
			sendMessage(staticSender, PluginStrings.ERROR_PREFIX.toString() + PluginStrings.CONFIG_NOVALUE.toString() + path);
			return;
		}
		plugin.saveConfig();
		sendMessage(staticSender, PluginStrings.INFO_PREFIX.toString() + PluginStrings.CONFIG_PARAMETER.toString() + path + PluginStrings.CONFIG_VALUE_ASSIGNED.toString() + data + PluginStrings.CONFIG_SUCCESS.toString());
	}
	
	private static void setString(String path, String data) {
		plugin.getConfig().set(path, data);
		plugin.saveConfig();
		sendMessage(staticSender, PluginStrings.INFO_PREFIX.toString() + PluginStrings.CONFIG_PARAMETER.toString() + path + PluginStrings.CONFIG_VALUE_ASSIGNED.toString() + data + PluginStrings.CONFIG_SUCCESS.toString());
	}
	
	private static void setBoolean(String path, String data) {
		boolean bool = Boolean.parseBoolean(data);
		plugin.getConfig().set(path, bool);
		plugin.saveConfig();
		sendMessage(staticSender, PluginStrings.INFO_PREFIX.toString() + PluginStrings.CONFIG_PARAMETER.toString() + path + PluginStrings.CONFIG_VALUE_ASSIGNED.toString() + data + PluginStrings.CONFIG_SUCCESS.toString());
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
			default: {
				return Difficulty.HARD;
			}
		}
	}

	public static String getLocale() {
		return plugin.getConfig().getString("lang");
	}

	public static boolean getSkinRotation() {
		return plugin.getConfig().getBoolean("rotate_skins");
	}

	private static Set<String> printAllConfig() {
		return plugin.getConfig().getKeys(false);
	}

	public static double getDrownedDropRate(){
		return plugin.getConfig().getDouble("trident_drop");
	}
	
}
