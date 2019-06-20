package com.no1inparticular.rpgserver.guilds;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.no1inparticular.rpgserver.Main;
import com.no1inparticular.rpgserver.apis.HotbarMessager;

import net.md_5.bungee.api.ChatColor;

public class GuildEvents implements Listener {

	Main main;
	public GuildEvents(Main instance) {
		main = instance;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.getFrom().getChunk() != event.getTo().getChunk()) {
			Guild guildFrom = Main.guildManager.getChunkOwner(event.getFrom().getChunk());
			Guild guildTo = Main.guildManager.getChunkOwner(event.getTo().getChunk());
			if(guildFrom != guildTo) {
				event.getPlayer().sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + guildTo.getName(), "", 20, 20, 20);
			}
		}
	}
	
	// Taking peoples money when they earn it
	// Need to find an event to use
//	public void onTransactionEvent( event) {
//		double amount = event.getTransaction().getAmount();
//		if(amount > 0) {
//			OfflinePlayer player = event.getTransaction().getReceiver().tryCastToPlayer();
//			if(player != null) {
//				Guild guild = Main.guildManager.getPlayersGuild(player.getUniqueId());
//				if(guild != null) {
//					guild.bank += amount*0.1;
//					Main.econ.withdrawPlayer(player, amount*0.1);
//					HotbarMessager.sendHotBarMessage(player.getPlayer(), ChatColor.GREEN + "10% of your earnings just went to the guild.");
//				}
//			}
//			
//		}
//		
//	}
	
	// Stop people doing stuff outside their guild
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.PHYSICAL) {
			if(Main.guildManager.getChunkOwner(event.getPlayer().getLocation().getChunk()) != Main.guildManager.getPlayersGuild(event.getPlayer().getUniqueId())) {
				event.setCancelled(true);
				HotbarMessager.sendHotBarMessage(event.getPlayer(), ChatColor.RED + "You cannot interact here, this is not your guild's territory.");
			}
		}
	}
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		if(event.getPlayer() != null) {
			if(Main.guildManager.getChunkOwner(event.getPlayer().getLocation().getChunk()) != Main.guildManager.getPlayersGuild(event.getPlayer().getUniqueId())) {
				event.setCancelled(true);
				HotbarMessager.sendHotBarMessage(event.getPlayer(), ChatColor.RED + "You cannot interact here, this is not your guild's territory.");
			}
		}
	}
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		if(event.getPlayer() != null) {
			if(Main.guildManager.getChunkOwner(event.getPlayer().getLocation().getChunk()) != Main.guildManager.getPlayersGuild(event.getPlayer().getUniqueId())) {
				event.setCancelled(true);
				HotbarMessager.sendHotBarMessage(event.getPlayer(), ChatColor.RED + "You cannot interact here, this is not your guild's territory.");
			}
		}
	}
}
