 package me.PauMAVA.UhcPlugin;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UhcPluginCore extends JavaPlugin {
		
		
		/*Logger to handle plugin's output*/
		private static final Logger UhcLogger = Bukkit.getServer().getLogger();
		private static UhcPluginCore instance;
		
		@Override
		public void onEnable() {
			instance = this;
			UhcLogger.info("Enabled UhcPlugin!");
			this.saveDefaultConfig();
			UhcTeamsManager.createTeamsFile();
			this.getServer().getPluginManager().registerEvents(new EventsRegister(), this);
			this.getCommand("uhc").setExecutor(new UhcCmdHub());
			this.getCommand("uhc").setTabCompleter(new UhcCompleteTab());
			//this.getCommand("abort").setExecutor(new AbortCmd(this));
			
		}
		
		@Override
		public void onDisable() {
			UhcLogger.info("Disabled UhcPlugin!");
		}
		
		public static UhcPluginCore getInstance() {
			return instance;
		}
		
		public Logger getPluginLogger() {
			return UhcLogger;
		}
}
