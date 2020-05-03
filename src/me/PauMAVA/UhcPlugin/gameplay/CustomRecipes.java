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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.lang.PluginStrings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;


public class CustomRecipes {
	
	public CustomRecipes(Boolean instantiate) {
		if(instantiate) {
			playerHeadGoldenApple();
			removeOriginalGoldenMelon();
			newGlisteringMelon();
			darkCrystal();
			superGoldenApple();
			hyperGoldenApple();
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

	private void darkCrystal() {
		ItemStack darkCrystal = new ItemStack(Material.END_CRYSTAL, 1);
		ItemMeta meta = darkCrystal.getItemMeta();
		meta.setDisplayName(PluginStrings.DARK_CRYSTAL_DISPLAYNAME.toString());
		meta.setLore(new ArrayList<String>(Arrays.asList(PluginStrings.DARK_CRYSTAL_LORE1.toString(), PluginStrings.DARK_CRYSTAL_LORE2.toString(), PluginStrings.DARK_CRYSTAL_LORE3.toString())));
		darkCrystal.setItemMeta(meta);
		NamespacedKey name = new NamespacedKey(UhcPluginCore.getInstance(), "end_crystal");
		ShapedRecipe darkCrystalRecipe = new ShapedRecipe(name, darkCrystal);
		darkCrystalRecipe.shape("*-*", "-%-", "*-*");
		darkCrystalRecipe.setIngredient('*', Material.DIAMOND);
		darkCrystalRecipe.setIngredient('-', Material.BLAZE_POWDER);
		darkCrystalRecipe.setIngredient('%', Material.PLAYER_HEAD);
		Bukkit.getServer().addRecipe(darkCrystalRecipe);
	}

	private void superGoldenApple() {
		registerHyperSuperGoldenApples("super_golden_apple");
	}

	private void hyperGoldenApple() {
		registerHyperSuperGoldenApples("hyper_golden_apple");
	}

	private void registerHyperSuperGoldenApples(String key) {
		ItemStack sga = new ItemStack(Material.GOLDEN_APPLE, 1);
		NamespacedKey name = new NamespacedKey(UhcPluginCore.getInstance(), key);
		ShapedRecipe recipe = new ShapedRecipe(name, sga);
		recipe.shape("***", "*-*", "***");
		recipe.setIngredient('*', Material.GOLD_INGOT);
		recipe.setIngredient('-', Material.GOLDEN_APPLE);
		Bukkit.getServer().addRecipe(recipe);
	}
}
