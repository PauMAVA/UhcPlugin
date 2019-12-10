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

package me.PauMAVA.UhcPlugin.util;

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
			put("minecraft:adventure/adventuring_time", "Adventuring Time");
			put("minecraft:adventure/kill_a_mob", "Monster Hunter");
			put("minecraft:adventure/kill_all_mobs", "Monsters Hunted");
			put("minecraft:adventure/root", "Adventure Advancement");
			put("minecraft:adventure/shoot_arrow", "Take Aim");
			put("minecraft:adventure/sleep_in_bed", "Sweet dreams");
			put("minecraft:adventure/sniper_duel", "Sniper duel");
			put("minecraft:adventure/summon_iron_golem", "Hired Help");
			put("minecraft:adventure/throw_trident", "A Throwaway Joke"); 	
			put("minecraft:adventure/totem_of_undying", "Postmortal"); 
			put("minecraft:adventure/trade", "What a Deal"); 	
			put("minecraft:adventure/very_very_frightening", "Very Very Frightening"); 	
			put("minecraft:end/dragon_breath", "You Need a Mint"); 	
			put("minecraft:end/dragon_egg", "The Next Generation"); 	
			put("minecraft:end/elytra", "Sky\'s the Limit"); 	
			put("minecraft:end/enter_end_gateway", "Remote Getaway"); 	
			put("minecraft:end/find_end_city", "End Advancement"); 	
			put("minecraft:end/kill_dragon", "Free the End"); 	
			put("minecraft:end/levitate", "Great View From Up Here"); 	
			put("minecraft:end/respawn_dragon", "The End... Again..."); 	
			put("minecraft:end/root", "End Advancement"); 	
			put("minecraft:husbandry/balanced_diet", "A Balanced Diet"); 	
			put("minecraft:husbandry/break_diamond_hoe", "Serious Dedication"); 	
			put("minecraft:husbandry/bred_all_animals", "Two by Two"); 	
			put("minecraft:husbandry/breed_an_animal", "Husbandry Advancement"); 	
			put("minecraft:husbandry/plant_seed", "A Seedy Place"); 	
			put("minecraft:husbandry/root Husbandry", "Advancement");	
			put("minecraft:husbandry/tactical_fishing", "Tactical Fishing"); 	
			put("minecraft:husbandry/tame_an_animal", "Best Friends Forever");	
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
