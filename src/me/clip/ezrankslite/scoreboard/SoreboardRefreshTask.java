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

import me.clip.ezrankslite.EZRanksLite;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SoreboardRefreshTask implements Runnable {

	private EZRanksLite plugin;
	
	private final String player;
	
	public SoreboardRefreshTask(EZRanksLite instance, String player) {
		plugin = instance;
		this.player = player;
	}
	
	@Override
	public void run() {		
		
		@SuppressWarnings("deprecation")
		Player p = Bukkit.getServer().getPlayer(player);
		
		if (p == null) {
			return;
		}
		
		plugin.getBoardHandler().updateScoreboard(p);
		
	}
}