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

package me.PauMAVA.UhcPlugin.teams;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class UhcTeam {

    private String name;
    private List<Player> originalPlayers;
    private HashMap<Player, Boolean> alivePlayers = new HashMap<Player, Boolean>();

    public UhcTeam(String name, List<Player> players) {
        this.name = name;
        this.originalPlayers = players;
        for(Player player: this.originalPlayers) {
            this.alivePlayers.put(player, true);
        }
    }

    public List<Player> integrants() {
        return this.originalPlayers;
    }

    public Boolean isAlive(Player player) {
        return this.alivePlayers.get(player);
    }

    public Boolean isInTeam(Player player) {
        return this.originalPlayers.contains(player);
    }

    public void markPlayerAsDead(Player player) {
        if(isInTeam(player)) {
            this.alivePlayers.put(player, false);
        }
    }

    public void markPlayerAsAlive(Player player) {
        if(isInTeam(player)) {
            this.alivePlayers.put(player, true);
        }
    }

}
