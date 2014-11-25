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

public class EZAdminCommand implements CommandExecutor {

	private EZRanksLite plugin;
	private EZAdminConsole console;

	public EZAdminCommand(EZRanksLite instance) {
		plugin = instance;
		console = new EZAdminConsole(instance);
	}

	private static List<String> wtc = new ArrayList<String>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			
			return console.onCommand(sender, cmd, label, args);
		}

		Player p = (Player) sender;

		if (!p.hasPermission("ezranks.admin")) {
			
			plugin.sms(p, "&cYou do not have permission to use that command!");
			return true;
		}

		if (args.length == 0) {
			
			plugin.sms(p, "&fEZ&7Ranks&bLite &7version: &b"+plugin.getDescription().getVersion());
			plugin.sms(p, "&fCreated by: &bextended_clip");
			plugin.sms(p, "&7Use &e/ezadmin help &7for admin commands");
			
			return true;
		} 
		else if (args[0].equalsIgnoreCase("help")) {
			
			plugin.sms(p, "&fEZ&7Admin &b"+plugin.getDescription().getVersion()+" &eHelp");
			plugin.sms(p, "&a/ezadmin createrankup <rankfrom> <rankto> <cost>");
			plugin.sms(p, "&fCreate a new rankup");
			plugin.sms(p, "&a/ezadmin setlastrank <rank> <boolean>");
			plugin.sms(p, "&fSet if a rank is the last rank players can rankup to");
			plugin.sms(p, "&a/ezadmin deleterankup <rankfrom> <rankto>");
			plugin.sms(p, "&fDelete a rankup");
			plugin.sms(p, "&a/ezadmin list");
			plugin.sms(p, "&flist all available rankups");
			plugin.sms(p, "&a/ezadmin info <rankfrom> <rankto>");
			plugin.sms(p, "&fview current active rankup settings");
			plugin.sms(p, "&a/ezadmin reload");
			plugin.sms(p, "&fReload rankups.yml");
			plugin.sms(p, "&a/ezadmin enable <rankfrom> <rankto>");
			plugin.sms(p, "&fenable a rankup that is disabled");
			plugin.sms(p, "&a/ezadmin disable <rankfrom> <rankto>");
			plugin.sms(p, "&fdisable a rankup that is enabled");
			plugin.sms(p, "&a/ezadmin setcost <rankfrom> <rankto> <cost>");
			plugin.sms(p, "&fdisable a rankup that is enabled");
			plugin.sms(p, "&a/ezadmin addcmd <rankfrom> <rankto> <command>");
			plugin.sms(p, "&fadd a command to be executed when a player ranks up");
			plugin.sms(p, "&a/ezadmin delcmd <rankfrom> <rankto> <command>");
			plugin.sms(p, "&fremove an active rankup command");
			plugin.sms(p, "&a/sbtoggle <player>");
			plugin.sms(p, "&ftoggle a players scoreboard on/off");
			plugin.sms(p, "&a/sbrefresh <player>");
			plugin.sms(p, "&frefresh a players scoreboard");
			return true;
		} 
		else if (args[0].equalsIgnoreCase("setlastrank")
				|| args[0].equalsIgnoreCase("slr")) {
			
			if (!p.hasPermission("ezranks.admin.create")) {
				
				plugin.sms(p, "&cYou don't have permission to do this!!");
				return true;
			}

			if (args.length != 3) {
				
				plugin.sms(p, "&cIncorrect usage!");
				plugin.sms(p, "&bType &f/ezadmin help &bfor help");
				return true;
			}

			String rank = args[1];
			
			if (!isB(args[2].toUpperCase())) {
				
				plugin.sms(p, "&cIncorrect usage!");
				plugin.sms(p, "&bType &f/ezadmin help &bfor help");
				return true;
			}
			
			// if the groups are valid check if a rankup already exists
			if (plugin.getHooks().isValidServerGroup(rank)) {
				
				boolean is = Boolean.valueOf(args[2]);
				
				plugin.getRankFile().setLastRank(rank, is);
				
				plugin.getRankFile().reload();
				plugin.getRankFile().save();
				
				String loaded = plugin.getRankFile().loadRankupsFromFile();
				
				plugin.sms(p, loaded);
				
				if (is) {
					
					plugin.sms(p, rank+" &awill now be recognized as a last rank!");
				}
				else {
					
					plugin.sms(p, rank+" &awill not be recognized as a last rank!");
				}
				
				return true;
			} else {

				plugin.sms(p, "&4Last rank creation failed!");
				plugin.sms(p, "&f" + rank + "&b is not a valid server group");
				
				return true;
			}
		}
		else if (args[0].equalsIgnoreCase("createrankup")
				|| args[0].equalsIgnoreCase("cr")) {

			if (!p.hasPermission("ezranks.admin.create")) {
				plugin.sms(p, "&cYou don't have permission to do this!!");
				return true;
			}

			if (args.length != 4) {
				plugin.sms(p, "&cIncorrect usage!");
				plugin.sms(p, "&bType &f/ezadmin help &bfor help");
				return true;
			}

			String rankFrom = args[1];
			String rankTo = args[2];

			if (!plugin.isDouble(args[3])) {
				plugin.sms(p, "&cIncorrect rankup cost!");
				plugin.sms(p, "&bType &f/ezadmin help &bfor help");
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

				if (isRankup) {
					plugin.sms(p, "&4Rankup creation failed:");
					plugin.sms(p, "&bThere is already a rankup for &f"
							+ rankFrom + "&b to &f" + rankTo + " &b!");
					return true;
				}
				
				plugin.sms(p, "&bRankup &f"+rankFrom+" &bto &f"+rankTo+" &bloading...");
				plugin.getRankFile().createRankSection(rankFrom, rankTo, cost);
				plugin.getRankFile().reload();
				plugin.sms(p, plugin.getRankFile().loadRankupsFromFile());
				plugin.sms(p, "&aRankup creation successful:");
				plugin.sms(p, "&eYou may now edit the rankup inside of the rankups.yml");
				plugin.sms(p, "&ewhen you are finished editing, use /ezadmin reload");
				return true;
			} else {

				plugin.sms(p, "&4Rankup creation failed!");
				if (plugin.getHooks().isValidServerGroup(rankFrom) == false) {
					plugin.sms(p, "&f" + rankFrom
							+ "&b is not a valid server group");
				}
				if (plugin.getHooks().isValidServerGroup(rankTo) == false) {
					plugin.sms(p, "&f" + rankTo
							+ "&b is not a valid server group");
				}
				return true;

			}

		}
		/*
		 * delete rankup command
		 */
		else if (args[0].equalsIgnoreCase("deleterankup")) {

			if (!p.hasPermission("ezranks.admin.delete")) {
				plugin.sms(p, "&cYou don't have permission to do this!!");
				return true;
			}

			if (args.length != 3) {
				plugin.sms(p, "&4Incorrect usage!");
				plugin.sms(p, "&eType &f/ezadmin help &efor help");
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
				plugin.sms(p, "&4Rankup deletion failed:");
				plugin.sms(p, "&cThere were no rankups found for the rank &f"
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
				plugin.sms(p, "&4Rankup deletion failed:");
				plugin.sms(p, "&cThere is no rankup for &f" + rankFrom
						+ "&c to &f" + rankTo + "&c!");
				return true;
			}

			if (!wtc.contains(p.getName())) {
				wtc.add(p.getName());

				plugin.sms(p, "&eAre you sure you want to remove the rankup &f"
						+ rankFrom + "&e to &f" + rankTo + "&e?");
				plugin.sms(p, "&eType &f/rankup admin deleterankup " + rankFrom
						+ " " + rankTo + " &eto confirm!");

				final String plname = p.getName();
				final Player pll = p;
				Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin,
						new Runnable() {
							public void run() {
								if (wtc.contains(plname)) {
									wtc.remove(plname);
									plugin.sms(pll,
											"&cRankup removal has expired.");
								}
							}
						}, 20 * 15);
				return true;
			}

			wtc.remove(p.getName());
			ezrank.removeRankup(rankTo);
			plugin.getRankHandler().putRankData(rankFrom, ezrank);
			FileConfiguration config = plugin.getRankFile().load();
			config.set(rankFrom + "." + rankTo, null);
			plugin.getRankFile().save();
			plugin.sms(p, "&eThe rankup &f" + rankFrom + "&e to &f" + rankTo
					+ "&e");
			plugin.sms(p, "&ehas been successfully deleted!");
			return true;
		}
		/*
		 * reload command
		 */
		else if (args[0].equalsIgnoreCase("reload")) {
			if (!p.hasPermission("ezranks.admin.reload")) {
				plugin.sms(p, "&cYou don't have permission to do this!!");
				return true;
			}

			plugin.getRankFile().reload();
			plugin.getRankFile().save();
			
			plugin.getMultiplierConfig().reload();
			plugin.getMultiplierConfig().save();
			plugin.reloadConfig();
			plugin.saveConfig();
			plugin.loadOptions();
			String loaded = plugin.getRankFile().loadRankupsFromFile();
			plugin.sms(p, "&bYou have successfully reloaded EZRanks!");
			plugin.sms(p, "&f" + plugin.getMultiplierConfig().loadMultipliers()+" &bmultipliers loaded!");
			plugin.sms(p, "&f" + plugin.getMultiplierConfig().loadDiscounts()+" &bdiscounts loaded!");
			plugin.sms(p, "&f" + loaded);

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
		} else if (args[0].equalsIgnoreCase("list")) {
			if (!plugin.getRankHandler().isLoaded()) {
				plugin.sms(p, "&cThere are no rankups loaded!");
				return true;
			}
			for (String rank : plugin.getRankHandler().getLoadedRanks()) {
				if (plugin.getRankHandler().getRankData(rank) == null) {
					continue;
				}
				EZRank ezrank = plugin.getRankHandler().getRankData(rank);
				if (ezrank.getRankups() == null
						|| ezrank.getRankups().isEmpty()) {
					continue;
				}
				for (EZRankup rankup : ezrank.getRankups()) {
					plugin.sms(p, rank + " &bto &f" + rankup.getRank()
							+ "  &bCost: &f" + rankup.getCost());
				}
			}
			return true;
		}
		// enable a rankup
		else if (args[0].equalsIgnoreCase("enable")) {

			if (!p.hasPermission("ezranks.admin.setactive")) {
				plugin.sms(p, "&cYou don't have permission to do this!!");
				return true;
			}

			if (args.length != 3) {
				plugin.sms(p, "&4Incorrect usage!");
				plugin.sms(p, "&7/ezadmin enable <rankfrom> <rankto>");
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
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
				return true;
			}

			EZRank rank = plugin.getRankHandler().getRankData(groupFrom);

			if (!rank.hasRankups()) {
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
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
				plugin.sms(p, "&f" + groupFrom
						+ "&4 does not have a rankup to &f" + groupTo + "&4!");
				return true;
			}

			if (rankup.isActive()) {
				plugin.sms(p, "&4Rankup &f" + groupFrom + "&4 to &f" + groupTo
						+ " &4is already active!");
				return true;
			}

			rankup.setActive(true);
			rank.addRankup(groupTo, rankup);
			plugin.getRankHandler().putRankData(groupFrom, rank);
			FileConfiguration config = plugin.getRankFile().load();
			config.set(groupFrom + "." + groupTo + ".active", true);
			plugin.getRankFile().save();

			plugin.sms(p, "&bRankup &f" + groupFrom + "&b to &f" + groupTo
					+ " &bis now active!");

			return true;
		}
		// disable a rankup
		else if (args[0].equalsIgnoreCase("disable")) {

			if (!p.hasPermission("ezranks.admin.setactive")) {
				plugin.sms(p, "&cYou don't have permission to do this!!");
				return true;
			}

			if (args.length != 3) {
				plugin.sms(p, "&4Incorrect usage!");
				plugin.sms(p, "&7/ezadmin disable <rankfrom> <rankto>");
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
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
				return true;
			}

			EZRank rank = plugin.getRankHandler().getRankData(groupFrom);

			if (!rank.hasRankups()) {
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
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
				plugin.sms(p, "&f" + groupFrom
						+ "&4 does not have a rankup to &f" + groupTo + "&4!");
				return true;
			}

			if (!rankup.isActive()) {
				plugin.sms(p, "&4Rankup &f" + groupFrom + "&4 to &f" + groupTo
						+ " &4is already disabled!");
				return true;
			}

			rankup.setActive(false);
			rank.addRankup(groupTo, rankup);
			plugin.getRankHandler().putRankData(groupFrom, rank);
			FileConfiguration config = plugin.getRankFile().load();
			config.set(groupFrom + "." + groupTo + ".active", false);
			plugin.getRankFile().save();

			plugin.sms(p, "&bRankup &f" + groupFrom + "&b to &f" + groupTo
					+ " &bis now disabled!");

			return true;
		}
		// change cost
		else if (args[0].equalsIgnoreCase("setcost")) {

			if (!p.hasPermission("ezranks.admin.setcost")) {
				plugin.sms(p, "&cYou don't have permission to do this!!");
				return true;
			}

			if (args.length != 4) {
				plugin.sms(p, "&4Incorrect usage!");
				plugin.sms(p, "&7/ezadmin setcost <rankfrom> <rankto> <cost>");
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
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
				return true;
			}

			EZRank rank = plugin.getRankHandler().getRankData(groupFrom);

			if (!rank.hasRankups()) {
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
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
				plugin.sms(p, "&f" + groupFrom
						+ "&4 does not have a rankup to &f" + groupTo + "&4!");
				return true;
			}

			if (!plugin.isDouble(args[3])) {
				plugin.sms(p, "&f" + args[3]
						+ "&4is not a valid amount to charge&4!");
				return true;
			}

			String cost = args[3];

			rankup.setCost(cost);
			rank.addRankup(groupTo, rankup);
			plugin.getRankHandler().putRankData(groupFrom, rank);
			FileConfiguration config = plugin.getRankFile().load();
			config.set(groupFrom + "." + groupTo + ".cost", cost);
			plugin.getRankFile().save();

			plugin.sms(p, "&bRankup &f" + groupFrom + "&b to &f" + groupTo
					+ " &bwill now cost &f" + cost);

			return true;
		}
		// add command
		else if (args[0].equalsIgnoreCase("addcommand")
				|| args[0].equalsIgnoreCase("addcmd")) {

			if (!p.hasPermission("ezranks.admin.addcommand")) {
				plugin.sms(p, "&cYou don't have permission to do this!!");
				return true;
			}

			if (args.length < 4) {
				plugin.sms(p, "&4Incorrect usage!");
				plugin.sms(p, "&7/ezadmin addcmd <rankfrom> <rankto> <command>");
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
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
				return true;
			}

			EZRank rank = plugin.getRankHandler().getRankData(groupFrom);

			if (!rank.hasRankups()) {
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
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
				plugin.sms(p, "&f" + groupFrom
						+ "&4 does not have a rankup to &f" + groupTo + "&4!");
				return true;
			}

			String toAdd = args[3].replace("/", "");
			plugin.getLogger().info(toAdd);
			for (int i = 0; i < args.length; i++) {
				if (i <= 3) {
					continue;
				}
				toAdd = toAdd + " " + args[i];
				plugin.getLogger().info(toAdd);
			}

			plugin.getLogger().info(toAdd);

			List<String> cmds = rankup.getCommands();

			if (cmds.contains(toAdd)) {
				plugin.sms(p, "&f" + toAdd + "&4 already exists for &f"
						+ groupFrom + " &4to &f" + groupTo + "!");
				return true;
			}
			cmds.add(toAdd);
			rankup.setCommands(cmds);

			rank.addRankup(groupTo, rankup);
			plugin.getRankHandler().putRankData(groupFrom, rank);
			FileConfiguration config = plugin.getRankFile().load();
			config.set(groupFrom + "." + groupTo + ".rankup_commands", cmds);
			plugin.getRankFile().save();

			plugin.sms(p, toAdd + " &bwas successfully added to &f" + groupFrom
					+ " &bto &f" + groupTo);

			return true;
		}
		// delete command
		else if (args[0].equalsIgnoreCase("delcommand")
				|| args[0].equalsIgnoreCase("delcmd")) {

			if (!p.hasPermission("ezranks.admin.delcommand")) {
				plugin.sms(p, "&cYou don't have permission to do this!!");
				return true;
			}

			if (args.length < 4) {
				plugin.sms(p, "&4Incorrect usage!");
				plugin.sms(p, "&7/ezadmin delcmd <rankfrom> <rankto> <command>");
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
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
				return true;
			}

			EZRank rank = plugin.getRankHandler().getRankData(groupFrom);

			if (!rank.hasRankups()) {
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
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
				plugin.sms(p, "&f" + groupFrom
						+ "&4 does not have a rankup to &f" + groupTo + "&4!");
				return true;
			}

			String toDel = args[3].replace("/", "");

			for (int i = 0; i < args.length; i++) {
				if (i <= 3) {
					continue;
				}
				toDel = toDel + " " + args[i];
			}

			plugin.getLogger().info(toDel);

			List<String> cmds = rankup.getCommands();

			if (!cmds.contains(toDel)) {
				plugin.sms(p, "&f" + toDel + "&4 does not exist for &f"
						+ groupFrom + " &4to &f" + groupTo + "!");
				return true;
			}
			cmds.remove(toDel);
			rankup.setCommands(cmds);

			rank.addRankup(groupTo, rankup);
			plugin.getRankHandler().putRankData(groupFrom, rank);
			FileConfiguration config = plugin.getRankFile().load();
			config.set(groupFrom + "." + groupTo + ".rankup_commands", cmds);
			plugin.getRankFile().save();

			plugin.sms(p, toDel + " &bwas successfully removed from &f"
					+ groupFrom + " &bto &f" + groupTo);

			return true;
		}
		// delete command
		else if (args[0].equalsIgnoreCase("info")
				|| args[0].equalsIgnoreCase("information")) {

			if (args.length != 3) {
				plugin.sms(p, "&4Incorrect usage!");
				plugin.sms(p, "&7/ezadmin info <rankfrom> <rankto>");
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
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
				return true;
			}

			EZRank rank = plugin.getRankHandler().getRankData(groupFrom);

			if (!rank.hasRankups()) {
				plugin.sms(p, "&4There are no rankups loaded for the rank &f"
						+ groupFrom + "&4!");
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
				plugin.sms(p, "&f" + groupFrom
						+ "&4 does not have a rankup to &f" + groupTo + "&4!");
				return true;
			}

			plugin.sms(
					p,
					"&bRankup &f" + rank.getRank() + " &bto &f"
							+ rankup.getRank());
			plugin.sms(p, "&bActive: &f" + rankup.isActive());
			plugin.sms(p, "&bCost: &f" + rankup.getCost());
			plugin.sms(p, "&bRequirement message:");
			for (String msg : rankup.getRequirementMsg()) {
				plugin.sms(p, msg);
			}
			plugin.sms(p, "&bRankup commands:");
			for (String c : rankup.getCommands()) {
				plugin.sms(p, c);
			}

			return true;
		} else {
			plugin.sms(p, "&cIncorrect usage! Use &b/ezadmin help");
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
