package me.PauMAVA.UhcPlugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class UhcPluginCore extends JavaPlugin {

    @Override
    public void onEnable() {
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
}
