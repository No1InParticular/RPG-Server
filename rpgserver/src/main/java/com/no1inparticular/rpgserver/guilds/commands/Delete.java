package com.no1inparticular.rpgserver.guilds.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.no1inparticular.rpgserver.Main;
import com.no1inparticular.rpgserver.guilds.Guild;
import com.no1inparticular.rpgserver.guilds.GuildCMD;

import net.md_5.bungee.api.ChatColor;

public class Delete extends GuildCMD {

	public Delete(String description, String usage, String permission) {
		super(description, usage, permission);
	}

	@Override
	public void execute(Player player, String[] args) {
		UUID uuid = player.getUniqueId();
		Guild guild = Main.guildManager.getPlayersGuild(uuid);
		if(guild != null) {
			if(guild.getOwner().equals(uuid)) {
				Main.guildManager.removeGuild(guild);
    			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ChatColor.GREEN + " has disbanded his guild " + ChatColor.GOLD + "" + ChatColor.BOLD + guild.getName());
			} else {
				player.sendMessage(ChatColor.RED + "You aren't the leader of this guild");
			}
		} else {
			player.sendMessage(ChatColor.RED + "You aren't part of a guild");
		}
	}

}
