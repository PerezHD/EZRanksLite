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
package me.clip.ezrankslite.hooks;

import org.bukkit.entity.Player;

import src.john01dav.sqlperms.SQLPerms;

import me.clip.ezrankslite.EZRanksLite;

public class Hooks {
	
	
	EZRanksLite plugin;
	
	public Hooks(EZRanksLite i) {
		plugin = i;
	}
	
	public String getGroup(Player p) {
		if (plugin.useSQLPerms()) {
			return SQLPerms.instance.getRankManager().initRank(p);
		}
		else {
			return plugin.getVault().getGroup(p);
		}
	}

	public String[] getGroups(Player p) {
		if (plugin.useSQLPerms()) {
			return new String[] {SQLPerms.instance.getRankManager().initRank(p)};
		}
		else {
			return plugin.getVault().getGroups(p);
		}
	}
	
	public boolean hasPerm(Player p, String perm) {
		if (plugin.useSQLPerms()) {
			return p.hasPermission(perm);
		}
		else {
			return plugin.getVault().hasPerm(p, perm);
		}
	}
	
	public String[] getServerGroups() {
		if (plugin.useSQLPerms()) {
			return new String[] {""};
		}
		else {
			return plugin.getVault().getServerGroups();
		}
	}
	
	public boolean isValidServerGroup(String group) {
		if (plugin.useSQLPerms()) {
			return true;
		}
		else {
			return plugin.getVault().isValidGroup(group);
		}
	}

}
