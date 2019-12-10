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

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.chat.UhcChatManager;
import me.PauMAVA.UhcPlugin.gameplay.UhcDeathManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsRegister implements Listener {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	
	/*Listens for players joining the server and:
	 * - Injects them to the pipeline*/
	@EventHandler(priority=EventPriority.HIGH)
	public void onjoin(PlayerJoinEvent event) {
		plugin.getPluginLogger().info("Registered event!");
		Player player = event.getPlayer();
		PacketIntercepter.injectPlayer(player);
	}
	
	/*Listens for players quitting the game and:
	 * - Removes them from the pipeline*/
	@EventHandler(priority=EventPriority.HIGH)
	public void onleave(PlayerQuitEvent event) {
		plugin.getPluginLogger().info("Registered event!");
		Player player = event.getPlayer();
		PacketIntercepter.rmPlayer(player);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
			UhcDeathManager dManager = new UhcDeathManager(event.getEntity(), event.getEntity().getWorld(), event.getDeathMessage());
			dManager.setTotem(dManager.getPlayerCoords(), dManager.getPlayer(), Material.BLACK_STAINED_GLASS_PANE);
			dManager.setPlayerGamemode(event.getEntity(), GameMode.SPECTATOR);
			dManager.displayDeathMsgAndUpdateTeam(event.getEntity());
	}
	
	@EventHandler
	public void onAdvancement(PlayerAdvancementDoneEvent event) {
		Advancement advancement = event.getAdvancement();
		String advancementID = advancement.getKey().toString();
		if(advancementID.contains("story") || advancementID.contains("nether") || advancementID.contains("husbandry") || advancementID.contains("end")|| advancementID.contains("adventure")) {
			AdvancementsDatabase db = new AdvancementsDatabase();
			String advancementName = db.getCanonicalName(advancementID);
			UhcChatManager.dispatchAdvancementEvent(advancementName);
		}
	}
	
}
