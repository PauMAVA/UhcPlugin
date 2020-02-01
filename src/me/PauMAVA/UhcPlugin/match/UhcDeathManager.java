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

package me.PauMAVA.UhcPlugin.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.chat.Prefix;
import me.PauMAVA.UhcPlugin.lang.PluginStrings;
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
	
	public void setPlayerGamemode(GameMode gMode) {
		player.setGameMode(gMode);
	}
	
	public void displayDeathMsgAndUpdateTeam() {
		/* If the player already died do nothing! */
		if(UhcTeamsManager.getPlayerTeam(getPlayerName()) == null) {
			return;
		}
		Bukkit.getServer().broadcastMessage(Prefix.INGAME_UHC + "" + PluginStrings.DEATH_HEADER.toString() + this.dCause);
		Bukkit.getServer().broadcastMessage(Prefix.INGAME_UHC + "" + PluginStrings.DEATH_HEADER.toString() + player.getName() + PluginStrings.DEATH_ELIMINATION.toString());
		UhcTeamsManager.eliminate(player);
		player.sendMessage(Prefix.INGAME_UHC + "" + PluginStrings.DEATH_INFO.toString());
		player.sendMessage(Prefix.DEVELOPER + "" + PluginStrings.DEATH_THANKS.toString());
		player.sendMessage(Prefix.DEVELOPER + "" + PluginStrings.DEATH_GITHUB.toString());
	}
	
	public void setTotem(@Nullable Material material) {
		List<Integer> coords = getPlayerCoords();
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
	}
	
}
