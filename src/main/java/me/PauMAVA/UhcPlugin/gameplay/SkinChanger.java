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
import me.PauMAVA.MojangAPI.MojangAPI;
import me.PauMAVA.MojangAPI.RawPlayerProfileJson;
import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.teams.UhcTeamsManager;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class SkinChanger {

    private MojangAPI mojangAPI;

    private HashMap<Player, Player> playerMapping = new HashMap<>();

    private HashMap<UUID, String> originalPlayerNames = new HashMap<>();

    public SkinChanger() {
        this.mojangAPI = new MojangAPI();
    }

    public String getOriginalName(UUID uuid) {
        for (UUID key: originalPlayerNames.keySet()) {
            if (key.equals(uuid)) {
                return originalPlayerNames.get(uuid);
            }
        }
        return "";
    }

    public void cachePlayerInfo() {
        for (Player p: Bukkit.getServer().getOnlinePlayers()) {
            originalPlayerNames.put(p.getUniqueId(), p.getName());
        }
    }

    private GameProfile buildProfile(CraftPlayer cPlayer, UUID uuid) {
        GameProfile profile = new GameProfile(cPlayer.getUniqueId(), getOriginalName(uuid));
        RawPlayerProfileJson rawProfile;
        if (this.mojangAPI.getMojangAPICache().hasCachedRawProfile(uuid)) {
            rawProfile = this.mojangAPI.getMojangAPICache().getRawProfile(uuid);
        } else {
            rawProfile = this.mojangAPI.getPlayerInfoHandler().getRawPlayerProfile(uuid);
        }
        String skinUrl = rawProfile.getProperties().get(0).getValue();
        String signature = rawProfile.getProperties().get(0).getSignature();
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", skinUrl, signature));
        return profile;
    }

    private void changeSkin(CraftPlayer cPlayer, UUID requestedSkinUUID) {
        GameProfile gProfile = buildProfile(cPlayer, requestedSkinUUID);
        try {
            Field profileField = cPlayer.getHandle().getClass().getSuperclass().getDeclaredField("bT");
            profileField.setAccessible(true);
            profileField.set(cPlayer.getHandle(), gProfile);
            profileField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        reloadPlayer(cPlayer);
    }

    private void reloadPlayer(CraftPlayer cPlayer) {
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cPlayer.getEntityId());
        PacketPlayOutNamedEntitySpawn entitySpawn = new PacketPlayOutNamedEntitySpawn(cPlayer.getHandle());
        PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, cPlayer.getHandle());
        PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, cPlayer.getHandle());
        for (Player p: Bukkit.getServer().getOnlinePlayers()) {
            if (p.getUniqueId() != cPlayer.getPlayer().getUniqueId() && !UhcTeamsManager.getTeamObject(UhcTeamsManager.getPlayerTeam(cPlayer.getName())).isInTeam(p.getName())) {
                dispatchPackets(p, destroy, entitySpawn, removeInfo, addInfo);
                Bukkit.getServer().getScheduler().runTask(UhcPluginCore.getInstance(), () -> p.hidePlayer(cPlayer.getPlayer()));
                Bukkit.getServer().getScheduler().runTaskLater(UhcPluginCore.getInstance(), () -> p.showPlayer(cPlayer.getPlayer()), 5);
            }
        }
    }

    private void dispatchPacket(Player player, Packet packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private void dispatchPackets(Player player, Packet... packets) {
        for(Packet packet: packets) {
            dispatchPacket(player, packet);
        }
    }

    public void rotateSkinsAsync() {
        new BukkitRunnable(){
            @Override
            public void run() {
                mapPlayers(Bukkit.getServer().getOnlinePlayers());
                for (Player p: playerMapping.keySet()) {
                    changeSkin((CraftPlayer) p, playerMapping.get(p).getUniqueId());
                    sendTitle(p, playerMapping.get(p));
                }
            }
        }.runTaskAsynchronously(UhcPluginCore.getInstance());
    }

    private void mapPlayers(Collection<? extends Player> players) {
        List<? extends Player> shuffledPlayers = new ArrayList<>(players);
        Collections.shuffle(shuffledPlayers);
        Player[] originalPlayers = players.toArray(new Player[0]);
        for (int j = 0; j < players.size(); j++) {
            this.playerMapping.put(originalPlayers[j], shuffledPlayers.get(j));
        }
    }

    private void sendTitle(Player original, Player disguise) {
        original.sendTitle(ChatColor.GREEN + "◆"+ ChatColor.RESET + ChatColor.AQUA + "" + ChatColor.BOLD + " SKIN ROLL " + ChatColor.RESET + ChatColor.GREEN + "◆", ChatColor.GRAY + "Your disguise: " + originalPlayerNames.get(disguise.getUniqueId()), 0, 5*20,1*20);
    }
}
