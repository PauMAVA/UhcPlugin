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

package me.PauMAVA.UhcPlugin.chat;

import org.bukkit.ChatColor;

public enum Prefix {

    INGAME_UHC(ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "UHC" + ChatColor.GRAY + "" + ChatColor.BOLD + "] " + ChatColor.RESET),
    GLOBAL_CHAT(ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.RED + "" + ChatColor.BOLD + "GLOBAL" + ChatColor.GRAY + "" + ChatColor.BOLD + "] " + ChatColor.RESET),
    TEAM_CHAT(ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.AQUA + "" + ChatColor.BOLD + "TEAM" + ChatColor.GRAY + "" + ChatColor.BOLD + "] " + ChatColor.RESET);

    private final String value;

    Prefix(String prefix) {
        this.value = prefix;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
