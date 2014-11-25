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
package me.clip.ezrankslite.scoreboard;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EZScoreboard {

	private Scoreboard scoreboard;

	private String title;
	private Map<String, Integer> scores;
	private List<Team> teams;

	public EZScoreboard(String title) {
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.title = title;
		this.scores = Maps.newLinkedHashMap();
		this.teams = Lists.newArrayList();

	}

	public void send(Player p) {
		p.setScoreboard(scoreboard);
	}

	public void blank() {
		setLine(" ");
	}

	public void setLine(String text) {
		setLine(text, null);
	}

	public void setLine(String text, Integer score) {
		Preconditions.checkArgument(text.length() < 48, "text cannot be over 48 characters in length");
		text = fixDuplicates(text);
		scores.put(text, score);
	}

	public void reset() {
		title = null;
		scores.clear();
		for (Team t : teams)
			t.unregister();
		teams.clear();
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	private String fixDuplicates(String text) {
		while (scores.containsKey(text))
			text += "§r";
		if (text.length() > 48)
			text = text.substring(0, 47);
		return text;
	}

	private Map.Entry<Team, String> createTeam(String text) {
		String result = "";
		if (text.length() <= 16)
			return new AbstractMap.SimpleEntry<>(null, text);
		Team team = scoreboard.registerNewTeam("text-"
				+ scoreboard.getTeams().size());
		Iterator<String> iterator = Splitter.fixedLength(16).split(text)
				.iterator();
		team.setPrefix(iterator.next());
		result = iterator.next();
		if (text.length() > 32)
			team.setSuffix(iterator.next());
		teams.add(team);
		return new AbstractMap.SimpleEntry<>(team, result);
	}

	@SuppressWarnings("deprecation")
	public void build() {
		Objective obj = scoreboard
				.registerNewObjective(
						(title.length() > 16 ? title.substring(0, 15) : title),
						"dummy");
		obj.setDisplayName(title);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		int index = scores.size();

		for (Map.Entry<String, Integer> text : scores.entrySet()) {
			Map.Entry<Team, String> team = createTeam(text.getKey());
			Integer score = text.getValue() != null ? text.getValue() : index;
			OfflinePlayer player = new EZOfflinePlayer(team.getValue());
			if (team.getKey() != null)
				team.getKey().addPlayer(player);
			obj.getScore(player).setScore(score);
			index -= 1;
		}
	}

}