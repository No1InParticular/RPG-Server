package com.no1inparticular.rpgserver;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.no1inparticular.rpgserver.apis.InventoryHandler;
import com.no1inparticular.rpgserver.apis.armourequip.ArmourListener;
import com.no1inparticular.rpgserver.classes.Necromancer;
import com.no1inparticular.rpgserver.guilds.GuildCMD;
import com.no1inparticular.rpgserver.guilds.GuildEvents;
import com.no1inparticular.rpgserver.guilds.GuildManager;
import com.no1inparticular.rpgserver.guilds.commands.Claim;
import com.no1inparticular.rpgserver.guilds.commands.Create;
import com.no1inparticular.rpgserver.guilds.commands.Delete;
import com.no1inparticular.rpgserver.guilds.commands.Deposit;
import com.no1inparticular.rpgserver.guilds.commands.Unclaim;
import com.no1inparticular.rpgserver.players.PlayerEvents;
import com.no1inparticular.rpgserver.players.RPGPlayer;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener {

	public static File dataFolder;
	public static Main plugin;
	public static GuildManager guildManager;
	public static Economy econ = null;
	
	private HashMap<UUID, RPGPlayer> players;
	private HashMap<String, GuildCMD> guildCMDS;
	
	public void onEnable() {
		if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		plugin = this;
		guildManager = new GuildManager();
		new InventoryHandler();
		dataFolder = getDataFolder();
		setPlayers(new HashMap<UUID, RPGPlayer>());
		
		guildCMDS = new HashMap<String, GuildCMD>();
		guildCMDS.put("create", new Create("Create a guild", "/guild create <name>", "guild.use"));
		guildCMDS.put("deposit", new Deposit("Deposit money into the guilds bank", "/guild deposit <amount>", "guild.use"));
		guildCMDS.put("claim", new Claim("Claim a chunk", "/guild claim", "guild.use"));
		guildCMDS.put("unclaim", new Unclaim("Unclaim a chunk", "/guild unclaim", "guild.use"));
		guildCMDS.put("delete", new Delete("Delete your guild", "/guild delete", "guild.use"));
		
		this.getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new GuildEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new ArmourListener(getConfig().getStringList("blocked")), this);
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			getPlayers().put(uuid, new RPGPlayer(uuid));
		}
	}
	
	public void onDisable() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.getOpenInventory() != null && player.getOpenInventory().getTopInventory() != null && player.getOpenInventory().getTopInventory().equals(InventoryHandler.getClassInv())) {
				player.closeInventory();
			}
			UUID uuid = player.getUniqueId();
			getPlayers().get(uuid).saveTask.cancel();
			getPlayers().get(uuid).savePlayer();
			getPlayers().get(uuid).getHealthBar().removeAll();
			getPlayers().get(uuid).loadInventory();
			
			if(getPlayers().get(uuid).getPlayerClass().getName().equalsIgnoreCase("Necromancer")) {
				Necromancer beastClass = (Necromancer) getPlayers().get(uuid).getPlayerClass();
				for(Wolf wolf : beastClass.minions) {
					wolf.remove();
				}
			}
			
			getPlayers().remove(uuid);
			
			
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("class")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.openInventory(InventoryHandler.getClassInv());
			} else {
				sender.sendMessage(ChatColor.RED + "You must be a player to select a class.");
			}
		} else if (cmd.getName().equalsIgnoreCase("guild")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
					String message = "&aGuild Help:";
					message += "\n&a- &6/guild help&a: &fShow this message";
					for(String guildC : guildCMDS.keySet()) {
						GuildCMD guildCMD = guildCMDS.get(guildC);
						message += "\n&a- &6" + guildCMD.usage + "&a: &f" + guildCMD.description;
					}
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
				} else {
					if(guildCMDS.containsKey(args[0])) {
						GuildCMD guildCMD = guildCMDS.get(args[0]);
						if(player.hasPermission(guildCMD.permission)) {
							int argCount = guildCMD.usage.split(" ").length-1;
							if(args.length >= argCount) {
					            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
								guildCMDS.get(args[0]).execute(player, newArgs);
							} else {
								player.sendMessage(ChatColor.RED + guildCMD.usage);
							}
						} else {
							player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Unknown guild command, please do /guild help");
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You must be a player to do this.");
			}
		}
		return false;
	}
	
	public RPGPlayer getPlayer(UUID uuid) {
		return getPlayers().get(uuid);
	}

	public HashMap<UUID, RPGPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<UUID, RPGPlayer> players) {
		this.players = players;
	}
	public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
}
