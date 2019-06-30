package me.PauMAVA.UhcPlugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TeamsFile {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static FileConfiguration teamsConfig;
	private static File theFile;
	
	public TeamsFile() throws IOException{
		theFile = new File(plugin.getDataFolder(), "teams.yml");
		if(!theFile.exists()) {
			theFile.createNewFile();
		}
		teamsConfig = YamlConfiguration.loadConfiguration(theFile);
	}
	
	public TeamsFile(String fileName) throws IOException {
		String ymlName = fileName + ".yml";
		theFile = new File(plugin.getDataFolder(), ymlName);
		if(!theFile.exists()) {
			theFile.createNewFile();
		}
		teamsConfig = YamlConfiguration.loadConfiguration(theFile);
	}
	
	void loadDefaults() {
		teamsConfig.addDefault("solo", false);
		teamsConfig.addDefault("teamSize", 2);
		teamsConfig.addDefault("teams", null);
		UhcPluginCore.UhcLogger.info("Added defaults!");
	}
	
	void saveConfig() throws IOException {
		teamsConfig.save(theFile);
		UhcPluginCore.UhcLogger.info("Saved!");
	}
	

}
