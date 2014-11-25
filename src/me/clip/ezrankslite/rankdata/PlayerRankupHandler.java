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
package me.clip.ezrankslite.rankdata;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.ezrankslite.EZRanksLite;

public class PlayerRankupHandler {
	
	EZRanksLite plugin;

	public PlayerRankupHandler(EZRanksLite instance) {
		plugin = instance;
	}
	
	/**
	 * reset a players rank if the EZRank object allows it
	 * by running the reset_commands defined in the rankups.yml
	 * for the specific base rank.
	 * This will also handle the economy transaction to take the money needed
	 * to reset
	 * @param p Player object to reset 
	 * @param baseRank EZRank object associated with the Player permission group
	 * @return true if reset is successful
	 */
	public boolean resetPlayer(final Player p, final EZRank baseRank) {
		
		if (!baseRank.allowReset()) {
			return false;
		}
		
		OfflinePlayer pl = p;
		
		double balance = plugin.getEco().getBalance(pl);
		double needed = Double.parseDouble(baseRank.getResetCost());
		
		if (balance < needed) {
			return false;
		}
		
		List<String> commands = baseRank.getResetCommands();
		
		if (commands == null || commands.isEmpty()) {
			
			plugin.debug(true, "There were no reset_commands for rank "+baseRank.getRank()+"! Players will not be reset!");
			plugin.sms(p, "&cThis reset is not setup correctly! Please contact an admin!");
			return false;
		}
		
		for (String cmd : commands) {
			
			if (cmd.startsWith("ezmsg") || cmd.startsWith("ezmessage")) {
				
				plugin.sms(p, cmd.replace("%rankfrom%", baseRank.getRank())
						         .replace("%player%", p.getName())
						         .replace("%world%", p.getWorld().getName())
						         .replace("%rankprefix%", baseRank.getPrefix())
						         .replace("%rankfrom%", baseRank.getRank())
						         .replace("%balance%", EZRanksLite.fixMoney(balance))
						         .replace("%cost%", EZRanksLite.fixMoney(needed))
						         .replace("ezmsg ", "")
						         .replace("ezmessage ", ""));
			} else if (cmd.startsWith("ezbroadcast") || cmd.startsWith("ezbcast")) {
				
				plugin.bcast(cmd.replace("%rankfrom%", baseRank.getRank())
				                .replace("%player%", p.getName())
						        .replace("%world%", p.getWorld().getName())
						        .replace("%rankprefix%", baseRank.getPrefix())
						        .replace("%rankfrom%", baseRank.getRank())
						        .replace("%balance%", EZRanksLite.fixMoney(balance))
						        .replace("%cost%", EZRanksLite.fixMoney(needed))
						        .replace("ezbroadcast ", "")
						        .replace("ezbcast ", ""));
			} else {
				
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), 
						cmd.replace("%rankfrom%", baseRank.getRank())
					       .replace("%player%", p.getName())
					       .replace("%world%", p.getWorld().getName())
					       .replace("%rankprefix%", baseRank.getPrefix())
					       .replace("%rankfrom%", baseRank.getRank())
					       .replace("%balance%", balance + "")
					       .replace("%cost%", needed + ""));	
			}
		}	
		
		plugin.getEco().withdrawMoney(needed, pl);
		
		if (plugin.useScoreboard() && plugin.getBoardHandler().hasScoreboard(p)) {
			
			plugin.getBoardHandler().updateScoreboard(p);
		}
		
		return true;
	}
	
	/**
	 * rank a player up by handling the economy transaction associated with the EZRankup object
	 * by running the rankup_commands defined in the rankups.yml
	 * for the specific rankup.
	 * This will also handle the economy transaction to take the money needed
	 * to rankup
	 * @param p Player object to rankup 
	 * @param baseRank EZRank object associated with the Player permission group
	 * @param rankup EZRankup object that holds the information related to the rank the player is ranking up to
	 * @param cost double value of the cost to rankup
	 * @return true if rankup is successful
	 */
	public boolean rankupPlayer(final Player p, final EZRank baseRank, final EZRankup rankup, final double cost) {
		
		List<String> commands = rankup.getCommands();
		
		if (commands == null || commands.isEmpty()) {
			
			plugin.debug(true, "There were no rankup_commands for rankup "+baseRank.getRank()+" to "+rankup.getRank()+"!"+
					"The player will not be ranked up!");
			return false;
		}
		
		OfflinePlayer pl = p;
		
		double balance = plugin.getEco().getBalance(pl);
		
		if (balance < cost) {
			return false;
		}
		
		for (String cmd : commands) {
			
			if (cmd.startsWith("ezmsg") || cmd.startsWith("ezmessage")) {
				
				plugin.sms(p, cmd.replace("%rankfrom%", baseRank.getRank())
						         .replace("%rankto%", rankup.getRank())
						         .replace("%player%", p.getName())
						         .replace("%rankprefix%", baseRank.getPrefix())
						         .replace("%rankupprefix%", rankup.getPrefix())
						         .replace("%world%", p.getWorld().getName())
						         .replace("%balance%", EZRanksLite.fixMoney(balance))
						         .replace("%cost%", EZRanksLite.fixMoney(cost))
						         .replace("ezmsg ", "")
						         .replace("ezmessage ", ""));
				
			} else if (cmd.startsWith("ezbroadcast") || cmd.startsWith("ezbcast")) {
				
				plugin.bcast(cmd.replace("%rankfrom%", baseRank.getRank())
							    .replace("%rankto%", rankup.getRank())
							    .replace("%player%", p.getName())
							    .replace("%rankprefix%", baseRank.getPrefix())
							    .replace("%rankupprefix%", rankup.getPrefix())
							    .replace("%world%", p.getWorld().getName())
							    .replace("%balance%", EZRanksLite.fixMoney(balance))
							    .replace("%cost%", EZRanksLite.fixMoney(cost))
							    .replace("ezbroadcast ", "")
							    .replace("ezbcast ", ""));
			}
			else if (cmd.startsWith("ezeffect") || cmd.startsWith("ezeffects")) {
				
				if (!cmd.contains(" ")) {
					continue;
				}
				
				String[] args = cmd.split(" ");
				
				if (args.length < 2) {
					continue;
				}
				
				String type = args[1];
				
				if (type.equalsIgnoreCase("firework") || type.equalsIgnoreCase("fireworks")) {
					plugin.getEffectsHandler().fireworks(p.getLocation());
				}
				else if (type.equalsIgnoreCase("spawner") || type.equalsIgnoreCase("flames")) {
					plugin.getEffectsHandler().flames(p.getLocation());
				}
				else if (type.equalsIgnoreCase("smoke") || type.equalsIgnoreCase("smokeeffect")) {
					plugin.getEffectsHandler().smoke(p.getLocation());
				}
				else if (type.equalsIgnoreCase("ender") || type.equalsIgnoreCase("endereffect")) {
					plugin.getEffectsHandler().ender(p.getLocation());
				}	
				else if (type.equalsIgnoreCase("potion") || type.equalsIgnoreCase("potionbreak")) {
					plugin.getEffectsHandler().potionBreak(p.getLocation());
				}
				else if (type.equalsIgnoreCase("explosion") || type.equalsIgnoreCase("explosioneffect") || type.equalsIgnoreCase("tnt")) {
					plugin.getEffectsHandler().explosion(p.getLocation());
				}
				
			} else {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), 
						cmd.replace("%rankfrom%", baseRank.getRank())
					       .replace("%rankto%", rankup.getRank())
					       .replace("%player%", p.getName())
					       .replace("%world%", p.getWorld().getName())
					       .replace("%balance%", balance + "")
					       .replace("%cost%", EZRanksLite.fixMoney(cost)));
			}
		}	
		
		plugin.getEco().withdrawMoney(cost, pl);
		
		if (plugin.useScoreboard() && plugin.getBoardHandler().hasScoreboard(p)) {
				plugin.getBoardHandler().updateScoreboard(p);
		}
		
		return true;
	}

}
