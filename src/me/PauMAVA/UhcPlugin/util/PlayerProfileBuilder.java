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

package me.PauMAVA.UhcPlugin.util;

import com.mojang.authlib.GameProfile;
import me.PauMAVA.UhcPlugin.UhcPluginCore;

public class PlayerProfileBuilder {

    /* Waiting for proxied Mojang API development */

    private final UhcPluginCore plugin = UhcPluginCore.getInstance();

    public GameProfile getPlayerProfile(String playerName) throws NullPointerException {
        return null;
    }
}
