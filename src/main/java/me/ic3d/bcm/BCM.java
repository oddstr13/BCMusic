package me.ic3d.bcm;
//Java Imports
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.sound.SoundManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;


public class BCM extends  JavaPlugin	 {
	
	
	
	private boolean UsePermissions;
	public static PermissionHandler Permissions;
	private void setupPermissions() {
	    Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
	    if (this.Permissions == null) {
	        if (test != null) {
	            UsePermissions = true;
	            this.Permissions = ((Permissions) test).getHandler();
	            System.out.println("[BCMusic] Version " + this.getDescription().getVersion() + " Permissions system detected!");
	        } else {
	            log.info("[BCMusic] Version " + this.getDescription().getVersion() + " Permissions system not detected, defaulting to OP");
	            UsePermissions = false;
	        }
	    }
	}
	
	public Plugin BC;
	
    public boolean hasPerm(Player p, String string) {
        if (UsePermissions) {
            return this.Permissions.has(p, string);
        }
        return true;
    }
	
	public Configuration config;
	public SoundManager sm;
    
	//Get the logger (console)
	public final Logger log = Logger.getLogger("Minecraft");
	
	private final BCMPL playerlistener = new BCMPL(this);
	
	//When the plugin is enabled:
	public void onEnable() {
		//Get the plugin manager and register events to the listener
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerlistener, Event.Priority.Normal, this);
		//Setup Permissions 
		setupPermissions();
		this.BC = pm.getPlugin("Spout");
//		SoundManager bc = SpoutManager.getSoundManager();
		sm = SpoutManager.getSoundManager();
	    config = this.getConfiguration();
	    config.getString("Music.tothaface.URL", "http://URLHERE");
	    config.getString("Music.tothaface.Node", "BCM.nodehere");
	    config.getString("Music.tothaface.GlobalNode", "BCM.Global.nodehere");
	    config.getString("JukeBoxes.iron_ingot.URL", "http://URLHERE");
	    config.getString("JukeBoxes.iron_ingot.Node", "BCM.nodehere");
	    config.save();
		this.getCommand("bcm").setExecutor(new BCMcmd(this));
		//Log some info
		log.info("[BCMusic] Version " + this.getDescription().getVersion() + " by IC3D enabled");
	}
	//When the plugin is disabled:
	public void onDisable() {
		//Log some info
		log.info("[BCMusic] Version " + this.getDescription().getVersion() + " by IC3D disabled");
	}
	
}
