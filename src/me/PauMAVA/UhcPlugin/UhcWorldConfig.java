package me.PauMAVA.UhcPlugin;

import org.bukkit.GameRule;
import org.bukkit.World;

public class UhcWorldConfig {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static final GameRule<Boolean> naturalRegeneration = GameRule.NATURAL_REGENERATION;
	
	public static void setBorder(double radius) {
		double diameter = radius * 2;
		World overworld = plugin.getServer().getWorlds().get(0);
		World nether = plugin.getServer().getWorlds().get(1);
		World end = plugin.getServer().getWorlds().get(2);
		overworld.getWorldBorder().setCenter(0, 0);
		overworld.getWorldBorder().setSize(diameter);
		nether.getWorldBorder().setCenter(0.0, 0.0);
		nether.getWorldBorder().setSize(diameter);
		overworld.setGameRule(naturalRegeneration, false);
		nether.setGameRule(naturalRegeneration, false);
		end.setGameRule(naturalRegeneration, false);
		return;
	}
	
}
