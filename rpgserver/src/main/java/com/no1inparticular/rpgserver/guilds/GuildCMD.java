package com.no1inparticular.rpgserver.guilds;

import org.bukkit.entity.Player;

public abstract class GuildCMD {

	public String description;
	public String usage;
	public String permission;
	
	public GuildCMD(String description, String usage, String permission) {
		this.description = description;
		this.usage = usage;
		this.permission = permission;
	}
	
	public abstract void execute(Player player, String[] args);
}
