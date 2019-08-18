package me.PauMAVA.UhcPlugin;

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
		plugin.getPluginLogger().info("DIED");
	}
	
	@EventHandler
	public void onAdvancement(PlayerAdvancementDoneEvent event) {
		Advancement advancement = event.getAdvancement();
		String advancementName = advancement.getKey().toString();
		if(advancementName.contains("story")) {
			//TODO Take Advancement name from a database key-value?
			//TODO Make the name be a random string
			UhcChatManager.dispatchAdvancementEvent(advancementName);
		}
	}
	
}
