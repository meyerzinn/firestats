package me.meyerzinn.firestats;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.firebase.client.Firebase;

public class FireStats extends JavaPlugin {

	public static Firebase fb;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		if (getConfig().getString("firebase.baseURL").length() < 1) {
			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			fb = new Firebase(getConfig().getString("firebase.baseURL") + "/firestats");
			Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		}
	}
	
}
