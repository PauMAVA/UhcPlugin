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

package me.PauMAVA.UhcPlugin.gameplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.teams.TeamsFile;
import me.PauMAVA.UhcPlugin.teams.UhcTeamsManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.PauMAVA.UhcPlugin.util.Range;

public class RandomTeleporter {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static final TeamsFile teamsConfig = UhcTeamsManager.getTeamsManagementFile();
	private static final Integer borderRadius = plugin.getConfig().getInt("border_radius");
	private static final Integer spawnableRadius = borderRadius - 20;
	
	/* Side number is an arbitrary assignement: 
	 * - 1 --> North
	 * - 2 --> East
	 * - 3 --> South
	 * - 4 --> West */
	
	public static void teleportPlayers() {
		for(Player player: Bukkit.getServer().getOnlinePlayers()) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1000, 1));
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000, 1));
		}
		HashMap<String, Integer> teamSideRelation = assignSides(teamsConfig.getTeams());
		HashMap<String, Integer[]> teamCoordsRelation = new HashMap<String, Integer[]>();
		for(String teamName: teamSideRelation.keySet()) {
			Integer side = teamSideRelation.get(teamName);
			Integer[] coords = generateCoordinates(side, teamCoordsRelation.values());
			Bukkit.getServer().getConsoleSender().sendMessage("Coords for team " + teamName);
			for(Integer integer: coords) {
				Bukkit.getServer().getConsoleSender().sendMessage("" + integer.intValue());
			}
			teamCoordsRelation.put(teamName, coords);
			teleportAndSetup(teamName, teamCoordsRelation.get(teamName));
		}
	}
	
	private static HashMap<String, Integer> assignSides(Collection<String> teams) {
		HashMap<String, Integer> teamSideRelation = new HashMap<String, Integer>();
		List<Integer> sidesList = Arrays.asList(1,2,3,4);
		List<String> teamsList = new ArrayList<String>(teams);
		Collections.shuffle(teamsList);
		int teamsPerSide = teams.size() / 4;
		int remanentTeams = teams.size() - (teamsPerSide * 4);
		/* Handle nonremanent teams */
		for(int i = teamsPerSide; i > 0; i--) {
			List<String> tempList = new ArrayList<String>();
			Collections.shuffle(sidesList);
			int iterator = 0;
			for(Integer value: sidesList) {
				teamSideRelation.put(teamsList.get(iterator), value);
				tempList.add(teamsList.get(iterator));
				iterator++;
			}
			teamsList.removeAll(tempList);
			tempList.clear();
		}
		/* Handle remaining unassigned teams */
		if(remanentTeams != 0) {
			sidesList = sidesList.subList(0, remanentTeams);
			for(int i = remanentTeams; i > 0; i--) {
				Collections.shuffle(sidesList);
				int iterator = 0;
				for(Integer value: sidesList) {
					teamSideRelation.put(teamsList.get(iterator), value);
					iterator++;
				}
			}
		}
		return teamSideRelation;
	}
	
	private static Range getSpawnRange() {
		return new Range(-spawnableRadius, spawnableRadius);
	}
	
	private static Integer[] generateCoordinates(Integer side, Collection<Integer[]> otherCoords) {
		Range spawnRange = getSpawnRange();
		Integer[] arr = new Integer[2];
		do {	
			switch(side.intValue()) {
				case 1: {
					/* North side --> Fixed Y */
					arr[0] = spawnRange.getRandomInteger();
					arr[1] = spawnableRadius;
					break;
				}
				case 2: {
					/* East side --> Fixed X */
					arr[0] = spawnableRadius;
					arr[1] = spawnRange.getRandomInteger();
					break;
				}
				case 3: {
					/* South side --> Fixed -Y */
					arr[0] = spawnRange.getRandomInteger();
					arr[1] = -spawnableRadius;
					break;
				}
				case 4: {
					/* West side --> Fixed -X */
					arr[0] = -spawnableRadius;
					arr[1] = spawnRange.getRandomInteger();
					break;
				}
			}
		} while(!checkForValidCoords(arr, otherCoords));
		return arr;
	}
	
	private static boolean checkForValidCoords(Integer[] arr, Collection<Integer[]> otherCoords) {
		/* Players must be at least at 800 blocks of distance */
		Integer newX = arr[0];
		Integer newY = arr[1];
		if(otherCoords.isEmpty()) {
			return true;
		}
		for(Integer[] existingCoords: otherCoords) {
			Integer existingX = existingCoords[0];
			Integer existingY = existingCoords[0];
			double vectorX = existingX.doubleValue() - newX.doubleValue();
			double vectorY = existingY.doubleValue() - newY.doubleValue();
			Integer distance = (int) Math.sqrt(Math.pow(vectorX, 2.0) + Math.pow(vectorY, 2.0));
			if(distance < 800) {
				return false;
			}
		}
		return true;
	}

	/* TODO Alterative to Bukkit.getServer().getPlayer(String name) */
	@SuppressWarnings("deprecation")
	private static void teleportAndSetup(String teamName, Integer[] coords) {
		List<String> teamMembers = teamsConfig.getTeamMembers(teamName);
		if(teamMembers.isEmpty()) {
			return;
		}
		for(String playerName: teamMembers) {
			Player p = Bukkit.getServer().getPlayer(playerName);
			if(p != null) {
				p.setHealth(20);
				p.setFoodLevel(20);
				p.getInventory().clear();
				p.setGameMode(GameMode.SURVIVAL);
				p.teleport(new Location(p.getWorld(),coords[0], 120,coords[1]));
			}
		}
		return;
	}
}
