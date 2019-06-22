package me.PauMAVA.UhcPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class UhcCompleteTab implements TabCompleter {
	
	public static UhcPluginCore plugin = UhcPluginCore.getInstance();
	
	@Override
	public List<String> onTabComplete(CommandSender theSender, Command cmd, String label, String[] args) {
		if(theSender instanceof Player && label.equalsIgnoreCase("uhc")) {
			ArrayList<String> subCommands = new ArrayList<String>();
			subCommands.addAll(Arrays.asList("start","config","permissions"));
			return subCommands;
		} 
		if(args[0].equalsIgnoreCase("config")) {
			ArrayList<String> configArgs = new ArrayList<String>();
			configArgs.addAll(Arrays.asList("season","language"));
		}
		return null;
	}
}
