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

package me.PauMAVA.UhcPlugin.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.teams.UhcTeamsManager;
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
	private String dCause;
	
	public UhcDeathManager(Player player, World world, String dCause) {
		this.player = player;
		this.playerWorld = world;
		this.dCause = dCause;
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
	
	public String getDeathCause() {
		return this.dCause;
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
		/* If the player already died do nothing! */
		if(UhcTeamsManager.getPlayerTeam(getPlayerName()) == null) {
			return;
		}
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[Death] " + ChatColor.LIGHT_PURPLE + "The player " + this.dCause);
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[Death] " + ChatColor.LIGHT_PURPLE + "The player " + player.getName() + " has been eliminated!");
		String playerTeamName = UhcTeamsManager.getPlayerTeam(player.getName());
		UhcTeamsManager.getTeamsManagementFile().kickPlayer(player.getName(), playerTeamName);
		UhcTeamsManager.getTeamsManagementFile().saveConfig();
		Integer integrantsLeft = UhcTeamsManager.getTeamMembers(playerTeamName).size();
		if(integrantsLeft == 0) {
			Bukkit.broadcastMessage(ChatColor.BLUE + "[Info] " + ChatColor.AQUA + player.getName() + " was the last member of the team " + playerTeamName + "!" );
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "------ " + ChatColor.RESET + ChatColor.GOLD + "The team " + playerTeamName + " has been eliminated!" + ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + " ------");
			UhcTeamsManager.getTeamsManagementFile().deleteTeam(playerTeamName);
			UhcTeamsManager.getTeamsManagementFile().saveConfig();
			if(UhcTeamsManager.getTeamsManagementFile().getTeams().size() == 1) {
				/* A TEAM HAS WON */
				List<String> team = new ArrayList<String>(UhcTeamsManager.getTeamsManagementFile().getTeams());
				player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + team.get(0),ChatColor.AQUA + "" + ChatColor.BOLD +  "WINS", 0, 5*20, 1*20);
				UhcPluginCore.getInstance().setMatchStatus(false);
			}
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
