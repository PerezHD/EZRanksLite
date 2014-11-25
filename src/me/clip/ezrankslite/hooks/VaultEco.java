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
package me.clip.ezrankslite.hooks;

import me.clip.ezrankslite.EZRanksLite;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEco {

	private EZRanksLite plugin;

	public VaultEco(EZRanksLite instance) {
		plugin = instance;
	}
	
	public static Economy econ = null;
	
	public boolean setupEconomy() {
		if (this.plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = this.plugin.getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public boolean hasEnoughMoney(double amount, OfflinePlayer p) {
		if (econ.has(p, amount)) {
			return true;
		}
		return false;
	}
	
	public boolean withdrawMoney(double amount, OfflinePlayer p) {
		EconomyResponse econResp = econ.withdrawPlayer(p, amount);
		return econResp.transactionSuccess();
	}
	
	public double getBalance(OfflinePlayer p) {
		return econ.getBalance(p);
	}
	
}
