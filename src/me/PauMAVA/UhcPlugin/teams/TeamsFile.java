/*
 * UhcPlugin
 * Copyright (c) 2019  Pau Machetti Vallverdú
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.PauMAVA.UhcPlugin.teams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class TeamsFile {
	
	private static UhcPluginCore plugin = UhcPluginCore.getInstance();
	private File file;
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
	
	public File getFile() {
		return this.file;
	}
	
	private static void loadDefaults() {
		teamsConfig.addDefault("solo", false);
		teamsConfig.options().copyDefaults(true);
		teamsConfig.createSection("teams");
		plugin.getPluginLogger().info("Added defaults!");
	}
	
	public void saveConfig() {
		try {
			teamsConfig.save(file);
		} catch (IOException e) {
			plugin.getPluginLogger().severe("Couldn't save config file! IOException.");
			e.printStackTrace();
			return;
		}
		plugin.getPluginLogger().info("Saved TeamsConfig!");
	}
	
	public boolean registerTeam(String teamName) {
		if(!teamsConfig.isSet("teams." + teamName)) {
			teamsConfig.createSection("teams." + teamName).set("members", null);
			teamsConfig.getConfigurationSection("teams." + teamName).set("teamSize", 3);
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
				List<String> ls = teamsConfig.getConfigurationSection("teams." + teamName).getStringList("members");
				if(ls.size() == teamsConfig.getConfigurationSection("teams." + teamName).getInt("teamSize")) {
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
	
	public int kickPlayer(String playerName, String teamName) {
		if(!teamsConfig.isSet("teams." + teamName)) {
			/* Error Code 1 == No such team exists! */ 
			return 1;
		} else {
			ConfigurationSection section = teamsConfig.getConfigurationSection("teams." + teamName);
			if(section.getStringList("members") == null || !section.getStringList("members").contains(playerName)) {
				/* The player is not registered on that team */
				return 2;
			}
			List<String> ls = section.getStringList("members");
			ls.remove(playerName);
			section.set("members", ls);
			/* Nominal method return code */
			return 0;
		}

	}
	
	public boolean checkTeamExistance(String teamName) {
		return teamsConfig.isSet("teams." + teamName);
	}
	
	public List<String> getTeamMembers(String teamName) {
		if(!teamsConfig.isSet("teams." + teamName) || teamsConfig.getConfigurationSection("teams." + teamName).getString("members") == null) {
			return new ArrayList<String>(); /* If the team is empty return an empty List instead of null to avoid NullPointerException */
		} else {
			return teamsConfig.getConfigurationSection("teams." + teamName).getStringList("members"); 
		}
	}
	
	public Collection<String> getTeams() {
		if(teamsConfig.getConfigurationSection("teams").getKeys(false) == null) {
			return new ArrayList<String>();
		} else {
			return teamsConfig.getConfigurationSection("teams").getKeys(false);
		}
	}
	
	public int getTeamMaxSize(String teamName) {
		if(teamsConfig.getConfigurationSection("teams." + teamName) == null) {
			/* Then the team doesn't exist. Return negative integer */
			return -1;
		}
		return teamsConfig.getConfigurationSection("teams." + teamName).getInt("teamSize");
	}
	
	public List<String> settleNewSize(String teamName, int excedent) {
		List<String> originalList = getTeamMembers(teamName); 
		int teamSize = originalList.size();
		Integer newTeamSize = teamSize - excedent;
		Collections.reverse(originalList);
		List<String> removedList = originalList.subList(0, excedent);
		for(String player: removedList) {
			kickPlayer(player, teamName);
		}
		setMaxTeamSize(teamName, newTeamSize.toString());
		return removedList;
	}
	
	public boolean setMaxTeamSize(String teamName, String value) {
		int valueInt;
		try {
			valueInt = Integer.parseInt(value);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		teamsConfig.getConfigurationSection("teams." + teamName).set("teamSize", valueInt);
		return true;
	}

}
