package me.PauMAVA.UhcPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UhcConfigCmd {
	
	private static UhcPluginCore plugin = UhcPluginCore.getInstance();
	
	public static void config(CommandSender theSender, String[] args) {
		boolean legalArgs = checkForValidArgs(theSender, args.length);
		if(legalArgs == false) {
			return;
		}
		String option = args[1];
		String value = args[2];
		switch(option) {
			case "season": {
				setInt("season", value);
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
		try {
			int value = Integer.parseInt(data);
			if(value < 0) {
				//TODO WARNING
			}
			plugin.getConfig().set(path, value);
			plugin.saveConfig();
		} catch(NumberFormatException e) {
				//TODO MESSAGE
		} catch(NullPointerException e) {
				//TODO MESSAGE
		}
		
		return;
	}
}