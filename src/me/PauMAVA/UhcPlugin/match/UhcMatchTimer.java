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

package me.PauMAVA.UhcPlugin.match;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class UhcMatchTimer extends BukkitRunnable {

    private Integer chapterLength = 25;
    private Integer seconds = 59;
    private Integer minutes = chapterLength;
    private Integer episode = 1;
    private Integer totalTime = 0;
    private Integer i = -20;
    private String secondsString, minutesString, totalTimeString;
    private String season = UhcPluginCore.getInstance().getConfig().getString("season");

    @Override
    public void run() {
        seconds--;
        if(seconds < 0) {
            seconds = 59;
            minutes--;
            if(minutes < 0) {
                minutes = chapterLength;
                episode++;
            }
        }
        if(seconds < 10) {
            secondsString = "0" + seconds.toString();
        } else {
            secondsString = seconds.toString();
        }
        if(minutes < 10) {
            minutesString = "0" + minutes.toString();
        } else {
            minutesString = minutes.toString();
        }
        if(i > 0) {
            switch (i) {
                case 1: {
                    UhcScoreboardManager.refreshSidebarTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "U" + ChatColor.GOLD + "" + ChatColor.BOLD + "HC S" + season);
                    break;
                }
                case 2: {
                    UhcScoreboardManager.refreshSidebarTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "U" + ChatColor.YELLOW + "" + ChatColor.BOLD + "H" + ChatColor.GOLD + "" + ChatColor.BOLD + "C S" + season);
                    break;
                }
                case 3: {
                    UhcScoreboardManager.refreshSidebarTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "UH" + ChatColor.YELLOW + "" + ChatColor.BOLD + "C" + ChatColor.GOLD + "" + ChatColor.BOLD + " S" + season);
                    break;
                }
                case 4: {
                    UhcScoreboardManager.refreshSidebarTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "UHC" + ChatColor.YELLOW + "" + ChatColor.BOLD + " " + ChatColor.GOLD + "" + ChatColor.BOLD + "S" + season);
                    break;
                }
                case 5: {
                    UhcScoreboardManager.refreshSidebarTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "UHC " + ChatColor.YELLOW + "" + ChatColor.BOLD + "S" + ChatColor.GOLD + "" + ChatColor.BOLD + "" + season);
                    break;
                }
                case 6: {
                    UhcScoreboardManager.refreshSidebarTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "UHC S" + ChatColor.YELLOW + "" + ChatColor.BOLD + season);
                    break;
                }
                case 7: {
                    UhcScoreboardManager.refreshSidebarTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "UHC S" + season);
                    i = -20;
                    break;
                }
            }
        }
        String elapsedMinutes, elapsedSeconds;
        if((((totalTime % 86400) % 3600) / 60) < 10) {
            elapsedMinutes = "0" + ((totalTime % 86400) % 3600) / 60;
        } else {
            elapsedMinutes = "" + ((totalTime % 86400) % 3600) / 60;
        }
        if((((totalTime % 86400) % 3600) % 60) < 10) {
            elapsedSeconds = "0" + ((totalTime % 86400) % 3600) % 60;
        } else {
            elapsedSeconds = "" + ((totalTime % 86400) % 3600) % 60;
        }
        totalTimeString = String.valueOf((totalTime % 86400) / 3600) + ":" + elapsedMinutes + ":" + elapsedSeconds;
        UhcScoreboardManager.refreshSidebar(minutesString, secondsString, episode.toString(), totalTimeString);
        i++;
        totalTime++;
    }

}
