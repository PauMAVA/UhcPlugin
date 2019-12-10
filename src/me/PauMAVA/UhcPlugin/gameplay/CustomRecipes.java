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

package me.PauMAVA.UhcPlugin.gameplay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;


public class CustomRecipes {
	
	public CustomRecipes(Boolean instantiate) {
		if(instantiate) {
			playerHeadGoldenApple();
			removeOriginalGoldenMelon();
			newGlisteringMelon();
		}
	}
	
	private void playerHeadGoldenApple() {
		ItemStack golden_apple = new ItemStack(Material.GOLDEN_APPLE, 1);
		NamespacedKey name = new NamespacedKey(UhcPluginCore.getInstance(), "golden_apple");
		ShapedRecipe goldenApple = new ShapedRecipe(name, golden_apple);
		goldenApple.shape(" * ","*%*"," * ");	
		goldenApple.setIngredient('*', Material.GOLD_INGOT);
		goldenApple.setIngredient('%', Material.PLAYER_HEAD);
		Bukkit.getServer().addRecipe(goldenApple);
		return;
	}
	
	private void newGlisteringMelon() {
		ItemStack glistering_melon = new ItemStack(Material.GLISTERING_MELON_SLICE, 1);
		NamespacedKey name = new NamespacedKey(UhcPluginCore.getInstance(), "glistering_melon");
		ShapelessRecipe glisteringMelon = new ShapelessRecipe(name, glistering_melon);
		glisteringMelon.addIngredient(Material.GOLD_BLOCK);
		glisteringMelon.addIngredient(Material.MELON_SLICE);
		Bukkit.getServer().addRecipe(glisteringMelon);
	}
	
	private void removeOriginalGoldenMelon() {
		Iterator<Recipe> recipes = Bukkit.recipeIterator();
		List<Recipe> backupList = new ArrayList<Recipe>();
		while(recipes.hasNext()) {
			Recipe nextRecipe = recipes.next();
			if(!(nextRecipe.getResult().getType().equals(Material.GLISTERING_MELON_SLICE))) {
				backupList.add(nextRecipe);
			}	
		}
		Bukkit.getServer().clearRecipes();
		for(Recipe r: backupList) {
			Bukkit.getServer().addRecipe(r);
		}
	}
}
