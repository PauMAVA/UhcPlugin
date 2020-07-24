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

package me.PauMAVA.UhcPlugin.util;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.lang.PluginStrings;
import me.PauMAVA.UhcPlugin.teams.UhcTeam;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class UhcTabList extends BukkitRunnable {

    private ChatComponentText headerComp;
    private ChatComponentText footerComp;
    private String season = UhcPluginCore.getInstance().getConfig().getString("season");
    private String seasonPrefix = PluginStrings.SEASON_PREFIX.toString();
    private int i = 1;

    @Override
    public void run() {
        switch (i) {
            case 1: {
                this.headerComp = new ChatComponentText("\n" + ChatColor.YELLOW + "" + ChatColor.BOLD + "U" + ChatColor.GOLD + "" + ChatColor.BOLD + "HC " + seasonPrefix + season + "\n");
                break;
            }
            case 2: {
                this.headerComp = new ChatComponentText("\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "U" + ChatColor.YELLOW + "" + ChatColor.BOLD + "H" + ChatColor.GOLD + "" + ChatColor.BOLD + "C " + seasonPrefix + season + "\n");
                break;
            }
            case 3: {
                this.headerComp = new ChatComponentText("\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "UH" + ChatColor.YELLOW + "" + ChatColor.BOLD + "C" + ChatColor.GOLD + "" + ChatColor.BOLD + " " + seasonPrefix + season + "\n");
                break;
            }
            case 4: {
                this.headerComp = new ChatComponentText("\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "UHC" + ChatColor.YELLOW + "" + ChatColor.BOLD + " " + ChatColor.GOLD + "" + ChatColor.BOLD + seasonPrefix + season + "\n");
                break;
            }
            case 5: {
                this.headerComp = new ChatComponentText("\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "UHC " + ChatColor.YELLOW + "" + ChatColor.BOLD + seasonPrefix + ChatColor.GOLD + "" + ChatColor.BOLD + "" + season + "\n");
                break;
            }
            case 6: {
                this.headerComp = new ChatComponentText("\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "UHC " + seasonPrefix + ChatColor.YELLOW + "" + ChatColor.BOLD + season + "\n");
                break;
            }
            case 7: {
                this.headerComp = new ChatComponentText("\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "UHC " + seasonPrefix + season + "\n");
                i = -20;
                break;
            }
        }
        i++;
        Integer alivePlayers = 0;
        for(UhcTeam team: UhcPluginCore.getInstance().getMatchHandler().getRemainingTeams()) {
            alivePlayers += team.alive().size();
        }
        this.footerComp = new ChatComponentText("\n" + PluginStrings.TABLIST_ALIVE_PLAYERS.toString() + "" + alivePlayers + "\n" + ChatColor.LIGHT_PURPLE + "\n" + PluginStrings.TABLIST_COPYRIGHT.toString() + "\n");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("header");
            Field footerField = packet.getClass().getDeclaredField("footer");
            headerField.setAccessible(true);
            footerField.setAccessible(true);
            headerField.set(packet, this.headerComp);
            footerField.set(packet, this.footerComp);
            for(Player player: Bukkit.getServer().getOnlinePlayers()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void resetTab(Player player) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try{
            Field header = packet.getClass().getDeclaredField("header");
            Field footer = packet.getClass().getDeclaredField("footer");
            header.setAccessible(true);
            footer.setAccessible(true);
            header.set(packet, new ChatComponentText(""));
            footer.set(packet, new ChatComponentText(""));
        } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
