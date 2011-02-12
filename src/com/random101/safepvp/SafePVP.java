package com.random101.safepvp;

import java.io.File;

import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;


public class SafePVP extends JavaPlugin {
	private final SafePVPListener playerListener = new SafePVPListener(this);
	String pluginName = "SafePVP";
	String version = "1.6.1";

	public SafePVP(PluginLoader pluginLoader, Server instance,
			PluginDescriptionFile desc, File folder, File plugin,
			ClassLoader cLoader) {
		super(pluginLoader, instance, desc, folder, plugin, cLoader);

		registerEvents();
	}

	public void onDisable() {
		System.out.println(pluginName + " v" + version + " has been enabled.");
	}

	public void onEnable() {
		registerEvents();
		System.out.println(pluginName + " v" + version + " has been enabled.");
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);   
		getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGEDBY_ENTITY, playerListener, Priority.High, this);
	}
}
