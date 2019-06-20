package com.no1inparticular.rpgserver.guilds;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.no1inparticular.rpgserver.Main;

import net.md_5.bungee.api.ChatColor;

public class GuildManager {

	List<Guild> guilds;
	
	private File folder;
	
	private static class MyTimeTask extends TimerTask
	{

	    public void run()
	    {
	    	Main.guildManager.checkGuilds();
	    }
	}
	
	public GuildManager() {
		guilds = new ArrayList<Guild>();
		loadGuilds();
		// Interval to save data every 30 mins
		Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable() {
			public void run() {
				saveGuilds();
			}
		}, 0, 20*60*30);
		
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime todayNoon = LocalDateTime.of(today.toLocalDate(), LocalTime.NOON);
		LocalDateTime tomorrowNoon = LocalDateTime.of(today.plusDays(1).toLocalDate(), LocalTime.NOON);
		
		Timer timer = new Timer();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		if(today.isAfter(todayNoon)) {
			Instant instant = tomorrowNoon.toInstant(ZoneOffset.UTC);
		    Date date = Date.from(instant);
		    timer.schedule(new MyTimeTask(), date);
		    System.out.println("Todays noon " + format.format(date));
		} else {
			Instant instant = todayNoon.toInstant(ZoneOffset.UTC);
		    Date date = Date.from(instant);
		    timer.schedule(new MyTimeTask(), date);
		    System.out.println("Tomorrows noon " + format.format(date));
		}
	}
	
	public void loadGuilds() {
		folder = new File(Main.dataFolder + "/guilds");
		File[] guildList = folder.listFiles();
		if(guildList == null) { return; }
		for(File file : guildList) {
			if(file.isFile()) {
				FileConfiguration guildFile = YamlConfiguration.loadConfiguration(file);
				String name = file.getName();
				Double bank = guildFile.getDouble("bank");
				
				List<String> chunksCoords = guildFile.getStringList("chunks");
				List<Chunk> chunks = new ArrayList<Chunk>();
				for(String coords : chunksCoords) {
					Chunk chunk = Bukkit.getWorlds().get(0).getChunkAt(Integer.parseInt(coords.split(",")[0]), Integer.parseInt(coords.split(",")[1]));
					chunks.add(chunk);
				}
				
				UUID owner = (UUID) guildFile.get("owner");
				
				@SuppressWarnings("unchecked")
				List<UUID> members = (List<UUID>) guildFile.get("members");
				
				Guild guild = new Guild(name, bank, chunks, owner, members);
				guilds.add(guild);
			}
		}
	}
	
	public void saveGuilds() {
		for(Guild guild : guilds) {
			File file = new File(Main.dataFolder + "/guilds/" + guild.getName() + ".yml");
			
			FileConfiguration guildFile = YamlConfiguration.loadConfiguration(file);
			
			guildFile.set("bank", guild.bank);
			
			List<String> chunksCoords = guildFile.getStringList("chunks");
			for(Chunk chunk : guild.chunks) {
				chunksCoords.add(chunk.getX() + "," + chunk.getZ());
			}
			
			guildFile.set("chunks", chunksCoords);
			
			guildFile.set("owner", guild.getOwner());
			
			guildFile.set("members", guild.members);
		}
	}
	
	public void createGuild(String name, UUID owner) {
		Guild guild = new Guild(name, 0.0, new ArrayList<Chunk>(), owner, new ArrayList<UUID>());
		guilds.add(guild);
	}
	public void removeGuild(String name) {
		Guild guild = getGuild(name);
		guilds.remove(guild);
	}
	public void removeGuild(Guild guild) {
		guilds.remove(guild);
	}
	
	public Guild getGuild(String name) {
		for(Guild guild : guilds) {
			if(guild.getName().equalsIgnoreCase(name)) {
				return guild;
			}
		}
		return null;
	}
	
	public Guild getPlayersGuild(UUID uuid) {
		for(Guild guild : guilds) {
			if(guild.members.contains(uuid)) {
				return guild;
			}
		}
		return null;
	}
	
	public Guild getChunkOwner(Chunk chunk) {
		for(Guild guild : guilds) {
			if(guild.chunks.contains(chunk)) {
				return guild;
			}
		}
		return null;
	}
	
	public boolean claimChunk(Chunk chunk, Guild guild) {
		if(getChunkOwner(chunk) != null) {
			return false;
		}
		
		guild.chunks.add(chunk);
		return true;
	}
	
	public boolean unclaimChunk(Chunk chunk, Guild guild) {
		if(getChunkOwner(chunk) != null && getChunkOwner(chunk) == guild) {
			guild.chunks.remove(chunk);
			return true;
		}
		
		return false;
	}
	
	public boolean addMember(UUID uuid, Guild guild) {
		if(guild.members.contains(uuid)) {
			return false;
		} else {
			guild.members.add(uuid);
			return true;
		}
	}
	
	public boolean removeMember(UUID uuid, Guild guild) {
		if(guild.members.contains(uuid)) {
			guild.members.remove(uuid);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean deposit(Double amount, Guild guild) {
		try {
			guild.bank += amount;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void checkGuilds() {
		double dailyCost = 500;
		Iterator<Guild> guildIterator = guilds.iterator();
		while(guildIterator.hasNext()) {
			Guild guild = guildIterator.next();
			if(guild.bank >= dailyCost) {
				guild.bank -= dailyCost;
				if(guild.bank == 0) {
					Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + guild.getName() + ChatColor.RED + " has run out of money. If no money is deposited they will be removed tomorrow.");
				}
			} else {
				Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + guild.getName() + ChatColor.RED + " has gone bankrupt!");
				guildIterator.remove();
			}
		}
	}
}
