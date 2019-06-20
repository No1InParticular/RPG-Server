package com.no1inparticular.rpgserver.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.no1inparticular.rpgserver.Main;
import com.no1inparticular.rpgserver.guilds.GuildCMD;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

public class Create extends GuildCMD {

	public Create(String description, String usage, String permission) {
		super(description, usage, permission);
	}

	@Override
	public void execute(Player player, String[] args) {
		String guildName = args[0];
		if(Main.guildManager.getGuild(guildName) != null) {
			
			EconomyResponse r = Main.econ.withdrawPlayer(player, 5000);
            if(r.transactionSuccess()) {
    			Main.guildManager.createGuild(guildName, player.getUniqueId());
    			player.sendMessage(ChatColor.GREEN + "Guild has been created");
    			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ChatColor.GREEN + " has created the guild " + ChatColor.GOLD + "" + ChatColor.BOLD + guildName);
            } else {
    			player.sendMessage(ChatColor.RED + "You don't have enough money for this.");
            }
		} else {
			player.sendMessage(ChatColor.RED + "A guild with this name already exists");
		}
	}

}
