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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.rankdata.EZRankup;
import me.clip.voteparty.VotePartyAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;



public class ScoreboardHandler {

	private EZRanksLite plugin;

	public ScoreboardHandler(EZRanksLite instance) {
		plugin = instance;
	}

	protected HashMap<String, EZScoreboard> boards = new HashMap<String, EZScoreboard>();
	
	public static List<String> staffToggled = new ArrayList<String>();
	
	public void clearBoards() {
		boards = null;
	}

	public boolean hasScoreboard(Player p) {
		return boards.containsKey(p.getName())
				&& boards.get(p.getName()) != null;
	}

	public boolean hasScoreboard(String p) {
		return boards.containsKey(p) && boards.get(p) != null;
	}

	public EZScoreboard getEZBoard(String player) {
		if (hasScoreboard(player)) {
			return boards.get(player);
		}
		return null;
	}

	public void removeScoreboard(Player p) {
		if (hasScoreboard(p)) {
			EZScoreboard board = boards.get(p.getName());
			board.reset();
			boards.remove(p.getName());
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}

	public void updateScoreboard(Player p) {
		if (plugin.useScoreboard()) {
		if (plugin.getSbOptions().getDisabledWorlds() != null && 
				plugin.getSbOptions().getDisabledWorlds().contains(p.getLocation().getWorld().getName())) {
			return;
		}
		if (hasScoreboard(p)) {
			createScoreboard(p);
		}
		}
	}

	public int getProgress(double balance, String cost) {

		float bal = (float) balance;

		float c = Float.parseFloat(cost);

	    float percent = (100 * bal) / c;
	    
	    int progress = (int) Math.floor(percent);

	    if (progress >= 100) {
	    	return 100;
	    }
	    else if (progress < 0) {
	    	return 0;
	    }
		return progress;
		
	}
	
	@Deprecated
	public String getProgressBar(int progress, String color1, String color2) {
		return getProgressBar(progress);
	}

	public String getProgressBar(int progress) {
		String endColor = plugin.getSbOptions().getpBarEndColor();
		String barColor = plugin.getSbOptions().getpBarColor();
		String l = plugin.getSbOptions().getpBarLeftChar();
		String b = plugin.getSbOptions().getpBarChar();
		String r = plugin.getSbOptions().getpBarRightChar();
		String needColor = plugin.getSbOptions().getpBarNeedsColor();
		if (progress >= 10 && progress <= 19) {
			return endColor+l+barColor+b+needColor+b+b+b+b+b+b+b+b+endColor+r;
		} else if (progress >= 20 && progress <= 29) {
			return endColor+l+barColor+b+b+needColor+b+b+b+b+b+b+b+endColor+r;
		} else if (progress >= 30 && progress <= 39) {
			return endColor+l+barColor+b+b+b+needColor+b+b+b+b+b+b+endColor+r;
		} else if (progress >= 40 && progress <= 49) {
			return endColor+l+barColor+b+b+b+b+needColor+b+b+b+b+b+endColor+r;
		} else if (progress >= 50 && progress <= 59) {
			return endColor+l+barColor+b+b+b+b+b+needColor+b+b+b+b+endColor+r;
		} else if (progress >= 60 && progress <= 69) {
			return endColor+l+barColor+b+b+b+b+b+b+needColor+b+b+b+endColor+r;
		} else if (progress >= 70 && progress <= 79) {
			return endColor+l+barColor+b+b+b+b+b+b+b+needColor+b+b+endColor+r;
		} else if (progress >= 80 && progress <= 89) {
			return endColor+l+barColor+b+b+b+b+b+b+b+b+needColor+b+endColor+r;
		} else if (progress >= 90 && progress <= 99) {
			return endColor+l+barColor+b+b+b+b+b+b+b+b+b+endColor+r;
		} else if (progress >= 100) {
			return plugin.getSbOptions().getRankup();
		} else {
			return endColor+l+needColor+b+b+b+b+b+b+b+b+b+endColor+r;
		}
	}
	
	private void createStaffScoreboard(Player p) {
		
		ScoreboardOptions options = plugin.getSbOptions();

		if (options == null) {
			options = plugin.loadSBOptions();
		}
		
		String name = p.getName();
		
		String rank = plugin.getVault().getGroup(p);
		
		String displayName = p.getDisplayName();
		
		String online = String.valueOf(Bukkit.getServer().getOnlinePlayers().length);
		
		String max = String.valueOf(Bukkit.getServer().getMaxPlayers());
		
		int staffOnline = EZRanksLite.staffOnline.size();
		
		int playersOnline = Bukkit.getServer().getOnlinePlayers().length-staffOnline;
		
		
		String op = String.valueOf(p.isOp());
		
		String gm = String.valueOf(p.getGameMode().name());
		
		String flySpeed = String.valueOf(p.getFlySpeed());
		
		String entities = String.valueOf(p.getWorld().getEntities().size());
		
		String world = String.valueOf(p.getWorld().getName());
		
		String loadedWorlds = String.valueOf(Bukkit.getServer().getWorlds().size());
		
		String tps = "20"; //lol
		
		if (plugin.getEssentials() != null) {
			
			int tp = (int) plugin.getEssentials().getTimer().getAverageTPS();
			
			tps = String.valueOf(tp);
			
			if (plugin.getEssentials().getUser(p).getNickname() != null) {
				name = String.valueOf(plugin.getEssentials().getUser(p).getNickname());
			}
		}
		
		String votes = "0";
		String votesReceived = "0";
		String totalVotesNeeded = "0";
		
		if (EZRanksLite.useVoteParty) {
			votes = String.valueOf(VotePartyAPI.getCurrentVoteCounter());
			votesReceived = String.valueOf(VotePartyAPI.getVotes());
			totalVotesNeeded = String.valueOf(VotePartyAPI.getTotalVotesNeeded());
		}

		
		String title = options.getStaffTitle().replace("%player%", name)
				.replace("%displayname%", displayName)
				.replace("%rank%", rank)
				.replace("%online%", online)
				.replace("%onlinemax%", max)
				.replace("%onlinestaff%", String.valueOf(staffOnline))
				.replace("%onlineplayers%", String.valueOf(playersOnline))
				.replace("%tps%", tps)
				.replace("%op%", op)
				.replace("%gamemode%", gm)
				.replace("%flyspeed%", flySpeed)
				.replace("%entities%", entities)
				.replace("%world%", world)
				.replace("%loadedworlds%", loadedWorlds)
				.replace("%votes%", votes)
				.replace("%votesreceived%", votesReceived)
				.replace("%votesneeded%", totalVotesNeeded);
		
		title = plugin.getPlaceholders().getGlobalPlaceholders(title);
		title = plugin.getPlaceholders().getPlayerPlaceholders(name, title);

		EZScoreboard sb = new EZScoreboard(
				ChatColor.translateAlternateColorCodes('&', title));
		
		for (String s : options.getStaffText()) {

			if (s.equalsIgnoreCase("blank")) {

				sb.blank();

			} else {
				String send = s.replace("%player%", name)
						.replace("%displayname%", displayName)
						.replace("%rank%", rank)
						.replace("%online%", online)
						.replace("%onlinemax%", max)
						.replace("%onlinestaff%", String.valueOf(staffOnline))
						.replace("%onlineplayers%", String.valueOf(playersOnline))
						.replace("%tps%", tps)
						.replace("%op%", op)
						.replace("%gamemode%", gm)
						.replace("%flyspeed%", flySpeed)
						.replace("%entities%", entities)
						.replace("%world%", world)
						.replace("%loadedworlds%", loadedWorlds)
						.replace("%votes%", votes)
						.replace("%votesreceived%", votesReceived)
						.replace("%votesneeded%", totalVotesNeeded);

				send = plugin.getPlaceholders().getGlobalPlaceholders(send);
				send = plugin.getPlaceholders().getPlayerPlaceholders(name, send);

				sb.setLine(ChatColor.translateAlternateColorCodes('&', send));

			}

		}
		
		
		sb.build();
		sb.send(p);
		boards.put(p.getName(), sb);
		return;
		
		
	}

	public void createScoreboard(Player p) {
		
		ScoreboardOptions options = plugin.getSbOptions();

		if (options == null) {
			options = plugin.loadSBOptions();
		}
		
		if (options.useStaffScoreboard() 
				&& staffToggled != null 
				&& staffToggled.contains(p.getUniqueId().toString())) {
			createStaffScoreboard(p);
			return;
		}
		
		int online = Bukkit.getServer().getOnlinePlayers().length;

		OfflinePlayer pl = p;

		String name = p.getName();

		String rank = plugin.getHooks().getGroup(p);

		String bal = EZRanksLite.fixMoney(plugin.getEco().getBalance(pl));

		String nextRank = options.getNoRankups();

		String cost = "0";

		String votes = "0";
		String votesReceived = "0";
		String totalVotesNeeded = "0";

		int pro = 0;

		String progress = "0";

		String progressBar = "";
		
		String rankupPrefix = "";
		
		String rankPrefix = "";
		
		String difference = "0";

		if (EZRanksLite.useVoteParty) {
			votes = String.valueOf(VotePartyAPI.getCurrentVoteCounter());
			votesReceived = String.valueOf(VotePartyAPI.getVotes());
			totalVotesNeeded = String.valueOf(VotePartyAPI.getTotalVotesNeeded());
		}

		if (plugin.getRankHandler().hasRankData(rank)
				&& plugin.getRankHandler().getRankData(rank).hasRankups()) {
			
			rankPrefix = plugin.getRankHandler().getRankData(rank).getPrefix();
			
			for (EZRankup r : plugin.getRankHandler().getRankData(rank)
					.getRankups()) {
				
				nextRank = r.getRank();
				
				double needed = Double.parseDouble(r.getCost());
				
				needed = CostHandler.getMultiplier(p, needed);
				
				needed = CostHandler.getDiscount(p, needed);
				
				cost = EZRanksLite.fixMoney(needed);
				pro = getProgress(plugin.getEco().getBalance(pl), String.valueOf(needed));
				rankupPrefix = r.getPrefix();
				difference = EZRanksLite.getDifference(plugin.getEco().getBalance(pl), needed);
				
				if (pro == 100) {
					progress = pro + "%";
					progressBar = plugin.getSbOptions().getRankup();
				} else {
					progress = pro + "%";
					progressBar = getProgressBar(pro, options.getpBarColor(), options.getpBarEndColor());
				}

			}
		}
		
		String displayName = p.getDisplayName();
		
		String max = String.valueOf(Bukkit.getServer().getMaxPlayers());
		
		int staffOnline = EZRanksLite.staffOnline.size();
		
		int playersOnline = Bukkit.getServer().getOnlinePlayers().length-staffOnline;
		
		String world = String.valueOf(p.getWorld().getName());
		
		String tps = "20";
		
		if (plugin.getEssentials() != null) {
			
			int tp = (int) plugin.getEssentials().getTimer().getAverageTPS();
			
			tps = String.valueOf(tp);
			
			
			
			if (plugin.getEssentials().getUser(p).getNickname() != null) {
				name = String.valueOf(plugin.getEssentials().getUser(p).getNickname());
			}
		}

		String title = options.getTitle().replace("%player%", name)
				.replace("%displayname%", displayName)
				.replace("%onlinemax%", max)
				.replace("%onlinestaff%", String.valueOf(staffOnline))
				.replace("%onlineplayers%", String.valueOf(playersOnline))
				.replace("%tps%", tps)
				.replace("%world%", world)
				.replace("%rankfrom%", rank)
				.replace("%currentrank%", rank)
				.replace("%rankto%", nextRank)
				.replace("%rankup%", nextRank)
				.replace("%cost%", cost)
				.replace("%rankupcost%", cost)
				.replace("%balance%", bal)
				.replace("%bal%", bal)
				.replace("%online%", online + "")
				.replace("%progress%", progress)
				.replace("%progressbar%", progressBar)
				.replace("%votes%", votes)
				.replace("%votesreceived%", votesReceived)
				.replace("%votesneeded%", totalVotesNeeded)
				.replace("%rankprefix%", rankPrefix)
				.replace("%rankupprefix%", rankupPrefix)
				.replace("%difference%", difference)
				.replace("%needed%", difference);
		title = plugin.getPlaceholders().getGlobalPlaceholders(title);
		title = plugin.getPlaceholders().getPlayerPlaceholders(name, title);
		

		EZScoreboard sb = new EZScoreboard(
				ChatColor.translateAlternateColorCodes('&', title));

		for (String s : options.getText()) {

			if (s.equalsIgnoreCase("blank")) {

				sb.blank();

			} else {
				String send = s.replace("%player%", name)
						.replace("%displayname%", displayName)
						.replace("%onlinemax%", max)
						.replace("%onlinestaff%", String.valueOf(staffOnline))
						.replace("%onlineplayers%", String.valueOf(playersOnline))
						.replace("%tps%", tps)
						.replace("%world%", world)
						.replace("%rankfrom%", rank)
						.replace("%currentrank%", rank)
						.replace("%rankto%", nextRank)
						.replace("%rankup%", nextRank)
						.replace("%cost%", cost)
						.replace("%rankupcost%", cost)
						.replace("%balance%", bal)
						.replace("%bal%", bal)
						.replace("%online%", online + "")
						.replace("%progress%", progress)
						.replace("%progressbar%", progressBar)
						.replace("%votes%", votes)
						.replace("%votesreceived%", votesReceived)
						.replace("%votesneeded%", totalVotesNeeded)
						.replace("%rankprefix%", rankPrefix)
						.replace("%rankupprefix%", rankupPrefix)
						.replace("%difference%", difference)
						.replace("%needed%", difference);

				send = plugin.getPlaceholders().getGlobalPlaceholders(send);
				send = plugin.getPlaceholders().getPlayerPlaceholders(name, send);

				sb.setLine(ChatColor.translateAlternateColorCodes('&', send));

			}

		}

		sb.build();
		sb.send(p);
		boards.put(p.getName(), sb);
	}

}
