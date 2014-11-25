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

import java.util.Arrays;
import java.util.List;
import me.clip.ezrankslite.EZRanksLite;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

	private EZRanksLite plugin;

	public Config(EZRanksLite instance) {
		plugin = instance;
	}

	public void loadDefaultConfiguration() {
		FileConfiguration config = plugin.getConfig();
		config.options().header(
				"EZRanksLite version: " + plugin.getDescription().getVersion()
						+ " Main Configuration");
		config.addDefault("debug", Boolean.valueOf(false));
		config.addDefault("server_name", "EZRankCraft");
		config.addDefault("ranks.enabled", true);
		config.addDefault("ranks.format_has_access",
				"&a%rankfrom% &bto &a%rankto%  &bcost: &f%cost%");
		config.addDefault("ranks.format_no_access",
				"&7%rankfrom% &cto &7%rankto%  &ccost: &7%cost%");
		config.addDefault("ranks.header",
				Arrays.asList(new String[] { "none" }));
		config.addDefault("ranks.footer",
				Arrays.asList(new String[] { "none" }));
		config.addDefault("rankup_cooldown.enabled", true);
		config.addDefault("rankup_cooldown.time", 5);
		if (config.contains("scoreboard.text")) {
			config.set("scoreboard.text", null);
		}
		if (config.contains("check_updates")) {
			config.set("check_updates", null);
		}
		config.addDefault("scoreboard.enabled", true);
		config.addDefault("scoreboard.refresh_time", 30);
		config.addDefault("scoreboard.title", "&cScoreboard");
		config.addDefault(
				"scoreboard.display",
				Arrays.asList(new String[] { "&cCurrent Rank:", "%rankfrom%",
						"blank", "&c/rankup:", "%rankto%", "blank", "&cCost:",
						"&a$&f%cost%", "&cBalance:", "&a$&f%balance%" }));
		config.addDefault("scoreboard.no_rankups", "&cnone");
		config.addDefault("scoreboard.can_rankup", "&a/rankup");
		config.addDefault("scoreboard.progress_bar.bar_color", "&a");
		config.addDefault("scoreboard.progress_bar.needs_color", "&8");
		config.addDefault("scoreboard.progress_bar.end_color", "&f&l");
		config.addDefault("scoreboard.progress_bar.left_character", "\u2503");
		config.addDefault("scoreboard.progress_bar.bar_character", ":");
		config.addDefault("scoreboard.progress_bar.right_character", "\u2503");
		config.addDefault("scoreboard.disabled_worlds", Arrays.asList(new String[] {"world_nether", "world_the_end"}));
		config.addDefault("staff_scoreboard.enabled", true);
		config.addDefault("staff_scoreboard.title", "&cStaff");
		config.addDefault("staff_scoreboard.display",
				Arrays.asList(new String[] { "&cPlayers online:", "%onlineplayers%/%onlinemax%",
						"blank", "&cStaff online:", "%onlinestaff%", "blank", "&cTPS:",
						"&a%tps%", "&cPing:",
						"&a%ping%"}));
		config.addDefault("money.fix_thousands", true);
		config.addDefault("money.fix_millions", true);
		config.addDefault("money.thousands_format", "k");
		config.addDefault("money.millions_format", "M");
		config.addDefault("money.billions_format", "B");
		config.addDefault("money.trillions_format", "T");
		config.addDefault("money.quadrillions_format", "Q");
		config.addDefault("check_updates", true);
		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public String getSbProgressLeftChar() {
		return plugin.getConfig().getString("scoreboard.progress_bar.left_character");
	}
	
	public String getSbProgressRightChar() {
		return plugin.getConfig().getString("scoreboard.progress_bar.right_character");
	}
	
	public String getSbProgressBarChar() {
		return plugin.getConfig().getString("scoreboard.progress_bar.bar_character");
	}
	public String getSbProgressBarNeedsColor() {
		return plugin.getConfig().getString("scoreboard.progress_bar.needs_color");
	}
	
	public boolean useStaffSb() {
		return plugin.getConfig().getBoolean("staff_scoreboard.enabled");
	}
	
	public List<String> getStaffSbText() {
		return plugin.getConfig().getStringList("staff_scoreboard.display");
	}
	
	public String getStaffSbTitle() {
		return plugin.getConfig().getString("staff_scoreboard.title");
	}
	//ill look at it in a bit
	
	
	
	public boolean checkUpdates() {
			return plugin.getConfig().getBoolean("check_updates");
	}
	
	public String getProgressBarEndColor() {
		return plugin.getConfig().getString("scoreboard.progress_bar.end_color");
	}
	
	public String getProgressBarColor() {
		return plugin.getConfig().getString("scoreboard.progress_bar.bar_color");
	}

	public boolean isDebug() {
		return plugin.getConfig().getBoolean("debug");
	}

	public boolean useRankupCooldown() {
		return plugin.getConfig().getBoolean("rankup_cooldown.enabled");
	}

	public int rankupCooldownTime() {
		return plugin.getConfig().getInt("rankup_cooldown.time");
	}

	public boolean useRanks() {
		return plugin.getConfig().getBoolean("ranks.enabled");
	}

	public String ranksAccess() {
		return plugin.getConfig().getString("ranks.format_has_access");
	}

	public String ranksNoAccess() {
		return plugin.getConfig().getString("ranks.format_no_access");
	}

	public List<String> ranksHeader() {
		return plugin.getConfig().getStringList("ranks.header");
	}

	public List<String> ranksFooter() {
		return plugin.getConfig().getStringList("ranks.footer");
	}

	// scoreboard
	public boolean useScoreboard() {
		return plugin.getConfig().getBoolean("scoreboard.enabled");
	}

	public int getScoreboardRefreshTime() {
		return plugin.getConfig().getInt("scoreboard.refresh_time");
	}

	public String sbTitle() {
		return plugin.getConfig().getString("scoreboard.title");
	}

	public List<String> sbDisplay() {
		return plugin.getConfig().getStringList("scoreboard.display");
	}
	
	public String noRankups() {
		return plugin.getConfig().getString("scoreboard.no_rankups");
	}
	
	public String canRankup() {
		return plugin.getConfig().getString("scoreboard.can_rankup");
	}
	
	public List<String> sbDisabledWorlds() {
		return plugin.getConfig().getStringList("scoreboard.disabled_worlds");
	}

	// settings
	public String getServerName() {
		return plugin.getConfig().getString("server_name");
	}

	public boolean fixThousands() {
		return plugin.getConfig().getBoolean("money.fix_thousands");
	}

	public boolean fixMillions() {
		return plugin.getConfig().getBoolean("money.fix_millions");
	}
	
	public String getQFormat() {
		return plugin.getConfig().getString("money.quadrillions_format");
	}

	public String getTFormat() {
		return plugin.getConfig().getString("money.trillions_format");
	}

	public String getBFormat() {
		return plugin.getConfig().getString("money.billions_format");
	}

	public String getMFormat() {
		return plugin.getConfig().getString("money.millions_format");
	}

	public String getKFormat() {
		return plugin.getConfig().getString("money.thousands_format");
	}
	

}
