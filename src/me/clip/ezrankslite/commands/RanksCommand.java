/* This file is a class of EZRanksLite
 * @author extended_clip
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
package me.clip.ezrankslite.commands;

import java.util.Map;
import java.util.TreeMap;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.Lang;
import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.rankdata.EZRank;
import me.clip.ezrankslite.rankdata.EZRankup;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RanksCommand implements CommandExecutor {

	private EZRanksLite plugin;
	
	public RanksCommand(EZRanksLite instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		
		if (!(sender instanceof Player)) {
			plugin.sms(sender, "&cuse ezadmin list to view the rank list");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (!plugin.useRanksCommand()) {
			if (p.hasPermission("ezranks.admin")) {
				plugin.sms(p, Lang.RANKS_DISABLED.getConfigValue(null));
			}				
			return true;
		}
		
		if (plugin.getRanksHeader() != null) {
		for (String header : plugin.getRanksHeader()) {
            if (header.equalsIgnoreCase("none") && header.equalsIgnoreCase("off")) {
            } else {
                plugin.sms(p, header);
            }
        }
		}
		
		
		
		String pRank = plugin.getHooks().getGroup(p);

		Map<Integer, EZRank> map = new TreeMap<Integer, EZRank>();
		
		if (!plugin.getRankHandler().isLoaded()) {
			
			plugin.sms(p, Lang.RANKS_NO_RANKUPS_LOADED.getConfigValue(null));
			
			if (plugin.getRanksFooter() != null) {
				for (String footer : plugin.getRanksFooter()) {
					if (footer.equalsIgnoreCase("none") || footer.equalsIgnoreCase("off")) {}
					else {
						plugin.sms(p, footer);
					}
				}
				}
			return true;
		}
		
		for (String rank : plugin.getRankHandler().getLoadedRanks()) {
			int o = plugin.getRankHandler().getRankData(rank).getRankOrder();
			map.put(o, plugin.getRankHandler().getRankData(rank));
		}
		
		for(Integer key : map.keySet()) {
			EZRank ezr = map.get(key);
			if (ezr.hasRankups()){
				if (ezr.getRankups() == null) {
					continue;
				}
			for (EZRankup r : ezr.getRankups()) {
				if (r == null) {
					continue;
				}
				if (ezr.getRank() == null || r.getRank() == null) {
					plugin.getLogger().warning("There was a problem with your rankups.yml!");
					continue;
				}
				
				double cost = CostHandler.getMultiplier(p, Double.parseDouble(r.getCost()));
				cost = CostHandler.getDiscount(p, cost);
				
				if (pRank != null && pRank.equalsIgnoreCase(ezr.getRank())) {
				plugin.sms(p, plugin.getRanksYes().replace("%rankfrom%", ezr.getRank())
						.replace("%rankprefix%", ezr.getPrefix())
						.replace("%rankupprefix%", r.getPrefix())
						.replace("%rankto%", r.getRank())
						.replace("%needed%", EZRanksLite.getDifference(plugin.getEco().getBalance(p), Double.parseDouble(r.getCost())))
						.replace("%cost%", EZRanksLite.fixMoney(cost)));
				}
				else {
					plugin.sms(p, plugin.getRanksNo().replace("%rankfrom%", ezr.getRank())
							.replace("%rankprefix%", ezr.getPrefix())
							.replace("%rankupprefix%", r.getPrefix())
							.replace("%rankto%", r.getRank())
							.replace("%needed%", EZRanksLite.getDifference(plugin.getEco().getBalance(p), Double.parseDouble(r.getCost())))
							.replace("%cost%", EZRanksLite.fixMoney(cost)));
				}
			}
			}
			
			
		}	
		
		if (plugin.getRanksFooter() != null) {
			for (String footer : plugin.getRanksFooter()) {
				if (footer.equalsIgnoreCase("none") || footer.equalsIgnoreCase("off")) {}
				else {
					plugin.sms(p, footer);
				}
			}
			}
		
		return true;
	}
	
}
