package com.random101.safepvp;

import java.io.File;

import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;


public class SafePVP extends JavaPlugin {
	private final SafePVPListener1 playerListener = new SafePVPListener1(this);
	private final SafePVPListener1 entityListener = new SafePVPListener1(this);
	
	String pluginName = "SafePVP";
	String version = "1.7.1";

	public SafePVP(PluginLoader pluginLoader, Server instance,
			PluginDescriptionFile desc, File folder, File plugin,
			ClassLoader cLoader) {
		super(pluginLoader, instance, desc, folder, plugin, cLoader);

		registerEvents();
	}

	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!" );
	}

	public void onEnable() {
		registerEvents();
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);   
		getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DEATH, entityListener, Priority.High, this);
		getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGEDBY_ENTITY, entityListener, Priority.High, this);
		
	}
}
