package me.PauMAVA.UhcPlugin;

import me.PauMAVA.UhcPlugin.commands.MainCommandHandler;
import me.PauMAVA.UhcPlugin.teams.UhcTeamManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class UhcPluginCore extends JavaPlugin {

    private final String PLUGIN_PREFIX = ChatColor.AQUA + "[UhcPlugin] " + ChatColor.RESET;

    private UhcTeamManager teamManager;

    @Override
    public void onEnable() {
        teamManager = new UhcTeamManager(this);
        registerCommands();
    }

    private void registerCommands() {
        getCommand("uhc").setExecutor(new MainCommandHandler(this));
    }

    public UhcTeamManager getTeamManager() {
        return teamManager;
    }

    public void logInfo(String message) {
        getLogger().log(Level.INFO, ChatColor.AQUA + " INFO " + ChatColor.RESET + "-- " + message);
    }

    public void logWarning(String message) {
        getLogger().log(Level.WARNING, ChatColor.YELLOW + " WARNING " + ChatColor.RESET + "-- " + message);
    }

    public void logSevere(String message) {
        getLogger().log(Level.SEVERE, ChatColor.DARK_RED + " ERROR " + ChatColor.RESET + "-- " + message);
    }

    public void broadcastMessage(String message) {
        getServer().broadcastMessage(PLUGIN_PREFIX + message);
    }

    public void sendMessage(CommandSender target, String message) {
        target.sendMessage(PLUGIN_PREFIX + message);
    }

}
