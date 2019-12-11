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

package me.PauMAVA.UhcPlugin.gameplay;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.PauMAVA.UhcPlugin.util.PlayerProfileBuilder;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SkinChanger implements CommandExecutor {

    private static void changeSkin(CraftPlayer cPlayer, String name) {
        GameProfile gProfile = cPlayer.getProfile();
        /* Build game profile */
        PlayerProfileBuilder builder = new PlayerProfileBuilder();

        builder.getPlayerProfile(cPlayer.getPlayer().getName());
        Collection<Property> newProperties = gProfile.getProperties().get("textures");
        cPlayer.getProfile().getProperties().removeAll("textures");
        cPlayer.getProfile().getProperties().putAll("textures", newProperties);
        PacketPlayOutEntityDestroy destroyPlayerPacket = new PacketPlayOutEntityDestroy(cPlayer.getEntityId());
        PacketPlayOutPlayerInfo playerInfoPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, cPlayer.getHandle());
        dispatchPackets(destroyPlayerPacket, playerInfoPacket);
    }

    private static void dispatchPacket(Packet packet) {
       for (Player p: Bukkit.getServer().getOnlinePlayers()) {
           ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
       }
    }

    private static void dispatchPackets(Packet... packets) {
        for(Packet packet: packets) {
            dispatchPacket(packet);
        }
    }

    @Override
    public boolean onCommand(CommandSender theSender, Command command, String s, String[] args) {
        changeSkin((CraftPlayer) theSender, args[0]);
        return false;
    }
}
