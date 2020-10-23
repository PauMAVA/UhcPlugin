package me.PauMAVA.UhcPlugin.commands;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MainCommandHandler implements CommandExecutor {

    private static final List<String> allowedCommands = Arrays.asList("teams", "start", "config");

    private final UhcPluginCore plugin;

    private final TeamsCommandHandler teamsHandler;

    public MainCommandHandler(UhcPluginCore plugin) {
        this.plugin = plugin;
        this.teamsHandler = new TeamsCommandHandler(plugin);
    }

    @Override
    public boolean onCommand(CommandSender theSender, Command cmd, String label, String[] args) {
        if (args.length <= 0 || !allowedCommands.contains(args[0])) {
            plugin.sendMessage(theSender, ChatColor.RED + "Available options for command /uhc: " + allowedCommands.toString());
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "teams": return teamsHandler.handleCommand(theSender, Arrays.asList(args).subList(1, args.length));
        }
        return true;
    }

}
