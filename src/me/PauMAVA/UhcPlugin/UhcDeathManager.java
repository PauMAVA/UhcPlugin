package me.PauMAVA.UhcPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;


public class UhcDeathManager {
	private Player player;
	private World playerWorld;
	
	public UhcDeathManager(Player player, World world) {
		this.player = player;
		this.playerWorld = world;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public String getPlayerName() {
		return this.player.getName();
	}
	
	public World getPlayerWorld() {
		return this.playerWorld;
	}
	
	public List<Integer> getPlayerCoords() {
		Player p = this.player;
		Location location = p.getLocation();
		Integer[] arr = {location.getBlockX(), location.getBlockY(), location.getBlockZ()};
		return Arrays.asList(arr);
	}
	
	public void setPlayerGamemode(Player player, GameMode gMode) {
		player.setGameMode(gMode);
	}
	
	public void displayDeathMsgAndUpdateTeam(Player player) {
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[Death] " + ChatColor.LIGHT_PURPLE + "The player " + player.getName() + " has been eliminated!");
		String playerTeamName = UhcTeamsManager.getPlayerTeam(player.getName());
		UhcTeamsManager.getTeamsManagementFile().kickPlayer(player.getName(), playerTeamName);
		Integer integrantsLeft = UhcTeamsManager.getTeamMembers(playerTeamName).size();
		if(integrantsLeft == 0) {
			Bukkit.broadcastMessage(ChatColor.BLUE + "[Info] " + ChatColor.AQUA + player.getName() + " was the last member of the team " + playerTeamName + "!" );
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "------ " + ChatColor.RESET + ChatColor.GOLD + "The team " + playerTeamName + " has been eliminated!" + ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + " ------");
			UhcTeamsManager.getTeamsManagementFile().deleteTeam(playerTeamName);
		} else {
			Bukkit.broadcastMessage(ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The player " + player.getName() + " was part of the team " + playerTeamName + " which has " + integrantsLeft + " players left!");
		}
		player.sendMessage(ChatColor.DARK_PURPLE + "[Death] " + ChatColor.LIGHT_PURPLE + "You have died and you have been kicked from the team!. Please use the global chat to communicate with people.");
		player.sendMessage(ChatColor.GOLD + "[Developer] " + ChatColor.YELLOW + "Thank you for participating in this UHC. Hope you had a great time!");
		player.sendMessage(ChatColor.GOLD + "[Developer] " + ChatColor.YELLOW + "Thanks for using UhcPlugin by PauMava! More info at https://github.com/PauMAVA/UhcPlugin");
	}
	
	public void setTotem(List<Integer> coords, Player player, @Nullable Material material) {
		Location baseLocation = new Location(this.playerWorld, coords.get(0), coords.get(1), coords.get(2));
		Location skullLocation = new Location(this.playerWorld, coords.get(0), coords.get(1) + 1, coords.get(2));
		Block baseBlock = baseLocation.getBlock();
		baseBlock.setType(material);
		Block skullBlock = skullLocation.getBlock();
        skullBlock.setType(Material.PLAYER_HEAD);
        BlockState state = skullBlock.getState();
        Skull skull = (Skull) state;
        UUID uuid = player.getUniqueId();
        skull.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(uuid));
        skull.update();
		return;
	}
	
}
