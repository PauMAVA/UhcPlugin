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
import me.PauMAVA.UhcPlugin.commands.UhcConfigCmd;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;

public class LanguageManager {

    private Locale selectedLocale;
    private FileConfiguration languageFile;

    /**
     * Sets up the LanguageManager object. If the selected locale in the config cannot be accessed, it tries
     * to load the default Locale (ENGLISH, en, PauMAVA). On fail reports the error.
     */
    public LanguageManager() {
        extractLanguageFiles();
        setUpLocales();
        if (!setLocale(LocaleRegistry.getLocaleByShortName(UhcConfigCmd.getLocale()))) {
            UhcPluginCore.getInstance().getLogger().warning("Couldn't load lang " + UhcConfigCmd.getConfig().getString("lang") + "!");
            UhcPluginCore.getInstance().getLogger().warning("Loading default language lang_en...");
            if (!setLocale(LocaleRegistry.getLocaleByShortName("en"))) {
                UhcPluginCore.getInstance().getLogger().severe("Failed to load default language! Plugin won't work properly!");
            } else {
                UhcPluginCore.getInstance().getLogger().info("Successfully loaded lang_en.yml!");
            }
        } else {
            UhcPluginCore.getInstance().getLogger().info("Successfully loaded \'" + UhcConfigCmd.getLocale() + "\' locale!");
        }
    }

    /**
     * Changes the selected locale.
     * @param locale The target locale that will be selected.
     * @return true if locale file exists. false if locale file doesn't exist
     */
    private boolean setLocale(Locale locale) {
        File targetFile = new File(UhcPluginCore.getInstance().getDataFolder().toString() + "/lang_" + locale.getShortName() + ".yml");
        if (targetFile.exists()) {
            this.selectedLocale = locale;
            this.languageFile = YamlConfiguration.loadConfiguration(targetFile);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Registers the available locales.
     */
    private void setUpLocales() {
        LocaleRegistry.registerLocale(new Locale("ENGLISH", "en", "PauMAVA"));
        LocaleRegistry.registerLocale(new Locale("SPANISH", "es", "Lauuu"));
        LocaleRegistry.registerLocale(new Locale("CATALAN", "ca", "Crazychemist"));
        LocaleRegistry.registerLocale(new Locale("BRAZILIAN-PORTUGUESE", "br", "Cabreira"));
    }

    private void extractLanguageFiles() {
        HashMap<File, InputStream> streams = new HashMap<File, InputStream>() {{
           put(new File(UhcPluginCore.getInstance().getDataFolder().getPath() + "/lang_en.yml"), LanguageManager.class.getResourceAsStream("/lang_en.yml"));
           put(new File(UhcPluginCore.getInstance().getDataFolder().getPath() + "/lang_es.yml"), LanguageManager.class.getResourceAsStream("/lang_es.yml"));
           put(new File(UhcPluginCore.getInstance().getDataFolder().getPath() + "/lang_ca.yml"), LanguageManager.class.getResourceAsStream("/lang_ca.yml"));
           put(new File(UhcPluginCore.getInstance().getDataFolder().getPath() + "/lang_br.yml"), LanguageManager.class.getResourceAsStream("/lang_br.yml"));
        }};
        for (File destination: streams.keySet()) {
            try {
                if (!destination.exists()) {
                    destination.createNewFile();
                    InputStream in = streams.get(destination);
                    byte[] buffer = new byte[in.available()];
                    in.read(buffer);
                    OutputStream out = new FileOutputStream(destination);
                    out.write(buffer);
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get a string from the locale file.
     * @param string The constant containing the path in the language file.
     * @return The string at the specified path or an empty string if path is not set.
     */
    public String getString(PluginStrings string) {
        if (this.languageFile.isSet(string.getPath())) {
            return this.languageFile.getString(string.getPath()).replace("&", "§");
        } else {
            return "";
        }
    }
}
