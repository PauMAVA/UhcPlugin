package me.PauMAVA.UhcPlugin.match;

import me.PauMAVA.UhcPlugin.UhcPluginCore;

public class UhcMatch {

    private final UhcPluginCore core;

    private MatchStatus status;

    public UhcMatch(UhcPluginCore core) {
        this.core = core;
        status = MatchStatus.WAITING;
    }



}
