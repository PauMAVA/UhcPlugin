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

package me.PauMAVA.UhcPlugin.match;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.commands.UhcConfigCmd;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UhcMatchTimer extends BukkitRunnable {

    private UhcPluginCore plugin;
    private Integer chapterLength = plugin.getConfig().getInt("chapter_length");
    private Integer seconds = 59;
    private Integer minutes = chapterLength;
    private Integer episode = 1;
    private Integer totalTime = 0;

    @Override
    public void run() {
        seconds--;
        if(seconds < 0) {
            seconds = 60;
            minutes--;
            if(minutes < 0) {
                minutes = chapterLength;
                episode++;
            }
        }

    }

    private void refreshTimer(Integer episode, Integer minutes, Integer seconds) {

    }

}
