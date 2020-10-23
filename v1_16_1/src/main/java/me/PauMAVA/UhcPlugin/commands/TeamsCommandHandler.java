package me.PauMAVA.UhcPlugin.commands;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class TeamsCommandHandler implements UhcCommandHandler{

    private static List<String> availableCommands = Arrays.asList("register", "delete", "rename");

    private final UhcPluginCore plugin;

    public TeamsCommandHandler(UhcPluginCore plugin) {
        this.plugin = plugin;
    }

    public boolean handleCommand(CommandSender theSender, List<String> args) {
        if (args.size() <= 0) {
            plugin.sendMessage(theSender, ChatColor.RED + "Available commands for /uhc teams: " + availableCommands.toString());
            return false;
        }
        switch (args.get(0).toLowerCase()) {
            case "register": return false;
            case "delete": return false;
            case "rename": return false;
        }
        return false;
    }

}
