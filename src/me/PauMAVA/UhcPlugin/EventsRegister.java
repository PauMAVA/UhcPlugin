package me.PauMAVA.UhcPlugin;

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
