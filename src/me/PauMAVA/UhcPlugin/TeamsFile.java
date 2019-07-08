/* FUTURE IMPLEMENTATIONS
 * - Command: /uhc teams get [arg].
 *   if(arg == "ALL") then display all teams and its integrants.
 *   if(arg == String) then String = teamName and if(teamsConfig exists) then display teamName.
 * - When you decrease the teamSize autokick last member.
 * - Develop kick method.
 * - Handle null args!
 * */
package me.PauMAVA.UhcPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
			teamsConfig.createSection("teams." + teamName).set("members", null);;
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
	
	public int addPlayer(String playerName, String teamName) {
		if(!teamsConfig.isSet("teams." + teamName)) {
			/* Error Code 1 == No such team exists! */
			return 1;
		} else if(teamsConfig.getConfigurationSection("teams." + teamName).getList("members") == null || !teamsConfig.getConfigurationSection("teams." + teamName).getList("members").contains(playerName)) {
			
			if(teamsConfig.getConfigurationSection("teams." + teamName).getList("members") == null) {
				List<String> ls = new ArrayList<String>(Arrays.asList());
				ls.add(playerName);
				teamsConfig.getConfigurationSection("teams." + teamName).set("members", ls);
			} else {
				plugin.getPluginLogger().info("List exists!");
				@SuppressWarnings("unchecked")
				List<String> ls = (List<String>) teamsConfig.getConfigurationSection("teams." + teamName).getList("members");
				if(ls.size() == teamsConfig.getInt("teamSize")) {
					/* Error Code 3 == Team is full! */
					return 3;
				} else {
					ls.add(playerName);
					teamsConfig.getConfigurationSection("teams." + teamName).set("members", ls);
				}
			}
			/* Nominal method return code */
			return 0;
		} else {
			/* Error Code 2 == Player is already registered */
			return 2;
		}
	}
	
	//DELETE FUNCTION
	
	public boolean setMaxTeamSize(String value) {
		int valueInt;
		try {
			valueInt = Integer.parseInt(value);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		teamsConfig.set("teamSize", valueInt);
		return true;
	}

}
