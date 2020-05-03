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

public class SkinChanger extends BukkitRunnable implements CommandExecutor {

    private MojangAPI mojangAPI;

    private HashMap<Player, Player> playerMapping = new HashMap<>();

    public SkinChanger() {
        this.mojangAPI = new MojangAPI();
    }

    @Override
    public void run() {
        rotateSkins();
        this.cancel();
    }

    private GameProfile buildProfile(CraftPlayer cPlayer, String userName) {
        GameProfile profile = new GameProfile(cPlayer.getUniqueId(), userName);
        RawPlayerProfileJson rawProfile = this.mojangAPI.getPlayerInfoHandler().getRawPlayerProfile(this.mojangAPI.getPlayerInfoHandler().fetchUUID(userName));
        String skinUrl = rawProfile.getProperties().get(0).getValue();
        String signature = rawProfile.getProperties().get(0).getSignature();
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", skinUrl, signature));
        return profile;
    }

    private void changeSkin(CraftPlayer cPlayer, String requestedSkinName) {
        GameProfile gProfile = buildProfile(cPlayer, requestedSkinName);
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

    public void rotateSkins() {
        mapPlayers();
        for (Player target: Bukkit.getServer().getOnlinePlayers()) {
            UhcPluginCore.getInstance().getLogger().info("Target player: " + target.getName());
            Player random = pickRandomPlayer(target);
            UhcPluginCore.getInstance().getLogger().info("Random player: " + random.getName());
            changeSkin((CraftPlayer) target, random.getName());
            sendTitle(target, random);
        }
    }

    private void mapPlayers() {
        Player[] playerArray = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        int i = 0;
        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
            playerArray[i] = player;
            i++;
        }
        List<Player> players = Arrays.asList(playerArray);
        List<Player> shuffledPlayers = new ArrayList<>();
        Collections.copy(players, shuffledPlayers);
        Collections.shuffle(shuffledPlayers);
        for (int j = 0; j < players.size(); j++) {
            this.playerMapping.put(players.get(j), shuffledPlayers.get(j));
        }
    }

    private Player pickRandomPlayer(Player original) {
        for (Player p: playerMapping.keySet()) {
            if (p.getUniqueId().equals(original.getUniqueId())) {
                return playerMapping.get(p);
            }
        }
        return original;
    }

    private void sendTitle(Player original, Player disguise) {
        original.sendTitle(ChatColor.GREEN + "◆"+ ChatColor.RESET + ChatColor.AQUA + "" + ChatColor.BOLD + " SKIN ROLL " + ChatColor.RESET + ChatColor.GREEN + "◆", ChatColor.GRAY + "Your disguise: " + disguise.getName(), 0, 5*20,1*20);
    }

    @Override
    public boolean onCommand(CommandSender theSender, Command command, String s, String[] args) {
        changeSkin((CraftPlayer) theSender, args[0]);
        return false;
    }



}
