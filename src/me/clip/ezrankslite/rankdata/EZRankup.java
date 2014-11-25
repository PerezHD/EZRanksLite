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

import java.util.List;

public class EZRankup {
	
	private boolean active;
	private boolean confirmToRank;
	private String rank;
	private String cost;
	private List<String> requirementMsg;
	private List<String> commands;
	private String prefix;
	
	public EZRankup(String rank) {
		setRank(rank);
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public List<String> getRequirementMsg() {
		return requirementMsg;
	}

	public void setRequirementMsg(List<String> requirementMsg) {
		this.requirementMsg = requirementMsg;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isConfirmToRank() {
		return confirmToRank;
	}

	public void setConfirmToRank(boolean confirmToRank) {
		this.confirmToRank = confirmToRank;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	

}
