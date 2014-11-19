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
