package com.no1inparticular.rpgserver.players;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import com.no1inparticular.rpgserver.Main;
import com.no1inparticular.rpgserver.apis.InventoryHandler;
import com.no1inparticular.rpgserver.classes.Assassin;
import com.no1inparticular.rpgserver.classes.Berserker;
import com.no1inparticular.rpgserver.classes.FireMage;
import com.no1inparticular.rpgserver.classes.Necromancer;
import com.no1inparticular.rpgserver.classes.None;
import com.no1inparticular.rpgserver.classes.Priest;
import com.no1inparticular.rpgserver.classes.RPGClass;
import com.no1inparticular.rpgserver.classes.Ranger;

import net.md_5.bungee.api.ChatColor;

public class RPGPlayer {
	
	// Players data
	private double playerHealth;
	private int pvpKills;
	private int pvpDeaths;
	private RPGClass playerClass;
	
	// Boss bar to show health
	private BossBar healthBar;
	
	// Other data
	private UUID uuid;
	private File dataFile;
	private File folder;
	public BukkitTask saveTask;
	
	public RPGPlayer(UUID uuid) {
		this.uuid = uuid;
		folder = new File(Main.dataFolder + "/players");
		dataFile = new File(Main.dataFolder + "/players/" + uuid + ".yml");
		loadPlayer();
	}
	
	public boolean changeClass(String className) {
		if (className.equalsIgnoreCase("Berserker")) {
			playerClass.stopPassive();
			playerClass = new Berserker("Berserker", Bukkit.getPlayer(uuid));
			loadInventory();
			return true;
		} else if (className.equalsIgnoreCase("Ranger")) {
			playerClass.stopPassive();
			playerClass = new Ranger("Ranger", Bukkit.getPlayer(uuid));
			loadInventory();
			return true;
		} else if (className.equalsIgnoreCase("Assassin")) {
			playerClass.stopPassive();
			playerClass = new Assassin("Assassin", Bukkit.getPlayer(uuid));
			loadInventory();
			return true;
		} else if (className.equalsIgnoreCase("Priest")) {
			playerClass.stopPassive();
			playerClass = new Priest("Priest", Bukkit.getPlayer(uuid));
			loadInventory();
			return true;
		} else if (className.equalsIgnoreCase("FireMage")) {
			playerClass.stopPassive();
			playerClass = new FireMage("FireMage", Bukkit.getPlayer(uuid));
			loadInventory();
			return true;
		} else if (className.equalsIgnoreCase("Necromancer")) {
			playerClass.stopPassive();
			playerClass = new Necromancer("Necromancer", Bukkit.getPlayer(uuid));
			loadInventory();
			return true;
		} else if (className.equalsIgnoreCase("None")) {
			playerClass.stopPassive();
			playerClass = new None("None", Bukkit.getPlayer(uuid));
			loadInventory();
			return true;
		}
		return false;
	}
	
	public void loadInventory() {
		playerClass.startPassive();
		Player player = Bukkit.getPlayer(uuid);
		PlayerInventory playerInv = player.getInventory();
		
		
		int[] blackSlots = {15,16,17,24,26,33,34,35};
		int[] level1Slots = {18,19,20,21,22,23};
		int[] level2Slots = {9,10,11,12,13,14};
		
		ItemStack blackGlass = InventoryHandler.getItem("blackGlass");
		ItemStack redGlass = InventoryHandler.getItem("redGlass");
		
		for(int i : blackSlots) {
			playerInv.setItem(i, blackGlass);
		}
		
		int invLevel = 0;
		for(ItemStack armourPiece : playerInv.getArmorContents()) {
			if(armourPiece != null && armourPiece.hasItemMeta() && armourPiece.getItemMeta().hasLore()) {
				List<String> lore = armourPiece.getItemMeta().getLore();
				for(String loreLine : lore) {
					if(loreLine.equalsIgnoreCase(ChatColor.GREEN + "" + ChatColor.BOLD + "Backpack: " + ChatColor.GOLD + "Level 1")) {
						invLevel = 1;
					} else if(loreLine.equalsIgnoreCase(ChatColor.GREEN + "" + ChatColor.BOLD + "Backpack: " + ChatColor.GOLD + "Level 2")) {
						invLevel = 2;
					}
				}
			}
		}
		
		if(invLevel == 0) {
			// Set both rows to glass and drop items if there
			for(int i : level1Slots) {
				if(playerInv.getItem(i) != null && !playerInv.getItem(i).equals(redGlass)) {
					ItemStack item = playerInv.getItem(i);
					player.getWorld().dropItem(player.getLocation(), item);
				}
				playerInv.setItem(i, redGlass);
			}
			for(int i : level2Slots) {
				if(playerInv.getItem(i) != null && !playerInv.getItem(i).equals(redGlass)) {
					ItemStack item = playerInv.getItem(i);
					player.getWorld().dropItem(player.getLocation(), item);
				}
				playerInv.setItem(i, redGlass);
			}
		} else if (invLevel == 1) {
			// Set level 2 row to glass, but set level 1 row to air if glass is there
			for(int i : level2Slots) {
				if(playerInv.getItem(i) != null && !playerInv.getItem(i).equals(redGlass)) {
					ItemStack item = playerInv.getItem(i);
					player.getWorld().dropItem(player.getLocation(), item);
				}
				playerInv.setItem(i, redGlass);
			}
			for(int i : level1Slots) {
				if(playerInv.getItem(i) != null && playerInv.getItem(i).equals(redGlass)) {
					playerInv.setItem(i, new ItemStack(Material.AIR, 1));
				}
			}
		} else if (invLevel == 2) {
			// Set both rows to air if glass is there
			for(int i : level1Slots) {
				if(playerInv.getItem(i) != null && playerInv.getItem(i).equals(redGlass)) {
					playerInv.setItem(i, new ItemStack(Material.AIR, 1));
				}
			}
			for(int i : level2Slots) {
				if(playerInv.getItem(i) != null && playerInv.getItem(i).equals(redGlass)) {
					playerInv.setItem(i, new ItemStack(Material.AIR, 1));
				}
			}
		}
		
		playerInv.setItem(0, getPlayerClass().createAbilityItem());
	}
	
	public void loadPlayer() {
		healthBar = Bukkit.createBossBar(ChatColor.GREEN + "" + ChatColor.BOLD + "Health", BarColor.RED, BarStyle.SEGMENTED_20);
		healthBar.addPlayer(Bukkit.getPlayer(uuid));
		
		if(!dataFile.exists()) {
			try {
				folder.mkdir();
				dataFile.createNewFile();
				FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
				data.set("playerHealth", 100);
				data.set("pvpKills", 0);
				data.set("pvpDeaths", 0);
				data.set("playerClass", "None");
				data.save(dataFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
		setPlayerHealth(data.getInt("playerHealth"));
		setPvpKills(data.getInt("pvpKills"));
		setPvpDeaths(data.getInt("pvpDeaths"));
		
		String className = data.getString("playerClass");
		if (className.equalsIgnoreCase("Berserker")) {
			playerClass = new Berserker("Berserker", Bukkit.getPlayer(uuid));
		} else if (className.equalsIgnoreCase("Ranger")) {
			playerClass = new Ranger("Ranger", Bukkit.getPlayer(uuid));
		} else if (className.equalsIgnoreCase("Assassin")) {
			playerClass = new Assassin("Assassin", Bukkit.getPlayer(uuid));
		} else if (className.equalsIgnoreCase("Priest")) {
			playerClass = new Priest("Priest", Bukkit.getPlayer(uuid));
		} else if (className.equalsIgnoreCase("FireMage")) {
			playerClass = new FireMage("FireMage", Bukkit.getPlayer(uuid));
		} else if (className.equalsIgnoreCase("Necromancer")) {
			playerClass = new Necromancer("Necromancer", Bukkit.getPlayer(uuid));
		} else {
			playerClass = new None("None", Bukkit.getPlayer(uuid));
		}
		
		loadInventory();
		
		// Interval to save data every 30 mins
		saveTask = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable() {
			public void run() {
				savePlayer();
			}
		}, 0, 20*60*30);
		
	}
	
	public void savePlayer() {
		FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
		data.set("playerHealth", getPlayerHealth());
		data.set("pvpKills", getPvpKills());
		data.set("pvpDeaths", getPvpDeaths());
		data.set("playerClass", getPlayerClass().getName());
		try {
			data.save(dataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void damagePlayer(double damage) {
		
		if(getPlayerClass().getName().equals("Berserker")) {
			damage = damage*0.8;
		}
		
		Bukkit.getPlayer(uuid).sendMessage(getPlayerHealth() + "-" + damage);
		if(getPlayerHealth() - damage <= 0) {
			setPlayerHealth(0);
			Bukkit.getPlayer(uuid).setHealth(0);
		} else {
			setPlayerHealth(getPlayerHealth() - damage);
			Bukkit.getPlayer(uuid).setHealth(getPlayerHealth()/5);
		}
		Bukkit.getPlayer(uuid).sendMessage("H:" + getPlayerHealth());
	}
	public void healPlayer(double healAmount) {
		if(getPlayerHealth() + healAmount >= 100) {
			// Max health
			setPlayerHealth(100);
			Bukkit.getPlayer(uuid).setHealth(20);
		} else {
			setPlayerHealth(getPlayerHealth() + healAmount);
			Bukkit.getPlayer(uuid).setHealth(getPlayerHealth()/5);
		}
	}
	public void addPvpKill() {
		this.pvpKills++;
	}
	public void addPvpDeath() {
		this.pvpDeaths++;
	}
	
	// Getters and Setters
	public double getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(double d) {
		healthBar.setProgress(d/100);
		this.playerHealth = d;
	}

	public RPGClass getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(RPGClass playerClass) {
		this.playerClass = playerClass;
	}

	public BossBar getHealthBar() {
		return healthBar;
	}
	public int getPvpKills() {
		return pvpKills;
	}
	
	public void setPvpKills(int kills) {
		this.pvpKills = kills;
	}


	public int getPvpDeaths() {
		return pvpDeaths;
	}
	
	public void setPvpDeaths(int deaths) {
		this.pvpDeaths = deaths;
	}

	
	
}
