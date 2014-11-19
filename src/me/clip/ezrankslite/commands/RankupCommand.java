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
package me.clip.ezrankslite.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.Lang;
import me.clip.ezrankslite.events.EZRankupEvent;
import me.clip.ezrankslite.events.EZResetEvent;
import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.rankdata.EZRank;
import me.clip.ezrankslite.rankdata.EZRankup;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand implements CommandExecutor {

	private EZRanksLite plugin;

	public RankupCommand(EZRanksLite instance) {
		plugin = instance;
	}

	// reset confirmation list <playername>
	private static List<String> reset = new ArrayList<String>();
	// rankup confirmation map <playername, rankname>
	private static HashMap<String, String> wtc = new HashMap<String, String>();

	private static List<String> cooldown = new ArrayList<String>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("You need to be a player to rankup dummy!");
			return true;
		}

		Player p = (Player) sender;

		String rank = plugin.getHooks().getGroup(p);

		if (!plugin.getRankHandler().hasRankData(rank)) {
			plugin.sms(p, Lang.RANKUP_NO_RANKUPS_AVAILABLE
					.getConfigValue(new String[] { rank }));
			return true;
		}

		EZRank ezrank = plugin.getRankHandler().getRankData(rank);

		if (ezrank == null) {
			plugin.sms(p, Lang.RANKUP_NO_RANKUPS_AVAILABLE
					.getConfigValue(new String[] { rank }));
			return true;
		}

		if (args.length == 0) {
			
			if (plugin.useRankupCooldown()) {
				if (!p.hasPermission("ezadmin.cooldown.bypass")) {
					if (cooldown.contains(p.getName())) {
						plugin.sms(p, Lang.RANKUP_ON_COOLDOWN
								.getConfigValue(new String[] { plugin
										.getRankupCooldownTime() + "" }));
						return true;
					}
				}
			}
			
			if (ezrank.isLastRank()) {
				plugin.sms(p, Lang.RANKUP_LAST_RANK
						.getConfigValue(new String[] { rank }));
				return true;
			}
			
			
			if (!ezrank.hasRankups()) {
				plugin.sms(p, Lang.RANKUP_NO_RANKUPS_AVAILABLE
						.getConfigValue(new String[] { rank }));
				return true;
			}

			if (ezrank.getRankups().size() == 1) {

				EZRankup ru = ezrank.getRankups().iterator().next();

				if (!ru.isActive()) {
					plugin.sms(
							p,
							Lang.RANKUP_DISABLED.getConfigValue(new String[] {
									ru.getRank(), rank, ezrank.getPrefix() }));
					return true;
				}

				double balance = plugin.getEco().getBalance(p);

				double needed = Double.parseDouble(ru.getCost());

				needed = CostHandler.getMultiplier(p, needed);

				needed = CostHandler.getDiscount(p, needed);

				if (balance < needed) {
					for (String msg : ru.getRequirementMsg()) {
						plugin.sms(p, msg.replace("%rankfrom%", ezrank.getRank())
									   	 .replace("%rankto%", ru.getRank())
										 .replace("%rankprefix%", ezrank.getPrefix())
										 .replace("%rankupprefix%", ru.getPrefix())
										 .replace("%player%", p.getName())
										 .replace("%progress%", plugin.getBoardHandler()
												                      .getProgress(balance, String.valueOf(needed)) + "")
										 .replace("%progressbar%", plugin.getBoardHandler().getProgressBar(plugin.getBoardHandler()
																	     .getProgress(balance, String.valueOf(needed))))
										.replace("%difference%", EZRanksLite.getDifference(balance, needed))
										.replace("%costdifference%", EZRanksLite.getDifference(balance, needed))
										.replace("%world%", p.getWorld().getName())
										.replace("%balance%", EZRanksLite.fixMoney(balance))
										.replace("%cost%", EZRanksLite.fixMoney(needed)));
					}
					return true;
				}

				if (ru.isConfirmToRank()) {

					if (!wtc.containsKey(p.getName())) {
						wtc.put(p.getName(), ru.getRank());
						plugin.sms(p, Lang.RANKUP_CONFIRMATION
								.getConfigValue(new String[] {
										ru.getRank(),
										EZRanksLite.fixMoney(needed),
										ru.getPrefix() }));

						final String plname = p.getName();
						Bukkit.getScheduler().scheduleSyncDelayedTask(
								this.plugin, new Runnable() {
									public void run() {
										if (wtc.containsKey(plname)) {
											wtc.remove(plname);
										}
									}
								}, 20 * 15);
						return true;
					}

					String confirmRU = wtc.get(p.getName());

					if (!confirmRU.equals(ru.getRank())) {
						plugin.sms(p, Lang.RANKUP_CONFIRMATION_INCORRECT_RANKUP
								.getConfigValue(new String[] { confirmRU,
										ru.getRank(), ru.getPrefix() }));
						return true;
					}

					wtc.remove(p.getName());
				}
				
				//player has enough money and they have confirmed the rankup if one is available
				
				EZRankupEvent rankupEvent = new EZRankupEvent(p, ezrank.getRank(), ru.getRank(), String.valueOf(needed));
				Bukkit.getServer().getPluginManager().callEvent(rankupEvent);
				
				if (rankupEvent.isCancelled()) {
					plugin.debug(false, p.getName()+" attempted to rankup from "+ezrank.getRank()+" to " + ru.getRank());
					plugin.debug(false, "but the rankup event was cancelled by another plugin!");
					return true;
				}

				plugin.getPlayerhandler().rankupPlayer(p, ezrank, ru, needed);

				if (plugin.useRankupCooldown()) {

					if (!p.hasPermission("ezadmin.cooldown.bypass")) {

						cooldown.add(p.getName());
						final String plname = p.getName();
						final int time = plugin.getRankupCooldownTime();
						Bukkit.getScheduler().scheduleSyncDelayedTask(
								this.plugin, new Runnable() {
									public void run() {
										if (cooldown.contains(plname)) {
											cooldown.remove(plname);
										}
									}
								}, (20 * time));

					}
				}

				plugin.debug(false,
						p.getName() + " ranked up from " + ezrank.getRank()
								+ " to " + ru.getRank());

			} else {
				plugin.sms(p, Lang.RANKUP_MULTIPLE_RANKUPS
						.getConfigValue(new String[] {
								ezrank.getRankups().size() + "", rank }));
				for (EZRankup ru : ezrank.getRankups()) {
					double needed = Double.parseDouble(ru.getCost());

					needed = CostHandler.getMultiplier(p, needed);

					needed = CostHandler.getDiscount(p, needed);
					plugin.sms(p, Lang.RANKUP_MULTIPLE_RANKUPS_LIST
							.getConfigValue(new String[] {
									ru.getRank(),
									EZRanksLite.fixMoney(needed),
									ru.getPrefix() }));
				}
			}
			return true;
		} else if (args.length > 0) {
			if (args[0].equalsIgnoreCase("help")) {
				plugin.sms(p,
						Lang.HELP_HEADER.getConfigValue(new String[] { plugin
								.getServername() }));
				plugin.sms(p, Lang.HELP_RANKUP.getConfigValue(null));
				if (plugin.useRanksCommand()) {
					plugin.sms(p, Lang.HELP_RANKS.getConfigValue(null));
				}
				if (ezrank.allowReset()) {
					plugin.sms(p, Lang.HELP_RANK_RESET.getConfigValue(null));
				}
				if (plugin.useScoreboard()) {
					plugin.sms(p,
							Lang.HELP_SCOREBOARD_TOGGLE.getConfigValue(null));
					plugin.sms(p,
							Lang.HELP_SCOREBOARD_REFRESH.getConfigValue(null));
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("reset")) {
				
				if (!ezrank.allowReset()) {
					plugin.sms(p, Lang.RESET_NOT_ALLOWED.getConfigValue(null));
					return true;
				}
				
				if (plugin.useRankupCooldown()) {
					if (!p.hasPermission("ezadmin.cooldown.bypass")) {
						if (cooldown.contains(p.getName())) {
							plugin.sms(p, Lang.RANKUP_ON_COOLDOWN
									.getConfigValue(new String[] { plugin
											.getRankupCooldownTime() + "" }));
							return true;
						}
					}
				}

				double needed = Double.parseDouble(ezrank.getResetCost());
				double has = plugin.getEco().getBalance(p);
				if (has < needed) {
					plugin.sms(p, Lang.RESET_NOT_ENOUGH_MONEY
							.getConfigValue(new String[] {
									EZRanksLite.fixMoney(needed),
									EZRanksLite.fixMoney(has),
									ezrank.getRank(), ezrank.getPrefix() }));
					return true;
				}

				if (!reset.contains(p.getName())) {
					reset.add(p.getName());
					if (needed == 0) {
						plugin.sms(p, Lang.RESET_CONFIRMATION_FREE
								.getConfigValue(new String[] {
										ezrank.getRank(), ezrank.getPrefix() }));

					} else {
						plugin.sms(p, Lang.RESET_CONFIRMATION_COST
								.getConfigValue(new String[] {
										EZRanksLite.fixMoney(needed),
										ezrank.getRank(), ezrank.getPrefix() }));
					}

					final String plname = p.getName();
					Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin,
							new Runnable() {
								public void run() {
									if (reset.contains(plname)) {
										reset.remove(plname);
									}
								}
							}, 20 * 15);
					return true;
				}

				reset.remove(p.getName());
				
				EZResetEvent resetEvent = new EZResetEvent(p, ezrank.getRank(), ezrank.getResetCost());
				Bukkit.getServer().getPluginManager().callEvent(resetEvent);

				if (resetEvent.isCancelled()) {
					plugin.debug(false, p.getName() + " attempted to reset from " + ezrank.getRank());
					plugin.debug(false, "but the reset event was cancelled by another plugin!");
					return true;
				}
				
				plugin.getPlayerhandler().resetPlayer(p, ezrank);
				
				plugin.debug(false, p.getName() + " reset their rank from "+ezrank.getRank());
				
				if (plugin.useRankupCooldown()) {
					if (!p.hasPermission("ezadmin.cooldown.bypass")) {
						cooldown.add(p.getName());
						plugin.debug(false, p.getName()+" added to rankup cooldown");
						final String plname = p.getName();
						final int time = plugin.getRankupCooldownTime();
						Bukkit.getScheduler().scheduleSyncDelayedTask(
								this.plugin, new Runnable() {
									public void run() {
										if (cooldown.contains(plname)) {
											cooldown.remove(plname);
											plugin.debug(false, plname+" removed from rankup cooldown");
										}
									}
								}, (20 * time));
					}
				}

				return true;
			}
			
			if (plugin.useRankupCooldown()) {
				if (!p.hasPermission("ezadmin.cooldown.bypass")) {
					if (cooldown.contains(p.getName())) {
						plugin.sms(p, Lang.RANKUP_ON_COOLDOWN
								.getConfigValue(new String[] { plugin
										.getRankupCooldownTime() + "" }));
						return true;
					}
				}
			}
			
			if (ezrank.isLastRank()) {
				plugin.sms(p, Lang.RANKUP_LAST_RANK
						.getConfigValue(new String[] { rank }));
				return true;
			}
			
			if (!ezrank.hasRankups()) {
				plugin.sms(p, Lang.RANKUP_NO_RANKUPS_AVAILABLE
						.getConfigValue(new String[] { rank }));
				return true;
			}

			// rankup <rank>
			String rankto = args[0];

			EZRankup rankup = null;

			for (EZRankup ru : ezrank.getRankups()) {
				if (ru.getRank().equalsIgnoreCase(rankto)) {
					rankto = ru.getRank();
					rankup = ru;
				}
			}

			if (rankup == null) {
				plugin.sms(p, Lang.RANKUP_INCORRECT_RANK_ARGUMENT
						.getConfigValue(new String[] { rankto,
								ezrank.getRank(), ezrank.getPrefix()

						}));
				return true;
			}
			if (!rankup.isActive()) {
				plugin.sms(
						p,
						Lang.RANKUP_DISABLED.getConfigValue(new String[] {
								rankup.getRank(), rank, rankup.getPrefix(),
								ezrank.getPrefix() }));
				return true;
			}

			double balance = plugin.getEco().getBalance(p);
			double needed = Double.parseDouble(rankup.getCost());

			needed = CostHandler.getMultiplier(p, needed);

			needed = CostHandler.getDiscount(p, needed);

			if (balance < needed) {
				for (String msg : rankup.getRequirementMsg()) {
					plugin.sms(
							p,
							msg.replace("%rankfrom%", ezrank.getRank())
									.replace("%rankto%", rankup.getRank())
									.replace("%rankprefix%", ezrank.getPrefix())
									.replace("%rankupprefix%",
											rankup.getPrefix())
									.replace("%player%", p.getName())
									.replace("%progress%", plugin.getBoardHandler().getProgress(balance, String.valueOf(needed))+"")
									.replace("%progressbar%", plugin.getBoardHandler().getProgressBar(plugin.getBoardHandler().getProgress(balance, String.valueOf(needed))))
									.replace("%difference%", EZRanksLite.getDifference(balance, needed))
									.replace(
											"%costdifference%",
											EZRanksLite.getDifference(balance,
													needed))
									.replace("%world%", p.getWorld().getName())
									.replace(
											"%balance%",
											EZRanksLite.fixMoney(balance))
									.replace(
											"%cost%",
											EZRanksLite.fixMoney(needed)));
				}
				return true;
			}

			if (rankup.isConfirmToRank()) {
				if (!wtc.containsKey(p.getName())) {
					wtc.put(p.getName(), rankup.getRank());

					plugin.sms(p, Lang.RANKUP_CONFIRMATION_MULTIPLE_RANKUPS
							.getConfigValue(new String[] {
									rankup.getRank(),
									EZRanksLite.fixMoney(needed),
									ezrank.getRank(), ezrank.getPrefix(),
									rankup.getPrefix() }));

					final String plName = p.getName();
					Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin,
							new Runnable() {
								public void run() {
									if (wtc.containsKey(plName)) {
										wtc.remove(plName);
									}
								}
							}, 20 * 15);
					return true;
				}

				String confirmRU = wtc.get(p.getName());

				if (!confirmRU.equals(rankup.getRank())) {
					plugin.sms(p, Lang.RANKUP_CONFIRMATION_INCORRECT_RANKUP
							.getConfigValue(new String[] { confirmRU,
									rankup.getRank(), ezrank.getRank(),
									ezrank.getPrefix(), rankup.getPrefix() }));
					return true;
				}

				wtc.remove(p.getName());
			}
			
			
			EZRankupEvent rankupEvent = new EZRankupEvent(p, ezrank.getRank(), rankup.getRank(), String.valueOf(needed));
			Bukkit.getServer().getPluginManager().callEvent(rankupEvent);
			
			if (rankupEvent.isCancelled()) {
				plugin.debug(false, p.getName() + " attempted to rankup from " + ezrank.getRank()
								+ " to " + rankup.getRank());
				plugin.debug(false, "but the rankup event was cancelled by another plugin!");
				return true;
			}

			plugin.getPlayerhandler().rankupPlayer(p, ezrank, rankup, needed);

			plugin.debug(false, p.getName() + " ranked up from " + ezrank.getRank()
							+ " to " + rankup.getRank());

			if (plugin.useRankupCooldown()) {

				if (!p.hasPermission("ezadmin.cooldown.bypass")) {

					cooldown.add(p.getName());
					
					plugin.debug(false, p.getName()+" added to rankup cooldown");
					
					final String plname = p.getName();
					final int time = plugin.getRankupCooldownTime();
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin,
						new Runnable() {
							public void run() {
								if (cooldown.contains(plname)) {
									cooldown.remove(plname);
									plugin.debug(false, plname+" removed from rankup cooldown");
								}
							}
						}, (20 * time));

				}
			}

		}
		return true;
	}

}
