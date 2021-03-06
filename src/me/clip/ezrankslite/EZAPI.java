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
package me.clip.ezrankslite;

import java.util.Collection;
import java.util.Iterator;

import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.rankdata.EZRank;
import me.clip.ezrankslite.rankdata.EZRankup;

import org.bukkit.entity.Player;

/**
 * EZRanksLite API class 
 * @author Ryan McCarthy
 */
public class EZAPI {

	private EZRanksLite plugin;
	
	/**
	 * EZAPI constructor
	 * @param instance EZRanksLite plugin
	 */
	public EZAPI(EZRanksLite instance) {
		plugin = instance;
	}
	
	/**
	 * get all server groups loaded by the permissions plugin through Vault
	 * @return list of groups that permissions plugin has loaded
	 */
	public String[] getServerGroups() {
		return plugin.getHooks().getServerGroups();
	}
	
	/**
	 * get the current permission rank of a player
	 * @param p player to get the rank for
	 * @return current permissions group of a player
	 */
	public String getCurrentRank(Player p) {
		return plugin.getHooks().getGroup(p);
	}
	
	/**
	 * get all permission groups attached to a player
	 * @param p Player to get groups for
	 * @return All permission groups a player is in
	 */
	public String[] getPermissionsGroups(Player p) {
		return plugin.getHooks().getGroups(p);
	}
	
	/**
	 * get a players raw economy balance from Vault
	 * @param p Player to get balance for
	 * @return player economy balance from Vault
	 */
	public double getEconBalance(Player p) {
		return plugin.getEco().getBalance(p);
	}
	
	/**
	 * get a players economy balance formatted by EZRanksLite
	 * @param p Player object to get balance for
	 * @return economy balance formatted to options set in EZRanksLite config
	 */
	public String getFormattedBalance(Player p) {
		double bal = getEconBalance(p);
		return EZRanksLite.fixMoney(bal);
	}
	
	/**
	 * get a players rankup rank name
	 * @param p Player to get the rankup name for
	 * @return Permissions group player can rankup to
	 */
	public String getRankupName(Player p) {
		if (getRankups(p) == null || getRankups(p).isEmpty()) {
			return "";
		}
		return getRankups(p).iterator().next().getRank();
	}
	
	/**
	 * get a players next rank prefix defined in the rankups.yml
	 * @param p Player to get the rankup prefix for
	 * @return empty String if no prefix is available, next rank prefix otherwise
	 */
	public String getRankupPrefix(Player p) {
		if (getRankups(p) == null || getRankups(p).isEmpty()) {
			return "";
		}
		return getRankups(p).iterator().next().getPrefix();
	}
	
	/**
	 * get a players cost to rankup
	 * @param p Player to get the rankup cost for
	 * @return rankup cost if player has a rankup available, 0 otherwise
	 */
	public double getRankupCost(Player p) {
		if (getRankups(p) == null || getRankups(p).isEmpty()) {
			return 0;
		}
		double cost = 0;
		String c = getRankups(p).iterator().next().getCost();
		if (c != null) {
			try {
				cost = Double.parseDouble(c);
			} catch (Exception e) {
			}
		}
		return cost;
	}
	
	/**
	 * get a players cost to rankup formatted by EZRanksLite
	 * @param p Player to get the rankup cost for
	 * @return formatted rankup cost if player has a rankup available, 0 otherwise
	 */
	public String getRankupCostFormatted(Player p) {
		return EZRanksLite.fixMoney(getRankupCost(p));
	}
	
	/**
	 * get a players rankup progress
	 * @param p Player to get rankup progress for
	 * @return progress percentage completed based on amount of money needed to rankup
	 */
	public int getRankupProgress(Player p) {
		
		
		if (getRankups(p) == null || getRankups(p).isEmpty()) {
			return 100;
		}
		
		double cost = 0;
		
		Iterator<EZRankup> iterator = getRankups(p).iterator();
		
		while (iterator.hasNext()) {
			String c = iterator.next().getCost();
			if (c != null) {
				try {
					cost = Double.parseDouble(c);
				} catch (Exception e) {
					cost = 0;
				}
			}
			else {
				cost = 0;
			}
		}
		
		cost = CostHandler.getMultiplier(p, cost);
		cost = CostHandler.getDiscount(p, cost);
		
		return plugin.getBoardHandler().getProgress(getEconBalance(p), String.valueOf(cost));
	}
	
	/**
	 * get the percentage complete comparing balance to a cost
	 * @param p Player to get rankup progress for
	 * @return progress percentage completed based on amount of money needed to rankup
	 */
	public int getProgress(double balance, double cost) {
		return plugin.getBoardHandler().getProgress(balance, String.valueOf(cost));
	}
	
	/**
	 * get a players rankup progress bar
	 * @param p Player to get rankup progress bar for
	 * @return progress bar based on money needed to rankup
	 */
	public String getRankupProgressBar(Player p) {
		return plugin.getBoardHandler().getProgressBar(getRankupProgress(p));
	}
	
	/**
	 * get a progress bar for a certain percentage completed
	 * @param progress int percentage completed
	 * @return progress bar based on progress completed
	 */
	public String getProgressBar(int progress) {
		return plugin.getBoardHandler().getProgressBar(progress);
	}
	
	/*
	 * EZRank and EZRankup methods
	 */

	/**
	 * Get a collection of all rankups for a players current rank
	 * @param p player to get the rankups for
	 * @return collection of EZRankup objects which the player currently has access to
	 * based on the permissions group they are currently in
	 * These objects hold information such as the commands that are executed for that specific
	 * rankup, the rank the rankup lets the player go to, the cost to rankup, and other information
	 * will return null if player does not have any rankups
	 */
	public Collection<EZRankup> getRankups(Player p) {
		String rank = getCurrentRank(p);
		if (plugin.getRankHandler().getRankData(rank) != null
				&& plugin.getRankHandler().getRankData(rank).hasRankups()) {
			return plugin.getRankHandler().getRankData(rank).getRankups();
		}
		return null;
	}
	
	/**
	 * get the first rankup a player has access to based on the main permissions group they are in
	 * @param p Player to get EZRankup object for
	 * @return null if player does not have any rankups available
	 */
	public EZRankup getNextRankup(Player p) {
		if (getRankups(p) == null || getRankups(p).isEmpty()) {
			return null;
		}
		return getRankups(p).iterator().next();
	}
	
	/**
	 * Get the EZRank data object that was created from the rankups.yml
	 * for a specific players rank
	 * @param p player to get the EZRank object for
	 * @return get the rankdata object that was created from the rankups.yml file 
	 * relative to the players current permission group
	 * This EZRank object holds all the options/rankups for that specific 
	 * permissions group
	 * will return null if the players current rank was not loaded/created from the rankups.yml
	 */
	public EZRank getRankData(Player p) {
		String rank = getCurrentRank(p);
		if (plugin.getRankHandler().getRankData(rank) != null) return plugin.getRankHandler().getRankData(rank);
		return null;
	}
	
	/**
	 * Get the EZRank data object that was created from the rankups.yml
	 * for a specific permissions group
	 * @param permissionsGroup rank to get the EZRank object for
	 * @return get the rankdata object that was created from the rankups.yml file 
	 * relative to the permission plugin group specified
	 * This EZRank object holds all the options/rankups for that specific 
	 * permissions group
	 * will return null if the permissionsGroup was not loaded/created from the rankups.yml
	 */
	public EZRank getRankData(String permissionsGroup) {
		if (plugin.getRankHandler().getRankData(permissionsGroup) != null)
            return plugin.getRankHandler().getRankData(permissionsGroup);
		return null;
	}
	
	/*
	 * scoreboard 
	 */
	
	/**
	 * set a custom placeholder for a specific player in the EZRanksLite scoreboard.
	 * if a custom placeholder is used for a specific player, it must be set for every player if the
	 * identifier is used in the ezrankslite scoreboard.
	 * Any player who does not have a value set will return a blank value
	 * @param playername player name to set the placeholder value for
	 * @param identifier for the ezrankslite scoreboard config to recognize where to put the value on the
	 * scoreboard. identifier must start and end with %
	 * @param value of identifier
	 */
	public void setPlayerPlaceholder(String playername, String identifier, String value) {
		plugin.getPlaceholders().setPlayerPlaceholder(playername, identifier, value);
	}
	
	@Deprecated
	public void setCustomPlaceholder(String playername, String identifier, String value) {
		plugin.getPlaceholders().setPlayerPlaceholder(playername, identifier, value);
	}
	
	/**
	 * set a custom placeholder for a specific player in the EZRanksLite scoreboard.
	 * if a custom placeholder is used for a specific player, it must be set for every player if the
	 * identifier is used in the ezrankslite scoreboard.
	 * Any player who does not have a value set will return a blank value
	 * @param p player to set the placeholder value for
	 * @param identifier for the ezrankslite scoreboard config to recognize where to put the value on the
	 * scoreboard. identifier must start and end with %
	 * @param value of identifier
	 */
	public void setPlayerPlaceholder(Player p, String identifier, String value) {
		plugin.getPlaceholders().setPlayerPlaceholder(p.getName(), identifier, value);
	}
	
	/**
	 * set a custom placeholder for all players in the EZRanksLite scoreboard.
	 * @param identifier for the ezrankslite scoreboard config to recognize where to put the value on the
	 * scoreboard. identifier must start and end with %
	 * @param value of identifier
	 */
	public void setGlobalPlaceholder(String identifier, String value) {
		plugin.getPlaceholders().setGlobalPlaceholder(identifier, value);
	}
	
	/**
	 * force an update to the EZRanksLite scoreboard for a specific player if the show scoreboard conditions are met
	 * inside of EZRanksLite (if scoreboard is enabled, if player is in enabled world, if player has scoreboard toggled on)
	 * this will update all placeholders
	 * @param p player to update the EZRanksLite scoreboard for
	 */
	public void updateScoreboard(Player p) {
		plugin.getBoardHandler().updateScoreboard(p);
	}
}
