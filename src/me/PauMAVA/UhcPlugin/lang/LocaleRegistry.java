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

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;
import java.util.List;

public class LocaleRegistry {

    private static List<Locale> locales = new ArrayList<Locale>();

    /**
     * Registers a new locale into the locale registry
     * @param locale The locale to register
     */
    public static void registerLocale(Locale locale) {
        if (!locales.contains(locale)) {
            locales.add(locale);
        }
    }

    /**
     * Unregisters a locale from the locale registry if is registered.
     * @param locale The locale to unregister
     */
    public static void unregisterLocale(Locale locale) {
        locales.remove(locale);
    }

    /**
     * Get all registered locales
     * @return A list containing all registered locales
     */
    public static List<Locale> getLocales() {
        return locales;
    }

    /**
     * Get a registered locale by its language long name
     * @param longName The language long name e.g ENGLISH
     * @return The locale containing that language name if registered. If not returns a Locale containing "unknown" as its parameters.
     */
    public static Locale getLocaleByLongName(String longName) {
        for (Locale locale: locales) {
            if (locale.getLanguageName().equalsIgnoreCase(longName)) {
                return locale;
            }
        }
        return new Locale("Unknown", "Unknown", "Unknown");
    }

    /**
     * Get a registered locale by its language short name
     * @param shortName The language short name e.g en
     * @return The locale containing that language short name if registered. If not returns a Locale containing "unknown" as its parameters.
     */
    public static Locale getLocaleByShortName(String shortName) {
        for (Locale locale: locales) {
            if (locale.getShortName().equalsIgnoreCase(shortName)) {
                return locale;
            }
        }
        return new Locale("Unknown", "Unknown", "Unknown");
    }
}
