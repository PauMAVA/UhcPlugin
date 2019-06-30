package me.PauMAVA.UhcPlugin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UhcConfigCmd {
	
	private static UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static CommandSender staticSender;
	private static final List<String> canBeNegative = Arrays.asList("season");
	private static final List<String> allConfigOptions = Arrays.asList("season","chapter_length","border_radius");
	private static final List<String> canHandleAutoValues = Arrays.asList("border_radius");
	private static final List<String> legalDifficulties = Arrays.asList("peaceful","easy","normal","hard");
	
	public static void config(CommandSender theSender, String[] args) {
		staticSender = theSender;
		boolean legalArgs = checkForValidArgs(theSender, args.length);
		if(legalArgs == false) {
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
			//TODO .CONTAINS(STRING) LIST DOES NOT WORK PROPERLY
			case "difficulty": {
				if(legalDifficulties.contains(option.toString()) == true) {
					setString(option, value);
				} else {
					sendMessage(staticSender,ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "The option " + option + " cannot handle the value \"" + value + "\". It only accepts the following values: " + legalDifficulties);
				}
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
			length --;
			sendMessage(theSender, ChatColor.RED + "Argument number mismatch! Expected two arguments and recieved " + length);
			sendMessage(theSender, ChatColor.RED + "Usage: /uhc config <option> <value>");
			return false;
		}
		return true;
	}
	
	private static void sendMessage(CommandSender theSender, String msg) {
		if(theSender instanceof Player) {
			Player p = ((Player) theSender).getPlayer();
			p.sendMessage(msg);
		} else {
			UhcPluginCore.UhcLogger.info(msg);
		}
		return;
	}
	
	private static void setInt(String path, String data) {
		if(data.equalsIgnoreCase("auto") && canHandleAutoValues.contains(path)) {
			plugin.getConfig().set(path, data);
		} else {
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
				sendMessage(staticSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "the parameter " + path + " only accepts integer values!");
				return;
			} catch(NullPointerException e) {
				sendMessage(staticSender, ChatColor.DARK_RED + "[Error] " + ChatColor.RED + "you must provide a value for the parameter " + path);
				return;
			}
		}
		plugin.saveConfig();
		sendMessage(staticSender, ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "the parameter " + path + " has been assigned the value " + data + " successfully!");
		return;
	}
	
	private static void setString(String path, String data) {
		Bukkit.broadcastMessage("test");
		return;
	}
}
