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
package me.clip.ezrankslite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.clip.ezrankslite.commands.EZAdminCommand;
import me.clip.ezrankslite.commands.RanksCommand;
import me.clip.ezrankslite.commands.RankupCommand;
import me.clip.ezrankslite.commands.ScoreboardRefreshCommand;
import me.clip.ezrankslite.commands.ScoreboardSwitchCommand;
import me.clip.ezrankslite.commands.ScoreboardToggleCommand;
import me.clip.ezrankslite.config.Config;
import me.clip.ezrankslite.config.ConfigWrapper;
import me.clip.ezrankslite.effects.EffectsHandler;
import me.clip.ezrankslite.hooks.Hooks;
import me.clip.ezrankslite.hooks.VaultEco;
import me.clip.ezrankslite.hooks.VaultPerms;
import me.clip.ezrankslite.listeners.PlayerListener;
import me.clip.ezrankslite.metricslite.MetricsLite;
import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.multipliers.MultiplierFile;
import me.clip.ezrankslite.rankdata.PlayerRankupHandler;
import me.clip.ezrankslite.rankdata.RankHandler;
import me.clip.ezrankslite.rankdata.RankupFile;
import me.clip.ezrankslite.scoreboard.PlaceHolderHandler;
import me.clip.ezrankslite.scoreboard.ScoreboardIntervalTask;
import me.clip.ezrankslite.scoreboard.ScoreboardHandler;
import me.clip.ezrankslite.scoreboard.ScoreboardOptions;
import me.clip.ezrankslite.updater.Updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.earth2me.essentials.Essentials;

public class EZRanksLite extends JavaPlugin {
	
	private static EZRanksLite instance;
	
	private Config config = new Config(this);
	private RankupFile rankupfile = new RankupFile(this);
	private MultiplierFile multiplierfile = new MultiplierFile(this);
	private ConfigWrapper messagesFile = new ConfigWrapper(this, "", "messages.yml");

	private EffectsHandler effectsHandler = new EffectsHandler(this);
	private PlayerRankupHandler playerhandler = new PlayerRankupHandler(this);
	private RankHandler rankhandler = new RankHandler(this);
	private ScoreboardHandler boardHandler = new ScoreboardHandler(this);
	protected CostHandler multipliers = new CostHandler(this);
	private PlaceHolderHandler placeholders = new PlaceHolderHandler(this);

	private EZAdminCommand admincommand = new EZAdminCommand(this);
	private RankupCommand rankupcommand = new RankupCommand(this);
	private RanksCommand rankscommand = new RanksCommand(this);
	private ScoreboardToggleCommand togglecommand = new ScoreboardToggleCommand(this);
	private ScoreboardRefreshCommand refreshcommand = new ScoreboardRefreshCommand(this);
	private ScoreboardSwitchCommand switchcommand = new ScoreboardSwitchCommand(this);
	
	private VaultPerms vaultperms = new VaultPerms(this);
	private VaultEco vaulteco = new VaultEco(this);
	private Hooks hooks = new Hooks(this);

	public static boolean useVoteParty = false;
	
	private static boolean useSQLPerms;
	
	private static Essentials ess = null;
	
	//move to options object
	private static boolean debug;
	private static String servername;
	private static boolean fixThousands;
	private static boolean fixMillions;
	private static String millions;
	private static String billions;
	private static String thousands;
	private static String trillions;
	private static String quads;
	private static String ranksNo;
	private static String ranksYes;
	private static List<String> ranksHeader;
	private static List<String> ranksFooter;
	private static boolean useRanks;
	private static boolean useRankupCooldown;
	private static int rankupCooldownTime;
	//
	public static List<String> staffOnline;

	private static ScoreboardOptions sbOptions = null;
	private static boolean useScoreboard;
	private static int sbRefresh;
	
	private static BukkitTask sbTask = null;

	private static boolean checkUpdates;
	private Updater spigotUpdater = null;
	
	
	@Override
	public void onEnable() {

		if (!vaulteco.setupEconomy()) {
			debug(true, "Could not hook into an Economy plugin through Vault! Disabling EZRanksLite!");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("SQLPerms")) {
			
			debug(true, "SQLPerms was detected and will be used instead of Vault for permissions!");
			useSQLPerms = true;
			
		} else if (!vaultperms.setupVault()) {
			
			debug(true, "Could not detect Vault for permissions Hooking! Disabling EZRanksLite!");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		} 
		
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("VoteParty")) {
			
			debug(true, "EZRanksLite hooked into VoteParty!!");
			useVoteParty = true;
		}
		
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials")) {
			
			debug(true, "EZRanksLite hooked into Essentials!!");
			ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		}
		
		init();
		
		rankupfile.reload();
		rankupfile.save();
		
		messagesFile.createNewFile("Loading EZRanksLite messages.yml", "EZRanksLite messages file");
		loadMessages();
		
		getLogger().info(rankupfile.loadRankupsFromFile());
		
		multiplierfile.reload();
		multiplierfile.save();
		
		getLogger().info(multiplierfile.loadDiscounts()+" rankup cost discounts loaded!");
		getLogger().info(multiplierfile.loadMultipliers()+" rankup cost multipliers loaded!");
		
		registerCmds();
		registerListeners();
		
		if (!startMetricsLite()) {
			debug(false, "Could not start MetricsLite!");
		}
		
		if (useScoreboard) {
			debug(false, "Scoreboard features are enabled!");
			startScoreboardTask();
		}
		
		instance = this;
		
		checkUpdates = config.checkUpdates();
		
		staffOnline = new ArrayList<String>();
		
		if (checkUpdates) {
			spigotUpdater = new Updater(this);
			if (spigotUpdater.checkUpdate()) {
				getLogger().info("An update for EZRanksLite (EZRanksLite v"+Updater.getLatestVersion()+") is available at http://www.spigotmc.org/resources/ezrankslite.762/");
			}
			else {
				getLogger().info("You are running the latest version of EZRanksLite!");
			}
		}
	}

	@Override
	public void onDisable() {
		stopScoreboardTask();
		Bukkit.getScheduler().cancelTasks(this);
		
		sbOptions = null;
		staffOnline = null;
		ScoreboardHandler.staffToggled = null;
		boardHandler.clearBoards();
		instance = null;
	}

	private void registerCmds() {
		getCommand("ezadmin").setExecutor(admincommand);
		getCommand("rankup").setExecutor(rankupcommand);
		getCommand("ranks").setExecutor(rankscommand);
		getCommand("sbtoggle").setExecutor(togglecommand);
		getCommand("sbrefresh").setExecutor(refreshcommand);
		getCommand("sbswitch").setExecutor(switchcommand);
		debug(false, "Commands registered");
	}

	private void loadMessages() {
		Lang.setFile(messagesFile.getConfig());

		for (final Lang value : Lang.values()) {
			messagesFile.getConfig().addDefault(value.getPath(),
					value.getDefault());
		}

		messagesFile.getConfig().options().copyDefaults(true);
		messagesFile.saveConfig();
	}

	private void init() {
		config.loadDefaultConfiguration();
		debug = config.isDebug();
		servername = config.getServerName();
		useScoreboard = config.useScoreboard();
		fixThousands = config.fixThousands();
		fixMillions = config.fixMillions();
		thousands = config.getKFormat();
		millions = config.getMFormat();
		billions = config.getBFormat();
		trillions = config.getTFormat();
		quads = config.getQFormat();
		useRanks = config.useRanks();
		ranksYes = config.ranksAccess();
		ranksNo = config.ranksNoAccess();
		ranksHeader = config.ranksHeader();
		ranksFooter = config.ranksFooter();
		useRanks = config.useRanks();
		useRankupCooldown = config.useRankupCooldown();
		rankupCooldownTime = config.rankupCooldownTime();
		loadSBOptions();
		sbRefresh = config.getScoreboardRefreshTime();
	}

	public void loadOptions() {
		debug = config.isDebug();
		servername = config.getServerName();
		useScoreboard = config.useScoreboard();
		fixThousands = config.fixThousands();
		fixMillions = config.fixMillions();
		thousands = config.getKFormat();
		millions = config.getMFormat();
		billions = config.getBFormat();
		trillions = config.getTFormat();
		quads = config.getQFormat();
		useRanks = config.useRanks();
		ranksYes = config.ranksAccess();
		ranksNo = config.ranksNoAccess();
		ranksHeader = config.ranksHeader();
		ranksFooter = config.ranksFooter();
		useRankupCooldown = config.useRankupCooldown();
		rankupCooldownTime = config.rankupCooldownTime();
		loadSBOptions();
		sbRefresh = config.getScoreboardRefreshTime();
	}

	public ScoreboardOptions loadSBOptions() {
		ScoreboardOptions options = new ScoreboardOptions();
		options.setTitle(config.sbTitle());
		options.setText(config.sbDisplay());
		options.setRankup(config.canRankup());
		options.setNoRankups(config.noRankups());
		options.setpBarColor(config.getProgressBarColor());
		options.setpBarEndColor(config.getProgressBarEndColor());
		options.setpBarNeedsColor(config.getSbProgressBarNeedsColor());
		options.setDisabledWorlds(config.sbDisabledWorlds());
		options.setUseStaffScoreboard(config.useStaffSb());
		options.setStaffTitle(config.getStaffSbTitle());
		options.setStaffText(config.getStaffSbText());
		options.setpBarLeftChar(config.getSbProgressLeftChar());
		options.setpBarRightChar(config.getSbProgressRightChar());
		options.setpBarChar(config.getSbProgressBarChar());
		sbOptions = options;
		debug(false, "Scoreboard options loaded!");
		return options;
	}

	private void registerListeners() {
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	public VaultPerms getVault() {
		return vaultperms;
	}

	public VaultEco getEco() {
		return vaulteco;
	}

	public RankHandler getRankHandler() {
		return rankhandler;
	}

	public PlayerRankupHandler getPlayerhandler() {
		return playerhandler;
	}

	public EffectsHandler getEffectsHandler() {
		return effectsHandler;
	}

	public ScoreboardHandler getBoardHandler() {
		return boardHandler;
	}
	
	@Deprecated
	public ScoreboardHandler getBoardhandler() {
		return boardHandler;
	}

	public Config getConfigFile() {
		return config;
	}

	public RankupFile getRankFile() {
		return rankupfile;
	}
	
	public MultiplierFile getMultiplierConfig() {
		return multiplierfile;
	}
	
	public ConfigWrapper getMessagesFile() {
		return messagesFile;
	}

	public void debug(boolean severe, String msg) {
		if (severe) {
			getLogger().severe(msg);
		} else {
			if (debug) {
				getLogger().info(msg);
			}
		}
	}

	private boolean startMetricsLite() {
		try {
			MetricsLite ml = new MetricsLite(this);
			ml.start();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void startScoreboardTask() {
		if (useScoreboard) {
			if (sbTask == null) {
				sbTask = getServer().getScheduler().runTaskTimerAsynchronously(this,
						new ScoreboardIntervalTask(this), 0L, (20L * sbRefresh));
				debug(false, "Scoreboard refresh task has started and will refresh every "
								+ sbRefresh + " seconds.");
			} else {
				sbTask.cancel();
				sbTask = null;
				sbTask = getServer().getScheduler().runTaskTimerAsynchronously(this,
						new ScoreboardIntervalTask(this), 0L, (20L * sbRefresh));
				debug(false,
						"Scoreboard refresh task has started and will refresh every "
								+ sbRefresh + " seconds.");
			}
		}
	}

	public void stopScoreboardTask() {
		if (!useScoreboard && sbTask != null) {
			sbTask.cancel();
			sbTask = null;
			debug(false, "Scoreboard refresh task has been cancelled!");
		}
	}

	public void sms(Player p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void sms(CommandSender s, String msg) {
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void bcast(String msg) {
		Bukkit.broadcastMessage(ChatColor
				.translateAlternateColorCodes('&', msg));
	}
	
	public static String getDifference(double balance, double cost) {
		
		double diff = cost-balance;
		
		if (diff <= 0) {
			return "0";
		}
		return fixMoney(diff);
	}
	
	@Deprecated
	public static String fixMoney(double amount, String amt) {
		return fixMoney(amount);
	}

	public static String fixMoney(double amount) {

		if (amount >= 1000000000000000.0D) {
			
			if (fixMillions) {
				
				String format = String.format("%.2f", new Object[] { Double.valueOf(amount / 1000000000000000.0D) });
				
				int p = format.indexOf(".");
				
				String p2 = format.substring(p+1);
					
				if (p2.endsWith("0")){
					if (p2.startsWith("0")) {
						format = format.substring(0, format.length()-3);
					}
					else {
						format = format.substring(0, format.length()-1);
					}
				}
				
				return format+quads;
			} else {
				long send = (long) amount;
				return String.valueOf(send);
			}
		}
		
		else if (amount >= 1000000000000.0D) {
			if (fixMillions) {
				
				String format = String.format("%.2f", new Object[] { Double.valueOf(amount / 1000000000000.0D) });
				
				int p = format.indexOf(".");
				
				String p2 = format.substring(p+1);
					
				if (p2.endsWith("0")){
					
					if (p2.startsWith("0")) {
						format = format.substring(0, format.length()-3);
					}
					else {
						format = format.substring(0, format.length()-1);
					}
				}
				
				return format+trillions;
			} else {
				long send = (long) amount;
				return String.valueOf(send);
			}
		}
		
		else if (amount >= 1000000000.0D) {
			if (fixMillions) {
				
				String format = String.format("%.2f", new Object[] { Double.valueOf(amount / 1000000000.0D) });
				

				int p = format.indexOf(".");
				
				String p2 = format.substring(p+1);
					
				if (p2.endsWith("0")){
					
					if (p2.startsWith("0")) {
						format = format.substring(0, format.length()-3);
					}
					else {
						format = format.substring(0, format.length()-1);
					}
				}
				
				return format+billions;
				
			} else {
				long send = (long) amount;
				return String.valueOf(send);
			}
		}
		
		else if (amount >= 1000000.0D) {
			if (fixMillions) {
				String format = String.format("%.2f", new Object[] { Double.valueOf(amount / 1000000.0D) });
				
				int p = format.indexOf(".");
				
				String p2 = format.substring(p+1);
					
				if (p2.endsWith("0")){
					
					if (p2.startsWith("0")) {
						format = format.substring(0, format.length()-3);
					}
					else {
						format = format.substring(0, format.length()-1);
					}
				}
				
				return format+millions;
			} else {
				long send = (long) amount;
				return String.valueOf(send);
			}
		} 
		
		else if (amount >= 1000.0D) {
			if (fixThousands) {
				String format = String.format("%.2f", new Object[] { Double.valueOf(amount / 1000.0D) });

				int p = format.indexOf(".");
				
				String p2 = format.substring(p+1);
					
				if (p2.endsWith("0")){
					
					if (p2.startsWith("0")) {
						format = format.substring(0, format.length()-3);
					}
					else {
						format = format.substring(0, format.length()-1);
					}
				}
				
				return format+thousands;
			}
		}
		
		int amt = (int) amount;
		return String.valueOf(amt);
	}

	public boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public ScoreboardOptions getSbOptions() {
		return sbOptions;
	}
	
	public PlaceHolderHandler getPlaceholders() {
		return placeholders;
	}

	public String getRanksNo() {
		return ranksNo;
	}

	public String getRanksYes() {
		return ranksYes;
	}

	public List<String> getRanksHeader() {
		return ranksHeader;
	}

	public List<String> getRanksFooter() {
		return ranksFooter;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		EZRanksLite.servername = servername;
	}

	public boolean useScoreboard() {
		return useScoreboard;
	}

	public boolean useRanksCommand() {
		return useRanks;
	}

	public boolean useRankupCooldown() {
		return useRankupCooldown;
	}

	public int getRankupCooldownTime() {
		return rankupCooldownTime;
	}

	public boolean useSQLPerms() {
		return useSQLPerms;
	}
	
	public Hooks getHooks() {
		return hooks;
	}
	
	public boolean checkUpdates() {
		return checkUpdates;
	}
	
	public Updater getUpdater() {
		return spigotUpdater;
	}

	public static EZRanksLite i() {
		return instance;
	}
	
	public static EZRanksLite getInstance() {
		return instance;
	}
	
	public static EZAPI getAPI() {
		return new EZAPI(instance);
	}
	
	public Essentials getEssentials() {
		return ess;
	}
}
