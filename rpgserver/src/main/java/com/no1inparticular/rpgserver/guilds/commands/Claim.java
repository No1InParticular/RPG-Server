package com.no1inparticular.rpgserver.guilds.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.no1inparticular.rpgserver.Main;
import com.no1inparticular.rpgserver.guilds.Guild;
import com.no1inparticular.rpgserver.guilds.GuildCMD;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

public class Claim extends GuildCMD {

	public Claim(String description, String usage, String permission) {
		super(description, usage, permission);
	}

	public void execute(Player player, String[] args) {
		UUID uuid = player.getUniqueId();
		Guild guild = Main.guildManager.getPlayersGuild(uuid);
		if(guild != null) {
			if(guild.getOwner().equals(uuid)) {
				EconomyResponse r = Main.econ.withdrawPlayer(player, 5000);
	            if(r.transactionSuccess()) {
					if (Main.guildManager.claimChunk(player.getLocation().getChunk(), guild)) {
		    			Bukkit.broadcastMessage(ChatColor.GREEN + "Chunk has been claimed.");
					} else {
		    			Bukkit.broadcastMessage(ChatColor.RED + "This chunk is owned by another guild.");
					}
	            } else {
	    			player.sendMessage(ChatColor.RED + "You don't have enough money for this.");
	            }
        	} else {
        		player.sendMessage(ChatColor.RED + "You aren't the leader of this guild");
			}
		} else {
			player.sendMessage(ChatColor.RED + "You aren't part of a guild");
		}
	}

}
