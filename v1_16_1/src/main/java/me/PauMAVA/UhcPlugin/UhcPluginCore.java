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
 * UhcPluginCore.java created on 26/7/20 19:45 by Pau
 *
 */

package me.PauMAVA.UhcPlugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Level;

public class UhcPluginCore extends JavaPlugin {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    private void log(Level level, String message) {
        getLogger().log(level, message);
    }

    public void logInfo(String message) {
        log(Level.INFO, message);
    }

    public void logWarning(String message) {
        log(Level.WARNING, message);
    }

    public void logSevere(String message) {
        log(Level.SEVERE, message);
    }

    public Player getPlayerByUUID(UUID target) {
        return getServer().getOnlinePlayers()
                .stream()
                .filter(player -> player.getUniqueId().equals(target))
                .findFirst()
                .orElse(null);
    }

}
