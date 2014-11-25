package me.clip.ezrankslite.updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import me.clip.ezrankslite.EZRanksLite;

public class Updater {

		private EZRanksLite plugin;
		
	    final int resourceId = 762;
	    private static String latestVersion = "";
	    private static boolean updateAvailable = false;

	    public Updater(EZRanksLite i) {
	    	plugin = i;
	    }

	    private String getSpigotVersion() {
	        try {
	            HttpURLConnection connection = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
	            connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
	            connection.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource="+resourceId)
	                            .getBytes("UTF-8"));
	            String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
	            if (version.length() <= 7) {
	                return version;
	            }
	        } catch (Exception ex) {
	            plugin.getLogger().info("Failed to check for a update on spigot.");
	        }
	        return null;
	    }

	    private boolean checkHigher(String currentVersion, String newVersion) {
	        String current = toReadable(currentVersion);
	        String newVers = toReadable(newVersion);
	        return current.compareTo(newVers) < 0;
	    }

	    public boolean checkUpdate() {
	        if (getLatestVersion() != "")
	            return true;
	        String version = getSpigotVersion();
	        if (version != null) {
	            if (checkHigher(plugin.getDescription().getVersion(), version)) {
	                latestVersion = version;
	                updateAvailable = true;
	                return true;
	            }
	        }
	        return false;
	    }

	    public static boolean updateAvailable() {
	        return updateAvailable;
	    }

	    public static String getLatestVersion() {
	        return latestVersion;
	    }

	    private String toReadable(String version) {
	        String[] split = Pattern.compile(".", Pattern.LITERAL).split(
	                version.replace("v", ""));
	        version = "";
	        for (String s : split)
	            version += String.format("%4s", s);
	        return version;
	    }
		 
}
