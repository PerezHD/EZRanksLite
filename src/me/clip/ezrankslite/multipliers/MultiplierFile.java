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
package me.clip.ezrankslite.multipliers;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import me.clip.ezrankslite.EZRanksLite;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MultiplierFile {

	private EZRanksLite plugin;
	private FileConfiguration dataConfig = null;
	private File dataFile = null;

	public MultiplierFile(EZRanksLite instance) {
		this.plugin = instance;
	}

	public boolean reload() {
		boolean firstLoad;
		if (this.dataFile == null) {
			this.dataFile = new File(this.plugin.getDataFolder(), "multipliers.yml");
		}
		if (this.dataFile.exists()) {
			firstLoad = false;
		} else {
			firstLoad = true;
		}

		this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
		setHeader();
		return firstLoad;
	}

	public FileConfiguration load() {
		if (this.dataConfig == null) {
			reload();
		}
		return this.dataConfig;
	}

	public void save() {
		if ((this.dataConfig == null) || (this.dataFile == null))
			return;
		try {
			load().save(this.dataFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE,
					"Could not save to " + this.dataFile, ex);
		}
	}
	
	public void setHeader() {
		this.dataConfig.options().header("EZRanksLite multiplier.yml file\n" +
				"This file allows you to create custom cost multipliers and discounts\n" +
				"Discounts need to be listed in the rankup_cost_discounts: section\n" +
				"Multipliers need to be listed in the rankup_cost_multipliers section\n" +
				"\n" +
				"The format for a multiplier and discount is the same:\n" +
				"<unique identifier>:\n" +
				"  priority: <unique number>\n" +
				"  permission: <your custom permission for this multiplier/discount>\n" +
				"  percentage: <your percentage to add on/take off of the cost>\n" +
				"\n" +
				"You can list as many multipliers/discounts you like for each section.\n" +
				"Each multiplier/discount must have a unique priority set!!!\n" +
				"The priority determines what multiplier comes first in your list\n" +
				"If a player happens to have multiple discount or multiplier\n" +
				"permissions, the lowest priority will always be applied\n" +
				"\n" +
				"The percentage system works just like a discount at a store...\n" +
				"If you set a discount percentage to 99.0 for a rank that cost 100 dollars" +
				"anyone with that discount permission will only pay 1 dollar\n" +
				"The format for percentage can include a decimal ex: 1.25, 99.9, 1, 1.05\n" +
				"DO NOT INCLUDE A % SYMBOL!!!\n" +
				"Discounts can not exceed a 100% percentage, \n" +
				"if your discount makes the rankup cost negative\n" +
				"the price will default to 1\n" +
				"You can set multipliers to any percentage: 400.0, 200.0 etc " +
				"\n" +
				"If a player has permission for a cost multiplier and discount, the multiplier will always be applied before the discount\n" +
				"is applied. Meaning: player has a multiplier to make ranking up cost twice as much and has perm for discount\n" +
				"The discount will be applied to the new cost AFTER the multiplier is applied.");
		this.dataConfig.options().copyDefaults(true);
		save();
	}

	public void createExampleMultiplier() {
		String prefix = "rankup_cost_multipliers.example";
		if (!this.dataConfig.contains("rankup_cost_multipliers")) {
			this.dataConfig.options().copyDefaults(true);
			this.dataConfig.set(prefix+".priority", 1.2);
			this.dataConfig.set(prefix+".permission", "some.multiplier.permission");
			this.dataConfig.set(prefix+".percentage", 1.5);
			save();
		}
		else if (this.dataConfig.getConfigurationSection("rankup_cost_multipliers").getKeys(false) == null
				|| this.dataConfig.getConfigurationSection("rankup_cost_multipliers").getKeys(false).isEmpty()) {
			this.dataConfig.set(prefix+".priority", 1.2);
			this.dataConfig.set(prefix+".permission", "some.multiplier.permission");
			this.dataConfig.set(prefix+".percentage", 1.5);
			save();
		}
		
	}
	
	public void createExampleDiscount() {
		String prefix = "rankup_cost_discounts.example";
		if (!this.dataConfig.isConfigurationSection("rankup_cost_discounts")) {
			this.dataConfig.set(prefix+".priority", 1.2);
			this.dataConfig.set(prefix+".permission", "some.discount.permission");
			this.dataConfig.set(prefix+".percentage", 2.2);
			save();
		}
		else if (this.dataConfig.getConfigurationSection("rankup_cost_discounts").getKeys(false) == null
				|| this.dataConfig.getConfigurationSection("rankup_cost_discounts").getKeys(false).isEmpty()) {
			this.dataConfig.set(prefix+".priority", 1);
			this.dataConfig.set(prefix+".permission", "some.discount.permission");
			this.dataConfig.set(prefix+".percentage", 1.5);
			save();
		}
		
	}
	
	
	
	public boolean containsEntry(String path) {
		return this.dataConfig.contains(path);
	}
	
	public boolean validateDiscount(String identifier) {
		boolean valid = true;
		if (containsEntry("rankup_cost_discounts."+identifier+".priority") != true) {
			valid = false;
			dataConfig.set("rankup_cost_discounts."+identifier+".priority", 1);
		}
		if (containsEntry("rankup_cost_discounts."+identifier+".permission") != true) {
			valid = false;
			dataConfig.set("rankup_cost_discounts."+identifier+".permission", "some.discount.permission");
		}
		if (containsEntry("rankup_cost_discounts."+identifier+".percentage") != true) {
			valid = false;
			dataConfig.set("rankup_cost_discounts."+identifier+".percentage", 1.5);
		}
		
		return valid;
	}
	
	public boolean validateMultiplier(String identifier) {
		boolean valid = true;
		if (containsEntry("rankup_cost_multipliers."+identifier+".priority") != true) {
			valid = false;
			dataConfig.set("rankup_cost_multipliers."+identifier+".priority", 1);
		}
		if (containsEntry("rankup_cost_multipliers."+identifier+".permission") != true) {
			valid = false;
			dataConfig.set("rankup_cost_multipliers."+identifier+".permission", "some.multiplier.permission");
		}
		if (containsEntry("rankup_cost_multipliers."+identifier+".percentage") != true) {
			valid = false;
			dataConfig.set("rankup_cost_multipliers."+identifier+".percentage", 1.5);
		}
		
		return valid;
	}
	
	public int loadMultipliers() {
		createExampleMultiplier();
		CostHandler.multipliers = new TreeMap<Integer, CostMultiplier>();
		
		Set<String> identifiers = this.dataConfig.getConfigurationSection("rankup_cost_multipliers").getKeys(false);
		
		if (identifiers == null || identifiers.isEmpty()) {
			plugin.debug(false, "There were no multipiers to load!");
			
			return 0;
		}
		
		for (String i : identifiers) {
			
			if (!validateMultiplier(i)) {
				save();
				plugin.debug(false, "Multiplier " + i + " was invalid and was automatically fixed! Please modify the default values and use /ezadmin reload to activate this multiplier!");
				continue;
			}
			
			CostMultiplier m = new CostMultiplier(i);
			m.setPriority(this.dataConfig.getInt("rankup_cost_multipliers."+i+".priority"));
			m.setPermission(this.dataConfig.getString("rankup_cost_multipliers."+i+".permission"));
			m.setMultiplier(this.dataConfig.getDouble("rankup_cost_multipliers."+i+".percentage"));
			CostHandler.multipliers.put(m.getPriority(), m);
		}
		return CostHandler.multipliers.size();
	}

	public int loadDiscounts() {
		createExampleDiscount();
		CostHandler.discounts = new TreeMap<Integer, Discount>();
		
		Set<String> identifiers = this.dataConfig.getConfigurationSection("rankup_cost_discounts").getKeys(false);
		
		if (identifiers == null || identifiers.isEmpty()) {
			plugin.debug(false, "There were no discounts to load!");
			
			return 0;
		}
		
		for (String i : identifiers) {
			
			if (!validateDiscount(i)) {
				save();
				plugin.debug(false, "Discount " + i + " was invalid and was automatically fixed! Please modify the default values and use /ezadmin reload to activate this discount!");
				continue;
			}
			
			Discount d = new Discount(i);
			d.setPriority(this.dataConfig.getInt("rankup_cost_discounts."+i+".priority"));
			d.setPermission(this.dataConfig.getString("rankup_cost_discounts."+i+".permission"));
			d.setMultiplier(this.dataConfig.getDouble("rankup_cost_discounts."+i+".percentage"));
			CostHandler.discounts.put(d.getPriority(), d);
		}
		return CostHandler.discounts.size();
	}
	
	
}
