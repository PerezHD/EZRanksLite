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
package me.clip.ezrankslite.commands;

import java.util.List;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.rankdata.EZRank;
import me.clip.ezrankslite.rankdata.EZRankup;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class EZAdminConsole implements CommandExecutor {
	
	private EZRanksLite plugin;
	
	public EZAdminConsole(EZRanksLite instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label,
			String[] args) {
		
		if (s instanceof Player) {
			return true;
		}

		if (args.length == 0) {
			plugin.sms(s, "&fEZ&7Ranks&bLite &7version: &b"
					+ plugin.getDescription().getVersion());
			plugin.sms(s, "&fCreated by: &bextended_clip");
			plugin.sms(s, "&7Use &e/ezadmin help &7for admin commands");
			return true;
		}

		else if (args[0].equalsIgnoreCase("help")) {
			plugin.sms(s, "&fEZ&7Admin &b"
					+ plugin.getDescription().getVersion() + " &eHelp");
			plugin.sms(s, "&a/ezadmin createrankup <rankfrom> <rankto> <cost>");
			plugin.sms(s, "&fCreate a new rankup");
			plugin.sms(s, "&a/ezadmin setlastrank <rank> <boolean>");
			plugin.sms(s, "&fSet if a rank is the last rank players can rankup to");
			plugin.sms(s, "&a/ezadmin deleterankup <rankfrom> <rankto>");
			plugin.sms(s, "&fDelete a rankup");
			plugin.sms(s, "&a/ezadmin list");
			plugin.sms(s, "&flist all available rankups");
			plugin.sms(s, "&a/ezadmin info <rankfrom> <rankto>");
			plugin.sms(s, "&fview current active rankup settings");
			plugin.sms(s, "&a/ezadmin reload");
			plugin.sms(s, "&fReload rankups.yml");
			plugin.sms(s, "&a/ezadmin enable <rankfrom> <rankto>");
			plugin.sms(s, "&fenable a rankup that is disabled");
			plugin.sms(s, "&a/ezadmin disable <rankfrom> <rankto>");
			plugin.sms(s, "&fdisable a rankup that is enabled");
			plugin.sms(s, "&a/ezadmin setcost <rankfrom> <rankto> <cost>");
			plugin.sms(s, "&fdisable a rankup that is enabled");
			plugin.sms(s, "&a/ezadmin addcmd <rankfrom> <rankto> <command>");
			plugin.sms(s, "&fadd a command to be executed when a player ranks up");
			plugin.sms(s, "&a/ezadmin delcmd <rankfrom> <rankto> <command>");
			plugin.sms(s, "&fremove an active rankup command");
			plugin.sms(s, "&a/sbtoggle <player>");
			plugin.sms(s, "&ftoggle a players scoreboard on/off");
			plugin.sms(s, "&a/sbrefresh <player>");
			plugin.sms(s, "&frefresh a players scoreboard");
			return true;
		}
		
		else if (args[0].equalsIgnoreCase("setlastrank")
				|| args[0].equalsIgnoreCase("slr")) {

			if (args.length != 3) {
				plugin.sms(s, "&cIncorrect usage!");
				plugin.sms(s, "&bType &f/ezadmin help &bfor help");
				return true;
			}

			String rank = args[1];
			
			if (!isB(args[2].toUpperCase())) {
				plugin.sms(s, "&cIncorrect usage!");
				plugin.sms(s, "&bType &f/ezadmin help &bfor help");
				return true;
			}
			
			// if the groups are valid check if a rankup already exists
			if (plugin.getHooks().isValidServerGroup(rank)) {
				
				boolean is = Boolean.valueOf(args[2]);
				
				plugin.getRankFile().setLastRank(rank, is);
				plugin.getRankFile().reload();
				plugin.getRankFile().save();
				String loaded = plugin.getRankFile().loadRankupsFromFile();
				plugin.sms(s, loaded);
				if (is) {
				plugin.sms(s, rank+" &awill now be recognized as a last rank!");
				}
				else {
					plugin.sms(s, rank+" &awill not be recognized as a last rank!");
				}
				return true;
			} else {

				plugin.sms(s, "&4Last rank creation failed!");
				plugin.sms(s, "&f" + rank + "&b is not a valid server group");
				
				return true;

			}

		}

		else if (args[0].equalsIgnoreCase("createrankup")
				|| args[0].equalsIgnoreCase("cr")) {

			if (args.length != 4) {
				plugin.sms(s, "&cIncorrect usage!");
				plugin.sms(s, "&bType &f/ezadmin help &bfor help");
				return true;
			}

			String rankFrom = args[1];
			String rankTo = args[2];

			if (!plugin.isDouble(args[3])) {
				plugin.sms(s, "&cIncorrect rankup cost!");
				plugin.sms(s, "&bType &f/ezadmin help &bfor help");
				return true;
			}

			String cost = args[3];

			// if the groups are valid check if a rankup already exists
			if (plugin.getHooks().isValidServerGroup(rankFrom)
					&& plugin.getHooks().isValidServerGroup(rankTo)) {

				boolean isRankup = false;

				if (plugin.getRankHandler().hasRankData(rankFrom)) {
					EZRank ezrank = plugin.getRankHandler().getRankData(
							rankFrom);

					if (ezrank.hasRankups()) {
					for (EZRankup r : ezrank.getRankups()) {
						if (r.getRank().equals(rankTo)) {
							isRankup = true;
							break;
						}
					}
				}
				}

				if (plugin.getRankFile().containsEntry(rankFrom + "." + rankTo)) {
					isRankup = true;
				}

				if (isRankup == true) {
					plugin.sms(s, "&4Rankup creation failed:");
					plugin.sms(s, "&bThere is already a rankup for &f"
							+ rankFrom + "&b to &f" + rankTo + " &b!");
					return true;
				}
				plugin.sms(s, "&bRankup &f" + rankFrom + " &bto &f" + rankTo
						+ " &bloading...");
				plugin.getRankFile().createRankSection(rankFrom, rankTo, cost);
				plugin.getRankFile().reload();
				plugin.getRankFile().save();
				String loaded = plugin.getRankFile().loadRankupsFromFile();
				plugin.sms(s, loaded);
				plugin.sms(s, "&aRankup creation successful:");
				plugin.sms(s,
						"&eYou may now edit the rankup inside of the rankups.yml");
				plugin.sms(s,
						"&ewhen you are finished editing, use /ezadmin reload");
				return true;
			} else {

				plugin.sms(s, "&4Rankup creation failed!");
				if (plugin.getHooks().isValidServerGroup(rankFrom) == false) {
					plugin.sms(s, "&f" + rankFrom
							+ "&b is not a valid server group");
				}
				if (plugin.getHooks().isValidServerGroup(rankTo) == false) {
					plugin.sms(s, "&f" + rankTo
							+ "&b is not a valid server group");
				}
				return true;

			}

		}
		/*
		 * delete rankup command
		 */
		else if (args[0].equalsIgnoreCase("deleterankup")) {

			if (args.length != 3) {
				plugin.sms(s, "&4Incorrect usage!");
				plugin.sms(s, "&eType &f/ezadmin help &efor help");
				return true;
			}

			String rankFrom = args[1];
			String rankTo = args[2];

			for (String g : plugin.getRankHandler().getLoadedRanks()) {
				if (g.equalsIgnoreCase(rankFrom)) {
					rankFrom = g;
					break;
				}
			}

			EZRank ezrank = plugin.getRankHandler().getRankData(rankFrom);
			if (ezrank == null) {
				plugin.sms(s, "&4Rankup deletion failed:");
				plugin.sms(s, "&cThere were no rankups found for the rank &f"
						+ rankFrom + "&b!");
				return true;
			}

			for (EZRankup ru : ezrank.getRankups()) {
				if (ru.getRank().equalsIgnoreCase(rankTo)) {
					rankTo = ru.getRank();
					break;
				}
			}

			EZRankup r = ezrank.getRankup(rankTo);

			if (r == null) {
				plugin.sms(s, "&4Rankup deletion failed:");
				plugin.sms(s, "&cThere is no rankup for &f" + rankFrom
						+ "&c to &f" + rankTo + "&c!");
				return true;
			}

			ezrank.removeRankup(rankTo);
			plugin.getRankHandler().putRankData(rankFrom, ezrank);
			FileConfiguration config = plugin.getRankFile().load();
			config.set(rankFrom + "." + rankTo, null);
			plugin.getRankFile().save();
			plugin.sms(s, "&eThe rankup &f" + rankFrom + "&e to &f" + rankTo
					+ "&e");
			plugin.sms(s, "&ehas been successfully deleted!");
			return true;
		}
		/*
		 * reload command
		 */
		else if (args[0].equalsIgnoreCase("reload")) {
			
			plugin.getRankFile().reload();
			plugin.getRankFile().save();
			
			plugin.getMultiplierConfig().reload();
			plugin.getMultiplierConfig().save();
			
			plugin.reloadConfig();
			plugin.saveConfig();
			plugin.loadOptions();
			String loaded = plugin.getRankFile().loadRankupsFromFile();
			plugin.sms(s, "&bYou have successfully reloaded EZRanks!");
			plugin.sms(s, "&f" + loaded);
			plugin.sms(s, "&f" + plugin.getMultiplierConfig().loadMultipliers()+" &bmultiplier(s) loaded!");
			plugin.sms(s, "&f" + plugin.getMultiplierConfig().loadDiscounts()+" &bdiscount(s) loaded!");
			
			if (plugin.useScoreboard()) {
				plugin.startScoreboardTask();
				for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
					plugin.getBoardHandler().createScoreboard(pl);
				}
			} else {
				plugin.stopScoreboardTask();

				for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
					pl.setScoreboard(Bukkit.getScoreboardManager()
							.getNewScoreboard());
				}

			}
			
			return true;
		}
		else if (args[0].equalsIgnoreCase("list")) {
			if (!plugin.getRankHandler().isLoaded()) {
				plugin.sms(s, "&cThere are no rankups loaded!");
				return true;
			}
			for (String rank : plugin.getRankHandler().getLoadedRanks()) {
				if (plugin.getRankHandler().getRankData(rank) == null) {
					continue;
				}
				EZRank ezrank = plugin.getRankHandler().getRankData(rank);
				
				if (ezrank.getRankups() == null || ezrank.getRankups().isEmpty()) {
					continue;
				}
				for (EZRankup rankup : ezrank.getRankups()) {
					plugin.sms(s, rank + " &bto &f" + rankup.getRank() + "  &bCost: &f" + rankup.getCost());
				}
			}
			return true;
		}
		//enable a rankup
				else if (args[0].equalsIgnoreCase("enable")) {

					if (args.length != 3) {
						plugin.sms(s, "&4Incorrect usage!");
						plugin.sms(s, "&7/ezadmin enable <rankfrom> <rankto>");
						return true;
					}
					
					String groupFrom = args[1];
					String groupTo = args[2];
					
					for (String gFrom : plugin.getRankHandler().getLoadedRanks()) {
						if (gFrom.equalsIgnoreCase(groupFrom)) {
							groupFrom = gFrom;
							break;
						}
					}
					
					if (plugin.getRankHandler().getRankData(groupFrom) == null) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRank rank = plugin.getRankHandler().getRankData(groupFrom);
					
					if (!rank.hasRankups()) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRankup rankup = null;
					
					for (EZRankup ezr : rank.getRankups()) {
						if (ezr.getRank().equalsIgnoreCase(groupTo)) {
							groupTo = ezr.getRank();
							rankup = ezr;
							break;
						}
					}
					
					if (rankup == null) {
						plugin.sms(s, "&f" + groupFrom + "&4 does not have a rankup to &f" + groupTo + "&4!");
						return true;
					}
					
					if (rankup.isActive()) {
						plugin.sms(s, "&4Rankup &f" + groupFrom + "&4 to &f" + groupTo + " &4is already active!");
						return true;
					}
					
					rankup.setActive(true);
					rank.addRankup(groupTo, rankup);
					plugin.getRankHandler().putRankData(groupFrom, rank);
					FileConfiguration config = plugin.getRankFile().load();
			    	config.set(groupFrom + "." + groupTo + ".active", true);
			    	plugin.getRankFile().save();
				
					plugin.sms(s, "&bRankup &f" + groupFrom + "&b to &f" + groupTo + " &bis now active!");
					
					return true;
				}
				//disable a rankup
				else if (args[0].equalsIgnoreCase("disable")) {

					if (args.length != 3) {
						plugin.sms(s, "&4Incorrect usage!");
						plugin.sms(s, "&7/ezadmin disable <rankfrom> <rankto>");
						return true;
					}
					
					String groupFrom = args[1];
					String groupTo = args[2];
					
					for (String gFrom : plugin.getRankHandler().getLoadedRanks()) {
						if (gFrom.equalsIgnoreCase(groupFrom)) {
							groupFrom = gFrom;
							break;
						}
					}
					
					if (plugin.getRankHandler().getRankData(groupFrom) == null) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRank rank = plugin.getRankHandler().getRankData(groupFrom);
					
					if (!rank.hasRankups()) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRankup rankup = null;
					
					for (EZRankup ezr : rank.getRankups()) {
						if (ezr.getRank().equalsIgnoreCase(groupTo)) {
							groupTo = ezr.getRank();
							rankup = ezr;
							break;
						}
					}
					
					if (rankup == null) {
						plugin.sms(s, "&f" + groupFrom + "&4 does not have a rankup to &f" + groupTo + "&4!");
						return true;
					}
					
					if (!rankup.isActive()) {
						plugin.sms(s, "&4Rankup &f" + groupFrom + "&4 to &f" + groupTo + " &4is already disabled!");
						return true;
					}
					
					rankup.setActive(false);
					rank.addRankup(groupTo, rankup);
					plugin.getRankHandler().putRankData(groupFrom, rank);
					FileConfiguration config = plugin.getRankFile().load();
			    	config.set(groupFrom + "." + groupTo + ".active", false);
			    	plugin.getRankFile().save();
				
					plugin.sms(s, "&bRankup &f" + groupFrom + "&b to &f" + groupTo + " &bis now disabled!");
					
					return true;
				}
				//change cost
				else if (args[0].equalsIgnoreCase("setcost")) {

					if (args.length != 4) {
						plugin.sms(s, "&4Incorrect usage!");
						plugin.sms(s, "&7/ezadmin setcost <rankfrom> <rankto> <cost>");
						return true;
					}
					
					String groupFrom = args[1];
					String groupTo = args[2];
					
					for (String gFrom : plugin.getRankHandler().getLoadedRanks()) {
						if (gFrom.equalsIgnoreCase(groupFrom)) {
							groupFrom = gFrom;
							break;
						}
					}
					
					if (plugin.getRankHandler().getRankData(groupFrom) == null) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRank rank = plugin.getRankHandler().getRankData(groupFrom);
					
					if (!rank.hasRankups()) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRankup rankup = null;
					
					for (EZRankup ezr : rank.getRankups()) {
						if (ezr.getRank().equalsIgnoreCase(groupTo)) {
							groupTo = ezr.getRank();
							rankup = ezr;
							break;
						}
					}
					
					if (rankup == null) {
						plugin.sms(s, "&f" + groupFrom + "&4 does not have a rankup to &f" + groupTo + "&4!");
						return true;
					}
					
					if (!plugin.isDouble(args[3])) {
						plugin.sms(s, "&f" + args[3] + "&4is not a valid amount to charge&4!");
						return true;
					}
					
					String cost = args[3];
					
					rankup.setCost(cost);
					rank.addRankup(groupTo, rankup);
					plugin.getRankHandler().putRankData(groupFrom, rank);
					FileConfiguration config = plugin.getRankFile().load();
			    	config.set(groupFrom + "." + groupTo + ".cost", cost);
			    	plugin.getRankFile().save();
				
					plugin.sms(s, "&bRankup &f" + groupFrom + "&b to &f" + groupTo + " &bwill now cost &f" + cost);
					
					return true;
				}
				//add command
				else if (args[0].equalsIgnoreCase("addcommand") || args[0].equalsIgnoreCase("addcmd")) {
					
					if (args.length < 4) {
						plugin.sms(s, "&4Incorrect usage!");
						plugin.sms(s, "&7/ezadmin addcmd <rankfrom> <rankto> <command>");
						return true;
					}
					
					String groupFrom = args[1];
					String groupTo = args[2];
					
					for (String gFrom : plugin.getRankHandler().getLoadedRanks()) {
						if (gFrom.equalsIgnoreCase(groupFrom)) {
							groupFrom = gFrom;
							break;
						}
					}
					
					if (plugin.getRankHandler().getRankData(groupFrom) == null) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRank rank = plugin.getRankHandler().getRankData(groupFrom);
					
					if (!rank.hasRankups()) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRankup rankup = null;
					
					for (EZRankup ezr : rank.getRankups()) {
						if (ezr.getRank().equalsIgnoreCase(groupTo)) {
							groupTo = ezr.getRank();
							rankup = ezr;
							break;
						}
					}
					
					if (rankup == null) {
						plugin.sms(s, "&f" + groupFrom + "&4 does not have a rankup to &f" + groupTo + "&4!");
						return true;
					}
					
					String toAdd = args[3].replace("/", "");
					plugin.getLogger().info(toAdd);
					for (int i=0;i<args.length;i++) {
						if (i <= 3) {
							continue;
						}
						toAdd = toAdd+" "+args[i];
						plugin.getLogger().info(toAdd);
					}
					
					plugin.getLogger().info(toAdd);
					
					List<String> cmds = rankup.getCommands();
					
					if (cmds.contains(toAdd)) {
						plugin.sms(s, "&f" + toAdd + "&4 already exists for &f" + groupFrom + " &4to &f" + groupTo + "!");
						return true;
					}
					cmds.add(toAdd);
					rankup.setCommands(cmds);
					
					rank.addRankup(groupTo, rankup);
					plugin.getRankHandler().putRankData(groupFrom, rank);
					FileConfiguration config = plugin.getRankFile().load();
			    	config.set(groupFrom + "." + groupTo + ".rankup_commands", cmds);
			    	plugin.getRankFile().save();
				
					plugin.sms(s, toAdd + " &bwas successfully added to &f" + groupFrom + " &bto &f" + groupTo);
					
					return true;
				}
				//delete command
				else if (args[0].equalsIgnoreCase("delcommand") || args[0].equalsIgnoreCase("delcmd")) {
					
					if (args.length < 4) {
						plugin.sms(s, "&4Incorrect usage!");
						plugin.sms(s, "&7/ezadmin delcmd <rankfrom> <rankto> <command>");
						return true;
					}
					
					String groupFrom = args[1];
					String groupTo = args[2];
					
					for (String gFrom : plugin.getRankHandler().getLoadedRanks()) {
						if (gFrom.equalsIgnoreCase(groupFrom)) {
							groupFrom = gFrom;
							break;
						}
					}
					
					if (plugin.getRankHandler().getRankData(groupFrom) == null) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRank rank = plugin.getRankHandler().getRankData(groupFrom);
					
					if (!rank.hasRankups()) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRankup rankup = null;
					
					for (EZRankup ezr : rank.getRankups()) {
						if (ezr.getRank().equalsIgnoreCase(groupTo)) {
							groupTo = ezr.getRank();
							rankup = ezr;
							break;
						}
					}
					
					if (rankup == null) {
						plugin.sms(s, "&f" + groupFrom + "&4 does not have a rankup to &f" + groupTo + "&4!");
						return true;
					}
					
					String toDel = args[3].replace("/", "");
					
					for (int i=0;i<args.length;i++) {
						if (i <= 3) {
							continue;
						}
						toDel = toDel+" "+args[i];
					}
					
					plugin.getLogger().info(toDel);
					
					List<String> cmds = rankup.getCommands();
					
					if (!cmds.contains(toDel)) {
						plugin.sms(s, "&f" + toDel + "&4 does not exist for &f" + groupFrom + " &4to &f" + groupTo + "!");
						return true;
					}
					cmds.remove(toDel);
					rankup.setCommands(cmds);
					
					rank.addRankup(groupTo, rankup);
					plugin.getRankHandler().putRankData(groupFrom, rank);
					FileConfiguration config = plugin.getRankFile().load();
			    	config.set(groupFrom + "." + groupTo + ".rankup_commands", cmds);
			    	plugin.getRankFile().save();
				
					plugin.sms(s, toDel + " &bwas successfully removed from &f" + groupFrom + " &bto &f" + groupTo);
					
					return true;
				}
				//delete command
				else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("information")) {
					
					if (args.length != 3) {
						plugin.sms(s, "&4Incorrect usage!");
						plugin.sms(s, "&7/ezadmin info <rankfrom> <rankto>");
						return true;
					}
					
					String groupFrom = args[1];
					String groupTo = args[2];
					
					for (String gFrom : plugin.getRankHandler().getLoadedRanks()) {
						if (gFrom.equalsIgnoreCase(groupFrom)) {
							groupFrom = gFrom;
							break;
						}
					}
					
					if (plugin.getRankHandler().getRankData(groupFrom) == null) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRank rank = plugin.getRankHandler().getRankData(groupFrom);
					
					if (!rank.hasRankups()) {
						plugin.sms(s, "&4There are no rankups loaded for the rank &f" + groupFrom + "&4!");
						return true;
					}
					
					EZRankup rankup = null;
					
					for (EZRankup ezr : rank.getRankups()) {
						if (ezr.getRank().equalsIgnoreCase(groupTo)) {
							groupTo = ezr.getRank();
							rankup = ezr;
							break;
						}
					}
					
					if (rankup == null) {
						plugin.sms(s, "&f" + groupFrom + "&4 does not have a rankup to &f" + groupTo + "&4!");
						return true;
					}
					
					plugin.sms(s, "&bRankup &f" + rank.getRank() + " &bto &f" + rankup.getRank());
					plugin.sms(s, "&bActive: &f" + rankup.isActive());
					plugin.sms(s, "&bCost: &f" + rankup.getCost());
					plugin.sms(s, "&bRequirement message:");
					for (String msg : rankup.getRequirementMsg()) {
						plugin.sms(s, msg);
					}
					plugin.sms(s, "&bRankup commands:");
					for (String c : rankup.getCommands()) {
						plugin.sms(s, c);
					}
					
					return true;
				}
		else {
			plugin.sms(s, "&cIncorrect usage! Use &b/ezadmin help");
		}
		return true;
	
	}
	
	private boolean isB(String s) {
		try {
			Boolean.parseBoolean(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
