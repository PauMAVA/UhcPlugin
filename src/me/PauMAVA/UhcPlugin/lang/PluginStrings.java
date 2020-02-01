/*
 * UhcPlugin
 * Copyright (c) 2019  Pau Machetti Vallverdú
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

package me.PauMAVA.UhcPlugin.lang;

import me.PauMAVA.UhcPlugin.UhcPluginCore;

public enum PluginStrings {

    /* CHAT PREFIXES */
    INGAME_PREFIX("chat.prefix.ingame"),
    GLOBAL_PREFIX("chat.prefix.global"),
    TEAM_PREFIX("chat.prefix.team"),
    DEVELOPER_PREFIX("chat.prefix.developer"),
    /* ADVANCEMENTS */
    ADVANCEMENT_MADE("chat.advancement"),
    /* COMMANDS */
    UNSUPPORTED_CMD("commands.unsupported"),
    ERROR_PREFIX("commands.errorprefix"),
    WARNING_PREFIX("commands.warningprefix"),
    INFO_PREFIX("commands.infoprefix"),
    CURRENT_CONFIG_HEADER("commands.config.current"),
    CONFIG_ARGUMENT_NUMBER_MISMATCH("commands.config.argmismatch"),
    CONFIG_USAGE("commands.config.usage"),
    CONFIG_NOVALUE("commands.config.novalue"),
    CONFIG_PARAMETER("commands.config.parameter"),
    CONFIG_ONLYINT("commands.config.onlyint"),
    CONFIG_VALUE_ASSIGNED("commands.config.valueassigned"),
    CONFIG_SUCCESS("commands.config.success"),
    CONFIG_THE_VALUE("commands.config.thevalue"),
    CONFIG_NEGATIVE_NO_PROBLEM("commands.config.negativenoproblem"),
    CONFIG_NEGATIVE_PROBLEM("commands.config.negativeproblem"),
    CONFIG_BAD_VALUE("commands.config.badvalue"),
    CONFIG_ACCEPTED_VALUE("commands.config.acceptedvalues"),
    CONFIG_ONLYBOOLEAN("commands.config.onlyboolean"),
    CONFIG_NO_OPTION("commands.config.nooption"),
    START_COUNTDOWN("commands.start.countdown"),
    START_ERROR("commands.start.error"),
    START_TITLE("commands.start.title"),
    START_SUBTITLE("commands.start.subtitle"),
    /* GAMEPLAY */
    DARK_CRYSTAL_DISPLAYNAME("gameplay.recipes.darkcrystal"),
    DARK_CRYSTAL_LORE1("gameplay.recipes.darkcrystallore1"),
    DARK_CRYSTAL_LORE2("gameplay.recipes.darkcrystallore2"),
    DARK_CRYSTAL_LORE3("gameplay.recipes.darkcrystallore3"),
    /* MATCH */
    DEATH_HEADER("match.death.messageheader"),
    DEATH_ELIMINATION("match.death.eliminated"),
    DEATH_INFO("match.death.info"),
    DEATH_THANKS("match.death.thanks"),
    DEATH_GITHUB("match.death.github"),
    EPISODE_START1("match.episodestart1"),
    EPISODE_START2("match.episodestart2"),
    SCOREBOARD_TIME_LEFT("match.scoreboard.timeleft"),
    SCOREBOARD_TOTAL_TIME("match.scoreboard.totaltime"),
    SCOREBOARD_EPISODE("match.scoreboard.episode"),
    SEASON_PREFIX("match.season"),
    /* TEAMS */
    TEAMS_THE_TEAM("teams.theteam"),
    TEAMS_ALREADY_EXISTS("teams.alreadyexists"),
    TEAMS_REGISTER_SUCCESS("teams.registersuccess"),
    TEAMS_DELETE_SUCCESS("teams.deletesuccess"),
    TEAMS_NOT_EXISTS("teams.notexists"),
    TEAMS_SAME_SIZE("teams.samesize"),
    TEAMS_NO_CHANGES("teams.nochanges"),
    TEAMS_AUTOKICK_NOTICE("teams.autokick"),
    TEAMS_SIZE_CHANGE_SUCCESS("teams.sizechangesuccess"),
    TEAMS_SIZE_CHANGE_SUCCESS2("teams.sizechangesuccess2"),
    TEAMS_UNEXPECTED_ERROR("teams.unexpectederror"),
    TEAMS_GET_NOT_EXISTS("teams.getnotexists"),
    TEAMS_FETCH_ALL("teams.fetchall"),
    TEAMS_NO_REGISTERED_TEAMS("teams.noteamsregistered"),
    TEAMS_IS_EMPTY("teams.isempty"),
    TEAMS_CONNECTOR("teams.connector"),
    TEAMS_INTEGRANTS("teams.integrants"),
    TEAMS_FREE_SPOTS("teams.freespots"),
    TEAMS_MAX_SIZE("teams.maxsize"),
    TEAMS_TEAM_NOT_EXISTS("teams.errorcodes.teamnotexists"),
    TEAMS_PLAYER_NOT_FOUND("teams.errorcodes.playernotfound"),
    TEAMS_FULL_TEAM("teams.errorcodes.fullteam"),
    TEAMS_OPERATION_SUCCESS("teams.errorcodes.success"),
    TEAMS_LAST_MEMBER("teams.elimination.lastmember"),
    TEAMS_ELIMINATED("teams.elimination.eliminated"),
    TEAMS_WIN_SUBTITLE("teams.elimination.winsubtitle"),
    TEAMS_THE_PLAYER("teams.elimination.theplayer"),
    TEAMS_PART_OF_TEAM("teams.elimination.partofteam"),
    TEAMS_CONNECTOR2("teams.elimination.connector2"),
    TEAMS_PLAYERS_LEFT("teams.elimination.playersleft"),
    TEAMS_DARK_CRYSTAL_WASTE("teams.darkcrystal.wasted"),
    TEAMS_DARK_CRYSTAL_USE("teams.darkcrystal.used"),
    TEAMS_LEGAL_ARGS_ADD_NOTICE("teams.legalargs.add.notice"),
    TEAMS_LEGAL_ARGS_ADD_USAGE("teams.legalargs.add.usage"),
    TEAMS_LEGAL_ARGS_KICK_NOTICE("teams.legalargs.kick.notice"),
    TEAMS_LEGAL_ARGS_KICK_USAGE("teams.legalargs.kick.usage"),
    TEAMS_LEGAL_ARGS_DELETE_NOTICE("teams.legalargs.delete.notice"),
    TEAMS_LEGAL_ARGS_DELETE_USAGE("teams.legalargs.delete.usage"),
    TEAMS_LEGAL_ARGS_REGISTER_NOTICE("teams.legalargs.register.notice"),
    TEAMS_LEGAL_ARGS_REGISTER_USAGE("teams.legalargs.register.usage"),
    TEAMS_LEGAL_ARGS_MAXSIZE_NOTICE("teams.legalargs.setmaxsize.notice"),
    TEAMS_LEGAL_ARGS_MAXSIZE_USAGE("teams.legalargs.setmaxsize.usage"),
    TEAMS_LEGAL_ARGS_NO_SUCH_OPTION("teams.legalargs.nosuchoption"),
    TEAMS_LEGAL_ARGS_AVAILABLE_OPTIONS("teams.legalargs.availableoptions"),
    /* UTIL */
    TABLIST_ALIVE_PLAYERS("util.tablist.aliveplayers"),
    TABLIST_COPYRIGHT("util.tablist.copyright"),
    /* WORLD */
    WORLD_BORDER_CLOSING1("world.border.closing1"),
    WORLD_BORDER_CLOSING2("world.border.closing2"),
    WORLD_BORDER_VELOCITY1("world.border.velocity1"),
    WORLD_BORDER_VELOCITY2("world.border.velocity2"),
    WORLD_BORDER_DISTANCE1("world.border.distance1"),
    WORLD_BORDER_DISTANCE2("world.border.distance2");

    private String path;

    /**
     * Sets the path to the target string in the language file as the constant parameter.
     * @param pathToString The path to the string in the language file.
     */
    PluginStrings(String pathToString) {
        this.path = pathToString;
    }

    /**
     * Overrides the toString function to return the path parameter.
     * @return The path parameter of the specified constant.
     */
    @Override
    public String toString() {
        return UhcPluginCore.getInstance().getLanguageManager().getString(this);
    }

    public String getPath() {
        return this.path;
    }
}