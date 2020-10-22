package me.PauMAVA.UhcPlugin.teams;

import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class UhcPlayer {

    private final UUID uuid;

    private final String originalName;

    public UhcPlayer(Player player) {
        uuid = player.getUniqueId();
        originalName = player.getName();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getOriginalName() {
        return originalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UhcPlayer uhcPlayer = (UhcPlayer) o;
        return Objects.equals(uuid, uhcPlayer.uuid) &&
                Objects.equals(originalName, uhcPlayer.originalName);
    }

}
