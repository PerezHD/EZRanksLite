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
package me.clip.ezrankslite.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EZRankupEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private String pname;
	private String oldrank;
	private String newrank;
	private String rankupCost;
	private boolean cancelled;

	public EZRankupEvent(Player p, String oldrank, String newrank, String rankupCost) {

		this.pname = p.getName();
		this.oldrank = oldrank;
		this.newrank = newrank;
		this.rankupCost = rankupCost;

	}

	public String getOldRank() {
		return oldrank;
	}

	public String getNewRank() {
		return newrank;
	}
	
	public String getCost() {
		return rankupCost;
	}

	public String getPlayerName() {
		return pname;
	}

	@SuppressWarnings("deprecation")
	public Player getPlayer() throws NullPointerException {
		return Bukkit.getServer().getPlayer(pname);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}


}
