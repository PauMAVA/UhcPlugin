package me.PauMAVA.UhcPlugin.teams;

import me.PauMAVA.UhcPlugin.UhcPluginCore;

import java.util.List;
import java.util.Objects;

public class UhcTeamManager {

    private final UhcPluginCore core;

    private final TeamHandlingMode mode;

    private List<UhcTeam> teams;

    public UhcTeamManager(UhcPluginCore core) {
        this(core, TeamHandlingMode.PRE_MADE);
    }

    public UhcTeamManager(UhcPluginCore core, TeamHandlingMode mode) {
        this.core = core;
        this.mode = mode;
    }

    public UhcTeam registerTeam(String name, int maxSize) {
        if (isTeamRegistered(name)) {
            return null;
        }
        UhcTeam team = new UhcTeam(name, maxSize);
        teams.add(team);
        return team;
    }

    public UhcTeam getTeamByName(String name) {
        return teams.stream().filter(uhcTeam -> uhcTeam.getName().equals(name)).findFirst().get();
    }

    public boolean isTeamRegistered(String name) {
        return teams.stream().anyMatch(team -> team.getName().equals(name));
    }

    public boolean removeTeam(UhcTeam team) {
        return teams.removeIf(uhcTeam -> uhcTeam.getUuid().equals(team.getUuid()));
    }

}
