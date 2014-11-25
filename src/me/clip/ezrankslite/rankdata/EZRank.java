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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class EZRank {
	
	private String rank;
	private HashMap<String, EZRankup> rankups;

	private boolean allowReset;
	
	private List<String> resetCommands;
	
	private String resetCost;
	
	private int rankOrder;
	
	private String prefix;
	
	private boolean lastRank;
	
	public EZRank(String rankName) {
		setRank(rankName);
	}
	
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public Collection<EZRankup> getRankups() {
		return rankups.values();
	}
	
	private Set<String> getRankupNames() {
		return rankups.keySet();
	}
	
	public boolean isRankup(String rankName) {
		if (!hasRankups()) {
			return false;
		}
		return getRankupNames().contains(rankName);
	}
	
	public EZRankup getRankup(String rankup) {
		if (!isRankup(rankup)) {
			return null;
		}
		return rankups.get(rankup);
	}
	
	public boolean hasRankups() {
		if (rankups == null || rankups.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public void addRankup(String rankName, EZRankup r) {
		if (rankups == null) {
			rankups = new HashMap<String, EZRankup>();
		}
		rankups.put(rankName, r);
	}
	
	public boolean removeRankup(String rankName) {
		if (isRankup(rankName)) {
			rankups.remove(rankName);
			return true;
		}
		return false;
	}
	

	public boolean allowReset() {
		return allowReset;
	}

	public boolean setAllowReset(boolean allowReset) {
		this.allowReset = allowReset;
		return allowReset;
	}

	public List<String> getResetCommands() {
		return resetCommands;
	}

	public void setResetCommands(List<String> resetCommands) {
		this.resetCommands = resetCommands;
	}

	public String getResetCost() {
		return resetCost;
	}

	public void setResetCost(String resetCost) {
		this.resetCost = resetCost;
	}

	public int getRankOrder() {
		return rankOrder;
	}

	public void setRankOrder(int rankOrder) {
		this.rankOrder = rankOrder;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isLastRank() {
		return lastRank;
	}

	public void setIsLastRank(boolean lastRank) {
		this.lastRank = lastRank;
	}

}
