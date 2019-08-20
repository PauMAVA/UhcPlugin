package me.PauMAVA.UhcPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;

public class UhcWorldConfig {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static final List<World> dimensions = plugin.getServer().getWorlds();
	
	public static void setBorder(double radius) {
		double diameter = radius * 2;
		for(World dimension: dimensions) {
			dimension.getWorldBorder().setCenter(new Location(dimension, 0, 0, 0));
			dimension.getWorldBorder().setSize(diameter);
		}
		return;
	}
	
	public static <T> void setRules(HashMap<GameRule<T>, T> rules) {
		Set<GameRule<T>> gameRuleList = rules.keySet();
		for(GameRule<T> gm: gameRuleList) {
			T value = rules.get(gm);
			for(World dimension: dimensions) {
				dimension.setGameRule(gm, value);
			}
		}
	}
	
	public static void setDifficulty(Difficulty d) {
		for(World dimension: dimensions) {
			dimension.setDifficulty(d);
		}
		return;
	} 
	
	public static void setTime(Long time) {
		for(World dimension: dimensions) {
			dimension.setTime(time);
		}
		return;
	}
	
	public static HashMap<GameRule<Boolean>, Boolean> getRules() {
		HashMap<GameRule<Boolean>, Boolean> rulesMap = new HashMap<>();
		rulesMap.put(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		rulesMap.put(GameRule.DO_DAYLIGHT_CYCLE, true);
		rulesMap.put(GameRule.DO_MOB_SPAWNING, true);
		rulesMap.put(GameRule.SHOW_DEATH_MESSAGES, false);
		rulesMap.put(GameRule.NATURAL_REGENERATION, false);
		return rulesMap;
	}
	
}
