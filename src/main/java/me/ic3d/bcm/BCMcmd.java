package me.ic3d.bcm;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

public class BCMcmd implements CommandExecutor {
	private final BCM plugin;

  	public BCMcmd(BCM instance) {
	  plugin = instance;
  	}
  	//Get the logger
  	private static final Logger log = Logger.getLogger("Minecraft");
  	
  	//The main part of a CommandExecutor class
  	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
  		if(!(sender instanceof Player)) {
  			sender.sendMessage("[BCMusic] You need to be a player!");
  			return true;
  		}
  		Player player = (Player) sender;
  		if(args.length == 0) {
  			player.sendMessage("Usage: /bcm <song> <global>");
  		}
  		if(args.length == 1) {
  			String rawName = args[0].toLowerCase();
  			String node = plugin.config.getString("Music." + rawName + ".Node");
  			String url = plugin.config.getString("Music." + rawName + ".URL");
  			Boolean hasPerm = plugin.hasPerm(player, node);
  			if(!hasPerm == true) {
  				player.sendMessage(ChatColor.RED + "You don't have permission to play that song!");
  				return true;
  			}
  			SpoutPlayer cp = (SpoutPlayer) player;
  			if(cp.isSpoutCraftEnabled() == false) {
  				cp.sendMessage(ChatColor.RED + "You don't have Spoutcraft installed!");
  			}
  			plugin.sm.playCustomMusic(plugin, cp, url, true);
  		}
  		if(args.length == 2) {
  			String rawName = args[0].toLowerCase();
  			String global = args[1].toLowerCase();
  			if(!global.equals("global")) {
  				return true;
  			}
  			String node = plugin.config.getString("Music." + rawName + ".GlobalNode");
  			String url = plugin.config.getString("Music." + rawName + ".URL");
  			Boolean hasPerm = plugin.hasPerm(player, node);
  			if(!hasPerm == true) {
  				player.sendMessage(ChatColor.RED + "You don't have permission to play that song!");
  				return true;
  			}
  			plugin.sm.playGlobalCustomMusic(plugin, url, true);
  		}
  		return true;
  	}
}
