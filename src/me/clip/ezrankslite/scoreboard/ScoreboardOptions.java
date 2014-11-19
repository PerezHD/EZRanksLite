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
package me.clip.ezrankslite.scoreboard;

import java.util.List;

public class ScoreboardOptions {
	
	public ScoreboardOptions() {
	}
	
	private String title;

	private List<String> text;
	
	private String noRankups;
	
	private String rankup;
	
	private String pBarColor;
	private String pBarNeedsColor;
	
	private String pBarEndColor;
	
	private String pBarLeftChar;
	
	private String pBarChar;
	
	private String pBarRightChar;
	
	private List<String> disabledWorlds;
	
	private boolean useStaffScoreboard;
	
	private List<String> staffText;
	
	private String staffTitle;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}

	public String getNoRankups() {
		return noRankups;
	}

	public void setNoRankups(String noRankups) {
		this.noRankups = noRankups;
	}

	public String getRankup() {
		return rankup;
	}

	public void setRankup(String rankup) {
		this.rankup = rankup;
	}

	public String getpBarColor() {
		return pBarColor;
	}

	public void setpBarColor(String pBarColor) {
		this.pBarColor = pBarColor;
	}

	public String getpBarEndColor() {
		return pBarEndColor;
	}

	public void setpBarEndColor(String pBarEndColor) {
		this.pBarEndColor = pBarEndColor;
	}

	public List<String> getDisabledWorlds() {
		return disabledWorlds;
	}

	public void setDisabledWorlds(List<String> disabledWorlds) {
		this.disabledWorlds = disabledWorlds;
	}

	public boolean useStaffScoreboard() {
		return useStaffScoreboard;
	}

	public void setUseStaffScoreboard(boolean useStaffScoreboard) {
		this.useStaffScoreboard = useStaffScoreboard;
	}

	public List<String> getStaffText() {
		return staffText;
	}

	public void setStaffText(List<String> staffText) {
		this.staffText = staffText;
	}
	
	public String getStaffTitle() {
		return staffTitle;
	}

	public void setStaffTitle(String staffTitle) {
		this.staffTitle = staffTitle;
	}

	public String getpBarLeftChar() {
		return pBarLeftChar;
	}

	public void setpBarLeftChar(String pBarLeftChar) {
		this.pBarLeftChar = pBarLeftChar;
	}

	public String getpBarChar() {
		return pBarChar;
	}

	public void setpBarChar(String pBarChar) {
		this.pBarChar = pBarChar;
	}

	public String getpBarRightChar() {
		return pBarRightChar;
	}

	public void setpBarRightChar(String pBarRightChar) {
		this.pBarRightChar = pBarRightChar;
	}

	public String getpBarNeedsColor() {
		return pBarNeedsColor;
	}

	public void setpBarNeedsColor(String pBarNeedsColor) {
		this.pBarNeedsColor = pBarNeedsColor;
	}

}
