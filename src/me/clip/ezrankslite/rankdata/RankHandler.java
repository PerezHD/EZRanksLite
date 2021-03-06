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
package me.clip.ezrankslite.rankdata;

import java.util.HashMap;
import java.util.Set;

import me.clip.ezrankslite.EZRanksLite;

public class RankHandler {
	
	EZRanksLite plugin;
	
	public RankHandler(EZRanksLite instance) {
		plugin = instance;
	}
	
	private static HashMap<String, EZRank> ranks;
	
	public void loadMap() {
		ranks = new HashMap<String, EZRank>();
	}
	
	public void putRankData(String rankName, EZRank rank) {
		if (ranks == null) {
			ranks = new HashMap<String, EZRank>();
		}
		ranks.put(rankName, rank);
	}
	
	public boolean isLoaded() {
		if (ranks == null || ranks.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public Set<String> getLoadedRanks() {
		if (isLoaded()) {
			return ranks.keySet();
		}
		return null;
	}
	
	public boolean hasRankData(String rank) {
		if (isLoaded() && ranks.containsKey(rank) && ranks.get(rank) != null) {
			return true;
		}
		return false;
	}
	
	public EZRank getRankData(String rank) {
		if (hasRankData(rank)) {
			return ranks.get(rank);
		}
		return null;
	}

}
