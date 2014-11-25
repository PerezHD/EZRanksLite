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

import java.util.HashMap;
import java.util.Map;

import me.clip.ezrankslite.EZRanksLite;

public class PlaceHolderHandler {

	EZRanksLite plugin;

	public PlaceHolderHandler(EZRanksLite instance) {
		plugin = instance;
	}

	private static HashMap<String, Map<String, String>> playerPlaceholders = new HashMap<String, Map<String, String>>();

	private static HashMap<String, String> globalPlaceholders = new HashMap<String, String>();

	public void setPlayerPlaceholder(String player, String identifier,
			String value) {
		if (!identifier.startsWith("%")) {
			identifier = "%" + identifier;
		}
		if (!identifier.endsWith("%")) {
			identifier = identifier + "%";
		}
		Map<String, String> current = playerPlaceholders.get(player);
		
		if (current == null) {
			current = new HashMap<String, String>();
		}
		current.put(identifier, value);
		playerPlaceholders.put(player, current);
	}

	public String getPlayerPlaceholders(String player, String msg) {
		if (playerPlaceholders != null && !playerPlaceholders.isEmpty()) {
			if (playerPlaceholders.containsKey(player)
					&& playerPlaceholders.get(player) != null) {
				Map<String, String> ph = playerPlaceholders.get(player);
				if (!ph.isEmpty()) {

					for (String identifier : ph.keySet()) {
						msg = msg.replace(identifier, ph.get(identifier));
					}
				}
			}
		}
		return msg;
	}

	public void setGlobalPlaceholder(String identifier, String value) {
		if (!identifier.startsWith("%")) {
			identifier = "%" + identifier;
		}
		if (!identifier.endsWith("%")) {
			identifier = identifier + "%";
		}
		if (globalPlaceholders == null) {
			globalPlaceholders = new HashMap<String, String>();
		}
		globalPlaceholders.put(identifier, value);
	}

	public String getGlobalPlaceholders(String msg) {
		if (globalPlaceholders != null && !globalPlaceholders.isEmpty()) {

			for (String identifier : globalPlaceholders.keySet()) {
				msg = msg.replace(identifier,
						globalPlaceholders.get(identifier));
			}

		}

		return msg;
	}

}
