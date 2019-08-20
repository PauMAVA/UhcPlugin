package me.PauMAVA.UhcPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		List<Recipe> backupList = new ArrayList<>();
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
