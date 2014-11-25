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
package me.clip.ezrankslite.listeners;

import java.util.ArrayList;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.scoreboard.ScoreboardHandler;
import me.clip.ezrankslite.updater.Updater;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	private EZRanksLite plugin;

	public PlayerListener(EZRanksLite instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		
		if (p.hasPermission("ezranks.staff")) {
			if (EZRanksLite.staffOnline == null) {
				EZRanksLite.staffOnline = new ArrayList<String>();
			}
			if (!EZRanksLite.staffOnline.contains(p.getName())) {
				EZRanksLite.staffOnline.add(p.getName());
			}
		}

		if (p.hasPermission("ezranks.admin")) {
			
			if (plugin.checkUpdates() && plugin.getUpdater() != null) {
				
				if (Updater.updateAvailable()) {
					
					plugin.sms(p, "&aAn update is available for EZRanksLite (&fEZRanksLite "
									+ Updater.getLatestVersion() + "&a)");
					plugin.sms(p, "&aat &fhttp://bit.ly/1sljFdb");
				}

			}
		}

		if (!plugin.useScoreboard()) {
			return;
		}

		if (plugin.getSbOptions().getDisabledWorlds() != null
				&& plugin.getSbOptions().getDisabledWorlds()
						.contains(p.getLocation().getWorld().getName())) {
			return;
		}

		plugin.getBoardHandler().createScoreboard(p);

	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {

		if (!plugin.useScoreboard()) {
			return;
		}

		Player p = e.getPlayer();

		if (plugin.getSbOptions().getDisabledWorlds() != null
				&& plugin.getSbOptions().getDisabledWorlds()
						.contains(p.getLocation().getWorld().getName())) {
			plugin.getBoardHandler().removeScoreboard(p);
		} else {
			plugin.getBoardHandler().createScoreboard(p);
		}

	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		
		Player p = e.getPlayer();
		
		if (p.hasPermission("ezranks.staff")) {
			if (EZRanksLite.staffOnline != null 
				&& EZRanksLite.staffOnline.contains(p.getName())) {
				EZRanksLite.staffOnline.remove(p.getName());
			}
		}

		if (!plugin.useScoreboard()) {
			return;
		}

		plugin.getBoardHandler().removeScoreboard(p);
		
		if (ScoreboardHandler.staffToggled != null || !ScoreboardHandler.staffToggled.isEmpty()) {
			if (ScoreboardHandler.staffToggled.contains(p.getUniqueId().toString())) {
				ScoreboardHandler.staffToggled.remove(p.getUniqueId().toString());
			}
		}
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {

		Player p = e.getPlayer();
		
		if (p.hasPermission("ezranks.staff")) {
			if (EZRanksLite.staffOnline != null 
				&& EZRanksLite.staffOnline.contains(p.getName())) {
				EZRanksLite.staffOnline.remove(p.getName());
			}
		}
		
		if (!plugin.useScoreboard()) {
			return;
		}

		plugin.getBoardHandler().removeScoreboard(p);
		
		if (ScoreboardHandler.staffToggled != null || !ScoreboardHandler.staffToggled.isEmpty()) {
			if (ScoreboardHandler.staffToggled.contains(p.getUniqueId().toString())) {
				ScoreboardHandler.staffToggled.remove(p.getUniqueId().toString());
			}
		}
	}

}
