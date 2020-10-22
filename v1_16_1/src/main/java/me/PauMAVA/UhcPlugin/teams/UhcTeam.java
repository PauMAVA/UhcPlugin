package me.PauMAVA.UhcPlugin.teams;

import java.util.List;
import java.util.UUID;

public class UhcTeam {

    private UUID uuid;

    private final String name;

    private String displayName;

    private int maxSize;

    private List<UhcPlayer> players;

    public UhcTeam(String name, int maxSize) {
        this.name = name;
        this.displayName = name;
        this.maxSize = maxSize;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        if (this.maxSize > maxSize) {
            players.removeIf(player -> players.indexOf(player) > maxSize - 1);
        }
        this.maxSize = maxSize;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<UhcPlayer> getPlayers() {
        return players;
    }

    public boolean addPlayer(UhcPlayer player) {
        if (containsPlayer(player)) {
            return false;
        }
        players.add(player);
        return true;
    }

    public boolean removePlayer(UhcPlayer player) {
        return players.removeIf(player::equals);
    }

    public boolean containsPlayer(UhcPlayer player) {
        return players.stream().anyMatch(player::equals);
    }

}
