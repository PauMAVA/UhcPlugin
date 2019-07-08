package me.PauMAVA.UhcPlugin;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

import org.bukkit.configuration.file.YamlConfiguration;

public class TeamsFile {
	
	private static UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static File file;
	private static YamlConfiguration teamsConfig;

	public TeamsFile(@Nullable String fileName) {
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		if(fileName == null) {
			fileName = "teams";
		}
		file = new File(plugin.getDataFolder() + "\\" + fileName + ".yml");
		teamsConfig = YamlConfiguration.loadConfiguration(file);
		if(!file.exists()) {
			try {
				file.createNewFile();
				plugin.getPluginLogger().info("Generated teams.yml file!");
				loadDefaults();
				saveConfig();
			} catch(IOException e) {
				e.printStackTrace();
				plugin.getPluginLogger().severe("Could not create/save Teams file!");
			}
		}
	}
	
	private static void loadDefaults() {
		teamsConfig.addDefault("solo", false);
		teamsConfig.addDefault("teamSize", 2);
		teamsConfig.options().copyDefaults(true);
		plugin.getPluginLogger().info("Added defaults!");
	}
	
	public void saveConfig() {
		try {
			teamsConfig.save(file);
		} catch (IOException e) {
			plugin.getPluginLogger().severe("Couldn't save config file! IOException.");
			e.printStackTrace();
		}
		plugin.getPluginLogger().info("Saved TeamsConfig!");
	}
	
	public boolean registerTeam(String teamName) {
		if(!teamsConfig.isSet("teams." + teamName)) {
			teamsConfig.createSection("teams." + teamName);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean deleteTeam(String teamName) {
		if(teamsConfig.getConfigurationSection("teams." + teamName) == null) {
			return false;
		} else {
			teamsConfig.set("teams." + teamName, null);
			return true;
		}
	}
	
	public int addPlayer(String teamName, String playerName) {
		return 0;
	}

}
