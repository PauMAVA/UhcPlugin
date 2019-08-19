/* This is a ReadOnly database, so no setters required! */
package me.PauMAVA.UhcPlugin;

import java.util.HashMap;
import java.util.Map;

public class AdvancementsDatabase {
	
	private final Map<String, String> advancements = new HashMap<String, String>() {
		{
			put("minecraft:story/root", "Minecraft");
			put("minecraft:story/mine_stone", "Stone Age");
			put("minecraft:story/upgrade_tools", "Getting an Upgrade"); 
			put("minecraft:story/smelt_iron", "Acquire Hardware");
			put("minecraft:story/obtain_armor", "Suit Up");
			put("minecraft:story/lava_bucket", "Hot Stuff");
			put("minecraft:story/iron_tools", "Isn't It Iron Pick"); 
			put("minecraft:story/deflect_arrow", "Not Today, Thank You"); 
			put("minecraft:story/form_obsidian", "Ice Bucket Challenge");
			put("minecraft:story/mine_diamond", "Diamonds!");
			put("minecraft:story/enter_the_nether", "We Need to Go Deeper"); 
			put("minecraft:story/shiny_gear", "Cover Me With Diamonds");
			put("minecraft:story/enchant_item", "Enchanter");
			put("minecraft:story/cure_zombie_villager", "Zombie Doctor"); 
			put("minecraft:story/follow_ender_eye", "Eye Spy");
			put("minecraft:story/enter_the_end", "The End?");
			put("minecraft:nether/root", "Nether");
			put("minecraft:nether/return_to_sender", "Return to Sender");
			put("minecraft:nether/fast_travel", "Subspace Bubble");
			put("minecraft:nether/find_fortress", "A Terrible Fortress");
			put("minecraft:nether/uneasy_alliance", "Uneasy Alliance");
			put("minecraft:nether/get_wither_skull", "Spooky Scary Skeleton");
			put("minecraft:nether/obtain_blaze_rod", "Into Fire");
			put("minecraft:nether/summon_wither", "Withering Heights");
			put("minecraft:nether/brew_potion", "Local Brewery");
			put("minecraft:nether/create_beacon", "Bring Home the Beacon");
			put("minecraft:nether/all_potions", "A Furious Cocktail");
			put("minecraft:nether/create_full_beacon", "Beaconator");
			put("minecraft:nether/all_effects", "How Did We Get Here?");
		}
	};
	
	public AdvancementsDatabase() {
		
	}
	
	public String getCanonicalName(String internalID) {
		if(!this.advancements.containsKey(internalID)) {
			return "Unknown";
		}
		return this.advancements.get(internalID);
	}
	
}
