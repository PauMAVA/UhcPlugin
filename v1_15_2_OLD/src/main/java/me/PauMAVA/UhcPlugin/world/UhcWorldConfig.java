/*
 * UhcPlugin
 * Copyright (c) 2019  Pau Machetti Vallverdu
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

package me.PauMAVA.UhcPlugin.world;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UhcWorldConfig<T> {
	
	private final UhcPluginCore plugin;
	private final List<World> dimensions;

	private HashMap<World, HashMap<GameRule<T>, T>> cachedRules;

	public void cacheCurrentRules() {
		cachedRules = new HashMap<>();
		for (World world: dimensions) {
			HashMap<GameRule<T>, T> ruleSet = new HashMap<>();
			for (String ruleName: world.getGameRules()) {
				GameRule<T> gameRule = (GameRule<T>) GameRule.getByName(ruleName);
				ruleSet.put(gameRule, world.getGameRuleValue(gameRule));
			}
			cachedRules.put(world, ruleSet);
		}
	}

	public void restoreRules() {
		for (World world: cachedRules.keySet()) {
			HashMap<GameRule<T>, T> rules = cachedRules.get(world);
			for (GameRule<T> rule: rules.keySet()) {
				world.setGameRule(rule, rules.get(rule));
			}
		}
	}

	public UhcWorldConfig(UhcPluginCore plugin) {
		this.plugin = plugin;
		this.dimensions = plugin.getServer().getWorlds();
	}

	public void setBorder(double radius) {
		double diameter = radius * 2;
		for(World dimension: dimensions) {
			dimension.getWorldBorder().setCenter(new Location(dimension, 0, 0, 0));
			dimension.getWorldBorder().setSize(diameter);
			UhcWorldBorder.startWarningTask();
		}
	}

	public void setUhcRules() {
		setRules(getRules());
	}


	public <T> void setRules(HashMap<GameRule<T>, T> rules) {
		Set<GameRule<T>> gameRuleList = rules.keySet();
		for(GameRule<T> gm: gameRuleList) {
			T value = rules.get(gm);
			for(World dimension: dimensions) {
				dimension.setGameRule(gm, value);
			}
		}
	}
	
	public void setDifficulty(Difficulty d) {
		for(World dimension: dimensions) {
			dimension.setDifficulty(d);
		}
	}
	
	public void setTime(Long time) {
		for(World dimension: dimensions) {
			dimension.setTime(time);
		}
	}
	
	public HashMap<GameRule<Boolean>, Boolean> getRules() {
		HashMap<GameRule<Boolean>, Boolean> rulesMap = new HashMap<GameRule<Boolean>, Boolean>();
		rulesMap.put(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		rulesMap.put(GameRule.DO_DAYLIGHT_CYCLE, true);
		rulesMap.put(GameRule.DO_MOB_SPAWNING, true);
		rulesMap.put(GameRule.SHOW_DEATH_MESSAGES, false);
		rulesMap.put(GameRule.NATURAL_REGENERATION, false);
		return rulesMap;
	}
	
}
