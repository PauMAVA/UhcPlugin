package me.PauMAVA.UhcPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsRegister implements Listener {
	
	/*Listens for players joining the server and:
	 * - Injects them to the pipeline*/
	@EventHandler(priority=EventPriority.HIGH)
	public void onjoin(PlayerJoinEvent event) {
		UhcPluginCore.UhcLogger.info("Registered event!");
		Player player = event.getPlayer();
		PacketIntercepter.injectPlayer(player);
	}
	
	/*Listens for players quitting the game and:
	 * - Removes them of the pipeline*/
	@EventHandler(priority=EventPriority.HIGH)
	public void onleave(PlayerQuitEvent event) {
		UhcPluginCore.UhcLogger.info("Registered event!");
		Player player = event.getPlayer();
		PacketIntercepter.rmPlayer(player);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		//NOT WORKING
		UhcPluginCore.UhcLogger.info("DIED");
	}
	
}
