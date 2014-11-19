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