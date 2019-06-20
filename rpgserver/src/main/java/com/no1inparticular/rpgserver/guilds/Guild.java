package com.no1inparticular.rpgserver.guilds;

import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;

public class Guild {

	private String name;
	Double bank;
	List<Chunk> chunks;
	private UUID owner;
	List<UUID> members;
	
	public Guild(String name, Double bank, List<Chunk> chunks, UUID owner, List<UUID> members) {
		this.setName(name);
		this.bank = bank;
		this.chunks = chunks;
		this.owner = owner;
		this.members = members;
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
