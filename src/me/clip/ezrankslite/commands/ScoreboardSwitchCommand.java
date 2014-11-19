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

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.Lang;
import me.clip.ezrankslite.scoreboard.ScoreboardHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreboardSwitchCommand implements CommandExecutor {

	private EZRanksLite plugin;
	
	public ScoreboardSwitchCommand(EZRanksLite instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if (!(sender instanceof Player)) {
			
			plugin.sms(sender, "&cConsole /sbswitch command not supported!");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("ezranks.staff")) {
			plugin.sms(sender, Lang.NO_PERMISSION.getConfigValue(new String[] {
					"ezranks.staff"
			}));
			return true;
		}
		
		if (!plugin.useScoreboard()) {
			
			plugin.sms(sender, Lang.SCOREBOARD_DISABLED.getConfigValue(null));
			return true;
		}
		
		if (plugin.getSbOptions() == null) {
			
			plugin.loadSBOptions();
		}
		
		if (!plugin.getSbOptions().useStaffScoreboard()) {
			
			plugin.sms(sender, Lang.STAFF_SCOREBOARD_DISABLED.getConfigValue(null));
			return true;
		}
		
		
		
		if (ScoreboardHandler.staffToggled == null) {
			
			ScoreboardHandler.staffToggled = new ArrayList<String>();
		}
		
		if (ScoreboardHandler.staffToggled.isEmpty() 
				|| !ScoreboardHandler.staffToggled.contains(p.getUniqueId().toString())) {
			
			ScoreboardHandler.staffToggled.add(p.getUniqueId().toString());
			plugin.sms(p, Lang.STAFF_SCOREBOARD_TOGGLE_ON.getConfigValue(null));
		} else {
			
			ScoreboardHandler.staffToggled.remove(p.getUniqueId().toString());
			plugin.sms(p, Lang.STAFF_SCOREBOARD_TOGGLE_OFF.getConfigValue(null));
		}
		
		plugin.getBoardHandler().createScoreboard(p);
		return true;
	}
	
}
