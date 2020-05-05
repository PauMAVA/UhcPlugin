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

public class Locale {

    private String languageName;
    private String shortName;
    private String author;

    /**
     * Set up the Locale object
     * @param languageName The language long name e.g ENGLISH
     * @param shortName The language short name e.g en
     * @param author The language translation author name
     */
    public Locale(String languageName, String shortName, String author) {
        this.languageName = languageName;
        this.shortName = shortName;
        this.author = author;
    }

    /**
    * Get the language name
    * @return The language long name e.g ENGLISH
    */
    public String getLanguageName() {
        return this.languageName;
    }

    /**
     * Get the language short name
     * @return The language short name e.g en
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * Get the language translation author
     * @return The author name
     */
    public String getAuthor() {
        return this.author;
    }

}
