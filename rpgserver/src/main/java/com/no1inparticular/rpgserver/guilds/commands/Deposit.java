package com.no1inparticular.rpgserver.guilds.commands;

import org.bukkit.entity.Player;

import com.no1inparticular.rpgserver.Main;
import com.no1inparticular.rpgserver.guilds.Guild;
import com.no1inparticular.rpgserver.guilds.GuildCMD;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

public class Deposit extends GuildCMD {

	public Deposit(String description, String usage, String permission) {
		super(description, usage, permission);
	}

	@Override
	public void execute(Player player, String[] args) {
		String depositAmount = args[0];
		Guild guild = Main.guildManager.getPlayersGuild(player.getUniqueId());
		if(guild != null) {
			
			try {
				double amount = Double.parseDouble(depositAmount);
				EconomyResponse r = Main.econ.withdrawPlayer(player, amount);
	            if(r.transactionSuccess()) {
	    			player.sendMessage(ChatColor.GREEN + "Money has been deposited");
	            } else {
	    			player.sendMessage(ChatColor.RED + "You don't have enough money for this.");
	            }
			} catch (Exception e) {
    			player.sendMessage(ChatColor.RED + "Please enter a valid amount.");
			}
			
		} else {
			player.sendMessage(ChatColor.RED + "You aren't part of a guild");
		}
	}

}
