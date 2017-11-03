package com.digiturtle.chat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalChat extends JavaPlugin {
	
	private String ip, protocol;
	
	private int port;

	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		protocol = getConfig().getString("protocol", "http");
		ip = getConfig().getString("ip", "127.0.0.1");
		port = getConfig().getInt("port", 8000);
		getServer().getPluginManager().registerEvents(new ChatAdapter(), this);
	}
	
	public void onDisable() {
		saveConfig();
	}
	
	private void submitMessage(String user, String message) throws Exception {
		Calendar calendar = Calendar.getInstance();
		String urlText = protocol + "://" + ip + ":" + port + "/chat/create?user=" + user + "&date=" + 
				(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "_" 
						+ calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.SECOND))
				+ "&text=" + URLEncoder.encode(message, "UTF-8");
		getServer().getLogger().log(Level.INFO, "Logging message: " + urlText);
		URL url = new URL(urlText);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		InputStream in = conn.getInputStream();
		String reply = new BufferedReader(new InputStreamReader(in)).readLine();
		if (!reply.equalsIgnoreCase("Success!")) {
			IllegalStateException e = new IllegalStateException();
			e.printStackTrace();
		}
	}
	
	public class ChatAdapter implements Listener {
		
		@EventHandler
		public void onChat(AsyncPlayerChatEvent event) {
			try {
				submitMessage(event.getPlayer().getDisplayName(), event.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
