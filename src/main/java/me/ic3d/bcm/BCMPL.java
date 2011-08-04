package me.ic3d.bcm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.Door;
import org.bukkit.util.Vector;

public class BCMPL extends PlayerListener {

	public static BCM plugin;

	public BCMPL(BCM instance) {
		plugin = instance;
	}
	
	public HashMap<Block, Material> jukeboxes = new HashMap<Block, Material>();
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			if(block.getType() == Material.JUKEBOX) {
				String item = player.getItemInHand().getType().name().toLowerCase();
				String url = plugin.config.getString("JukeBoxes." + item + ".URL", "null");
				if(url.equals("null")) {
					return;
				} else {
					String node = plugin.config.getString("JukeBoxes." + item + ".Node", "null");
					if(!(node.equals("null"))) {
						if(plugin.hasPerm(player, node)) {
							plugin.sm.playGlobalCustomSoundEffect(plugin.BC, url, true, block.getLocation());
							this.jukeboxes.put(block, player.getItemInHand().getType());
							PlayerInventory inv = player.getInventory();
							ItemStack removed = inv.getItemInHand();
							if(removed.getAmount() > 1) {
								removed.setAmount(removed.getAmount() - 1);
							} else {
								inv.remove(removed);
								player.updateInventory();
							}
							event.setCancelled(true);
						} else {
							player.sendMessage(ChatColor.RED + "You don't have permission to play that song");
							return;
						}
					}
				}
			}
		}
		if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			if(block.getType().equals(Material.JUKEBOX)) {
				if(this.jukeboxes.containsKey(block)) {
					block.getWorld().dropItem(block.getLocation(), new ItemStack(this.jukeboxes.get(block).getId(), 1));
					this.jukeboxes.remove(block);
					event.setCancelled(true);
				}
			}
		}
	}
	
}
