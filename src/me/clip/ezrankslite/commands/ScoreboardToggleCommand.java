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

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.Lang;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreboardToggleCommand implements CommandExecutor {

	private EZRanksLite plugin;
	
	public ScoreboardToggleCommand(EZRanksLite instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (!plugin.useScoreboard()) {
			plugin.sms(sender, Lang.SCOREBOARD_DISABLED.getConfigValue(null));
			return true;
		}
		
		if (!(sender instanceof Player)) {
			if (args.length > 0) {
				
				@SuppressWarnings("deprecation")
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if (target == null) {
					plugin.sms(sender, Lang.PLAYER_NOT_ONLINE.getConfigValue(new String[] {
							args[0]
					}));
					return true;
				}
				
				if (plugin.getBoardHandler().hasScoreboard(target)) {
					plugin.getBoardHandler().removeScoreboard(target);
					plugin.sms(sender, Lang.SCOREBOARD_TOGGLE_OFF_OTHERS.getConfigValue(new String[] {
							target.getName()
					}));
				}
				else {
					plugin.getBoardHandler().createScoreboard(target);
					plugin.sms(sender, Lang.SCOREBOARD_TOGGLE_ON_OTHERS.getConfigValue(new String[] {
							target.getName()
					}));
				}
				
				
				
			}
			else {
				plugin.sms(sender, "&cIncorrect usage! Use &f/sbtoggle <player>");
			}
			return true;
		}
		
		Player p = (Player) sender;
		
		if (args.length == 0) {
		if (plugin.getBoardHandler().hasScoreboard(p)) {
			plugin.getBoardHandler().removeScoreboard(p);
			plugin.sms(sender, Lang.SCOREBOARD_TOGGLE_OFF_SELF.getConfigValue(null));
		}
		else {
			plugin.getBoardHandler().createScoreboard(p);
			plugin.sms(sender, Lang.SCOREBOARD_TOGGLE_ON_SELF.getConfigValue(null));
		}
		}
		else if (args.length > 0) {
			
			if (!p.hasPermission("ezranks.admin.scoreboard")) {
				plugin.sms(sender, Lang.NO_PERMISSION.getConfigValue(null));
				return true;
			}
			
			@SuppressWarnings("deprecation")
			Player target = Bukkit.getServer().getPlayer(args[0]);
			
			if (target == null) {
				plugin.sms(sender, Lang.PLAYER_NOT_ONLINE.getConfigValue(new String[] {
						args[0]
				}));
				return true;
			}
			
			if (plugin.getBoardHandler().hasScoreboard(target)) {
				plugin.getBoardHandler().removeScoreboard(target);
				plugin.sms(target, Lang.SCOREBOARD_TOGGLE_OFF_SELF.getConfigValue(null));
				plugin.sms(sender, Lang.SCOREBOARD_TOGGLE_OFF_OTHERS.getConfigValue(new String[] {
						target.getName()
				}));
			}
			else {
				plugin.getBoardHandler().createScoreboard(target);
				plugin.sms(target, Lang.SCOREBOARD_TOGGLE_ON_SELF.getConfigValue(null));
				plugin.sms(sender, Lang.SCOREBOARD_TOGGLE_OFF_OTHERS.getConfigValue(new String[] {
						target.getName()
				}));
			}
			
			
			
		}
		return true;
	}
	
}
