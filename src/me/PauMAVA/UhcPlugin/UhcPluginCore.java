package me.PauMAVA.UhcPlugin;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UhcPluginCore extends JavaPlugin {
		
		
		/*Logger to handle plugin's output*/
		public static final Logger UhcLogger = Bukkit.getServer().getLogger();
	
		@Override
		public void onEnable() {
			UhcLogger.info("Enabled UhcPlugin!");
			this.getServer().getPluginManager().registerEvents(new EventsRegister(), this);
			this.getCommand("uhcstart").setExecutor(new UhcStartCmd(this));
			this.getCommand("abort").setExecutor(new AbortCmd(this));
			
		}
		
		@Override
		public void onDisable() {
			UhcLogger.info("Disabled UhcPlugin!");
		}
}
