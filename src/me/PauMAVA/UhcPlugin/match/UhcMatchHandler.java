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

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.chat.Prefix;
import me.PauMAVA.UhcPlugin.commands.UhcConfigCmd;
import me.PauMAVA.UhcPlugin.world.UhcWorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UhcMatchHandler {

    private UhcPluginCore plugin;
    private Boolean isRunning = false;
    private Integer timerTaskID;
    private List<Player> matchPlayers = new ArrayList<>();

    /* UhcMatchHandler constructor
    * @param plugin - the instance of the plugin core */
    public UhcMatchHandler(UhcPluginCore plugin) {
        this.plugin = plugin;
    }

    public void start() {
        this.isRunning = true;
        UhcWorldConfig.setBorder(plugin.getConfig().getDouble("border_radius"));
        UhcWorldConfig.setRules(UhcWorldConfig.getRules());
        UhcWorldConfig.setDifficulty(UhcConfigCmd.getDifficultyObject());
        UhcWorldConfig.setTime(0L);
        RandomTeleporter.teleportPlayers();
        UhcScoreboardManager.setUp();
        this.timerTaskID = new UhcMatchTimer().runTaskTimer(plugin, 0L, 20L).getTaskId();
    }

    public void end() {
        this.isRunning = false;
        Bukkit.getScheduler().cancelTask(this.timerTaskID);
    }

    /* Adds a player to the match
    * @param player - the player to be added to the match
    * @return the actual player list after addition */
    public List<Player> addPlayer(Player player) {
        this.matchPlayers.add(player);
        return this.matchPlayers;
    }

    /* Removes a player form the match
    * @param player - the player to be removed from the match
    * @return the actual player list after remove */
    public List<Player> removePlayer(Player player) {
        this.matchPlayers.remove(player);
        return this.matchPlayers;
    }

    /* Gets the players alive in the match
    * @return the actual list of players */
    public List<Player> getMatchPlayers() {
        return this.matchPlayers;
    }

    /* Prints the episode change announcement on the chat */
    public void episodeAnnouncement(Integer episode) {
        Bukkit.getServer().broadcastMessage(Prefix.INGAME_UHC + "" + ChatColor.GRAY + "Episode " + episode + " begins now!");
    }

    public Boolean getMatchStatus() {
        return this.isRunning;
    }

    public void setMatchStatus(Boolean matchStatus) {
        this.isRunning = matchStatus;
    }

}
