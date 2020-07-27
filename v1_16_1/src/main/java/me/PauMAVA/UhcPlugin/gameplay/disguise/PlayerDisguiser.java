/*
 * (c) 2019-2020 - UhcPlugin - Pau Machetti Vallverdú
 *
 * UhcPlugin is free software: you can redistribute it and/or modify
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * PlayerDisguiser.java created on 26/7/20 19:48 by Pau
 *
 */

package me.PauMAVA.UhcPlugin.gameplay.disguise;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.PauMAVA.MojangAPI.MojangAPI;
import me.PauMAVA.MojangAPI.RawPlayerProfileJson;
import me.PauMAVA.MojangAPI.RawPlayerProfileProperty;
import me.PauMAVA.UhcPlugin.UhcPluginCore;
import net.minecraft.server.v1_16_R1.Packet;
import net.minecraft.server.v1_16_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R1.PacketPlayOutPlayerInfo;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.UUID;

public class PlayerDisguiser {

    private final UhcPluginCore plugin;
    private final MojangAPI mojangAPI;

    public PlayerDisguiser(UhcPluginCore plugin) {
        this.plugin = plugin;
        mojangAPI = new MojangAPI();
    }

    public void disguisePlayer(UUID from, UUID to) {
        Player target = plugin.getPlayerByUUID(from);
        RawPlayerProfileJson profile = getRawPlayerProfileOptimized(to);
        applyNewInfo(target, profile);
    }

    private void applyNewInfo(Player target, RawPlayerProfileJson info) {
        CraftPlayer cPlayer = (CraftPlayer) target;
        GameProfile modifiedProfile = buildDisguisedProfile(target, info);
        try {
            Field profileField = cPlayer.getHandle().getClass().getSuperclass().getDeclaredField("bQ");
            profileField.setAccessible(true);
            profileField.set(cPlayer, modifiedProfile);
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
        for (Player p: plugin.getServer().getOnlinePlayers()) {
            dispatchPackets((CraftPlayer) p, destroy, entitySpawn, removeInfo, addInfo);
            p.hidePlayer(plugin, cPlayer);
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.showPlayer(plugin, cPlayer);
                }
            }.runTaskLater(plugin, 5L);
        }
    }

    private GameProfile buildDisguisedProfile(Player target, RawPlayerProfileJson info) {
        GameProfile profile = new GameProfile(target.getUniqueId(), info.getName());
        RawPlayerProfileProperty skinProperty = info.getProperties().get(0);
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", skinProperty.getValue(), skinProperty.getSignature()));
        return profile;
    }

    private RawPlayerProfileJson getRawPlayerProfileOptimized(UUID target) {
        return  mojangAPI.getMojangAPICache().hasCachedRawProfile(target) ?
                mojangAPI.getMojangAPICache().getRawProfile(target) :
                mojangAPI.getPlayerInfoHandler().getRawPlayerProfile(target);
    }

    private void dispatchPackets(CraftPlayer target, Packet... packets) {
        for (Packet packet : packets) {
            target.getHandle().playerConnection.networkManager.sendPacket(packet);
        }
    }

}
