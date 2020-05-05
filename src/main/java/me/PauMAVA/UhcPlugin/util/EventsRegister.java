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

package me.PauMAVA.UhcPlugin.util;

import com.mojang.datafixers.kinds.IdF;
import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.chat.UhcChatManager;
import me.PauMAVA.UhcPlugin.commands.UhcConfigCmd;
import me.PauMAVA.UhcPlugin.match.UhcDeathManager;
import me.PauMAVA.UhcPlugin.match.UhcScoreboardManager;
import me.PauMAVA.UhcPlugin.teams.UhcTeam;
import me.PauMAVA.UhcPlugin.teams.UhcTeamsManager;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EventsRegister implements Listener {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	private FileConfiguration configuration = UhcConfigCmd.getConfig();
	
	/*Listens for players joining the server and:
	 * - Injects them to the pipeline*/
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PacketIntercepter.injectPlayer(player);
	}
	
	/*Listens for players quitting the game and:
	 * - Removes them from the pipeline*/
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PacketIntercepter.rmPlayer(player);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(plugin.getMatchHandler().getMatchStatus()) {
			UhcDeathManager death = new UhcDeathManager(event.getEntity(), event.getEntity().getWorld(), event.getDeathMessage());
			death.setTotem(Material.BLACK_STAINED_GLASS_PANE);
			death.setPlayerGamemode(GameMode.SPECTATOR);
			death.displayDeathMsgAndUpdateTeam();
		}
	}
	
	@EventHandler
	public void onAdvancement(PlayerAdvancementDoneEvent event) {
		if(plugin.getMatchHandler().getMatchStatus()) {
			Advancement advancement = event.getAdvancement();
			String advancementID = advancement.getKey().toString();
			if(advancementID.contains("story") || advancementID.contains("nether") || advancementID.contains("husbandry") || advancementID.contains("end")|| advancementID.contains("adventure")) {
				AdvancementsDatabase db = new AdvancementsDatabase();
				String advancementName = db.getCanonicalName(advancementID);
				UhcChatManager.dispatchAdvancementEvent(advancementName);
			}
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player && plugin.getMatchHandler().getMatchStatus()) {
			UhcScoreboardManager.updateHealth();
		}
	}

	@EventHandler
	public void onPlayerHeal(EntityRegainHealthEvent event) {
		if(event.getEntity() instanceof Player && plugin.getMatchHandler().getMatchStatus()) {
			UhcScoreboardManager.updateHealth();
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getMaterial() == Material.END_CRYSTAL && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.BEDROCK && plugin.getMatchHandler().getMatchStatus()) {
			event.getItem().setType(Material.COAL);
			UhcTeamsManager.revive(event.getPlayer(), event.getClickedBlock());
		}
	}

	@EventHandler
	public void onEntityKill(EntityDeathEvent event) {
		if(event.getEntity() instanceof Drowned && event.getEntity().getKiller() != null) {
			customizeDrownedDrop(event);
		} else if (event.getEntity() instanceof Ghast && event.getEntity().getKiller() != null && configuration.getBoolean("custom_ghast_drops.enabled")) {
			customizeGhastDrop(event);
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(plugin.getMatchHandler().getMatchStatus() && event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerCraft(PrepareItemCraftEvent event) {
		if (event.getRecipe() != null) {
			checkGoldenApplesRecipes(event);
		}
	}

	@EventHandler
	public void playerEatEvent(PlayerItemConsumeEvent event) {
		if (event.getItem().getType() == Material.GOLDEN_APPLE) {
			onGoldenAppleConsume(event);
		}
	}

	private void customizeDrownedDrop(EntityDeathEvent event) {
		Integer randomNum = new Range(0 , 100).getRandomInteger();
		if(randomNum > 50) {
			ItemStack item = new ItemStack(Material.TRIDENT, 1);
			Damageable meta = (Damageable) item.getItemMeta();
			meta.setDamage(new Range(100, 250).getRandomInteger());
			item.setItemMeta((ItemMeta) meta);
			event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), item);
		}
	}

	private void customizeGhastDrop(EntityDeathEvent event) {
		World world = event.getEntity().getWorld();
		ItemStack item = null;
		try {
			item = new ItemStack(Material.valueOf(configuration.getString("custom_ghast_drops.item").toUpperCase()), configuration.getInt("custom_ghast_drops.amount"));
			world.dropItem(event.getEntity().getLocation(), item);
		} catch (IllegalArgumentException ignored) {}
		if (event.getDrops().size() > 0 && item != null) {
			List<ItemStack> copy = new ArrayList<>();
			copy.addAll(event.getDrops());
			for (ItemStack i: copy) {
				event.getDrops().remove(i);
			}
		}
	}

	private void checkGoldenApplesRecipes(PrepareItemCraftEvent event) {
		ItemStack resultItem = event.getRecipe().getResult();
		if (resultItem.getType() == Material.GOLDEN_APPLE) {
			ItemMeta resultMeta = resultItem.getItemMeta();
			ItemStack centralItem = event.getInventory().getMatrix()[4];
			if (centralItem.getType() == Material.GOLDEN_APPLE) {
				if (centralItem.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Super Golden Apple")) {
					resultMeta.setDisplayName(ChatColor.DARK_PURPLE + "Hyper Golden Apple");
				} else if (centralItem.getItemMeta().getDisplayName().equalsIgnoreCase(resultMeta.getDisplayName())) {
					resultMeta.setDisplayName(ChatColor.GOLD + "Super Golden Apple");
				}
			}
			ItemStack finalItem = new ItemStack(Material.GOLDEN_APPLE, 1);
			finalItem.setItemMeta(resultMeta);
			event.getInventory().setResult(finalItem);
		}
	}

	private void onGoldenAppleConsume(PlayerItemConsumeEvent event) {
		String displayName = event.getItem().getItemMeta().getDisplayName();
		Player player = event.getPlayer();
		if (displayName.equalsIgnoreCase(ChatColor.GOLD + "Super Golden Apple")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
			player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 0));
		} else if(displayName.equalsIgnoreCase(ChatColor.DARK_PURPLE + "Hyper Golden Apple") && player.getHealthScale() < 20 * 10) {
			player.setHealthScale(player.getHealthScale() + configuration.getInt("hga.containers") * 2);
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getHealthScale());
		}
		player.setFoodLevel(player.getFoodLevel() + Math.min(20 - player.getFoodLevel(), 4));
	}
	
}
