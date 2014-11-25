/* This file is a class of EZRanksLite
 * @author Ryan McCarthy
 * 
 * 
 * EZRanksLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 
 * EZRanksLite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.clip.ezrankslite.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigWrapper {

	private final JavaPlugin plugin;
	private FileConfiguration config;
	private File configFile;
	private final String folderName, fileName;

	public ConfigWrapper(final JavaPlugin instance, final String folderName,
			final String fileName) {
		this.plugin = instance;
		this.folderName = folderName;
		this.fileName = fileName;
	}

	public void createNewFile(final String message, final String header) {
		reloadConfig();
		saveConfig();
		loadConfig(header);

		if (message != null) {
			plugin.getLogger().info(message);
		}
	}

	public FileConfiguration getConfig() {
		if (config == null) {
			reloadConfig();
		}
		return config;
	}

	public void loadConfig(final String header) {
		config.options().header(header);
		config.options().copyDefaults(true);
		saveConfig();
	}

	public void reloadConfig() {
		if (configFile == null) {
			configFile = new File(plugin.getDataFolder() + folderName, fileName);
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public void saveConfig() {
		if (config == null || configFile == null) {
			return;
		}
		try {
			getConfig().save(configFile);
		} catch (final IOException ex) {
			plugin.getLogger().log(Level.SEVERE,
					"Could not save config to " + configFile, ex);
		}
	}
}
