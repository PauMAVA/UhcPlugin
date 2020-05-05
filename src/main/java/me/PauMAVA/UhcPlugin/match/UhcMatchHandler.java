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
import me.PauMAVA.UhcPlugin.gameplay.SkinChanger;
import me.PauMAVA.UhcPlugin.lang.PluginStrings;
import me.PauMAVA.UhcPlugin.teams.UhcTeam;
import me.PauMAVA.UhcPlugin.teams.UhcTeamsManager;
import me.PauMAVA.UhcPlugin.util.UhcTabList;
import me.PauMAVA.UhcPlugin.world.UhcWorldBorder;
import me.PauMAVA.UhcPlugin.world.UhcWorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UhcMatchHandler {

    private UhcPluginCore plugin;
    private Boolean isRunning = false;
    private Integer timerTaskID;
    private Integer tabTaskID;
    private List<Player> matchPlayers = new ArrayList<>();
    private List<UhcTeam> teams = new ArrayList<UhcTeam>();
    private UhcMatchTimer timer;

    private SkinChanger skinChanger;

    /**
    * UhcMatchHandler constructor
    * @param plugin - the instance of the plugin core */
    public UhcMatchHandler(UhcPluginCore plugin) {
        this.plugin = plugin;
        this.skinChanger = new SkinChanger();
    }

    public void start() {
        this.isRunning = true;
        skinChanger.cachePlayerInfo();
        for(String teamName: UhcTeamsManager.getTeamsManagementFile().getTeams()) {
            teams.add(UhcTeamsManager.getTeamObject(teamName));
        }
        UhcWorldConfig.setBorder(plugin.getConfig().getDouble("border_radius"));
        UhcWorldConfig.setRules(UhcWorldConfig.getRules());
        UhcWorldConfig.setDifficulty(UhcConfigCmd.getDifficultyObject());
        UhcWorldConfig.setTime(0L);
        RandomTeleporter.teleportPlayers();
        UhcScoreboardManager.setUp();
        this.timer = new UhcMatchTimer(this);
        if (UhcPluginCore.getInstance().getConfig().getBoolean("rotate_skins.enabled")) {
            this.timer.scheduleAsyncSkinRoll();
        }
        this.timerTaskID = timer.runTaskTimer(plugin, 0L, 20L).getTaskId();
        this.tabTaskID = new UhcTabList().runTaskTimer(plugin, 0L, 20L).getTaskId();
    }

    public void end() {
        this.isRunning = false;
        Bukkit.getScheduler().cancelTask(this.timerTaskID);
        Bukkit.getScheduler().cancelTask(this.tabTaskID);
        UhcWorldBorder.stopWarningTask();
    }

    /**
    * Adds a player to the match
    * @param player - the player to be added to the match
    * @return the actual player list after addition */
    public List<Player> addPlayer(Player player) {
        this.matchPlayers.add(player);
        return this.matchPlayers;
    }

    /**
    * Removes a player form the match
    * @param player - the player to be removed from the match
    * @return the actual player list after remove */
    public List<Player> removePlayer(Player player) {
        this.matchPlayers.remove(player);
        return this.matchPlayers;
    }

    /**
    * Gets the players alive in the match
    * @return the actual list of players */
    public List<Player> getMatchPlayers() {
        return this.matchPlayers;
    }

    /* Prints the episode change announcement on the chat */
    public void episodeAnnouncement(Integer episode) {
        Bukkit.getServer().broadcastMessage(Prefix.INGAME_UHC + "" + PluginStrings.EPISODE_START1.toString() + episode + PluginStrings.EPISODE_START2.toString());
    }

    public Boolean getMatchStatus() {
        return this.isRunning;
    }

    public void setMatchStatus(Boolean matchStatus) {
        this.isRunning = matchStatus;
    }

    public UhcTeam getUhcTeam(Player player) {
        for(UhcTeam team: this.teams) {
            if(team.isInTeam(player.getName())) {
                return team;
            }
        }
        return new UhcTeam("", new ArrayList<>());
    }

    public int remainingTeams() {
        int i = 0;
        for(UhcTeam team: teams) {
            if(!team.isEliminated()) {
                i++;
            }
        }
        return i;
    }

    public List<UhcTeam> getRemainingTeams() {
        List<UhcTeam> teams = new ArrayList<UhcTeam>();
        for(UhcTeam team: this.teams) {
            teams.add(team);
        }
        return teams;
    }

    public UhcMatchTimer getTimer() {
        return this.timer;
    }

    public SkinChanger getSkinChanger() {
        return skinChanger;
    }

    void giveItemToAllPlayers(ItemStack item) {
        for (UhcTeam team: getRemainingTeams()) {
            for (Player p: team.alive()) {
                Inventory inventory = p.getInventory();
                HashMap<Integer, ItemStack> result = inventory.addItem(item);
                for (Integer i: result.keySet()) {
                    if (result.get(i).equals(item)) {
                        p.getWorld().dropItem(p.getLocation(), item);
                    }
                }
            }
        }
    }

}
