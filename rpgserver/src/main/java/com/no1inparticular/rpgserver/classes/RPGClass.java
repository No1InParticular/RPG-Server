package com.no1inparticular.rpgserver.classes;

import java.text.DecimalFormat;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.no1inparticular.rpgserver.Main;
import com.no1inparticular.rpgserver.apis.HotbarMessager;

import net.md_5.bungee.api.ChatColor;

public abstract class RPGClass implements Listener {

	private String name;
	protected Player player;
	protected boolean leftCooldownActive;
	protected boolean rightCooldownActive;
	protected double leftCooldownTicks;
	protected double leftCurrentTicks;
	protected double rightCooldownTicks;
	protected double rightCurrentTicks;
	protected BukkitTask passiveTask;
	protected String leftAbilityName = "";
	protected String rightAbilityName = "";
	
	public RPGClass(String name, Player player) {
		this.name = name;
		this.player = player;
		leftCooldownActive = false;
		rightCooldownActive = false;
		startPassive();
	}
	

	public String getName() {
		return name;
	}
	
	public void tryUseLeft() {
		if(!leftCooldownActive) {

			if(useLeftAbility()) {

				if(leftAbilityName != "") {
					for(Entity entity : player.getNearbyEntities(10, 10, 10)) {
						if(entity instanceof Player) {
							Player nearPlayer = (Player) entity;
							nearPlayer.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ChatColor.GREEN + " has used " + ChatColor.GOLD + "" + ChatColor.BOLD + leftAbilityName + ChatColor.GREEN + ".");
						}
					}
					player.sendMessage(ChatColor.GREEN + "You used " + ChatColor.GOLD + "" + ChatColor.BOLD + leftAbilityName + ChatColor.GREEN + ".");
				}
				
				
				leftCooldownActive = true;
				
				new BukkitRunnable() {
					@SuppressWarnings("deprecation")
					public void run() {
						
						if(leftCurrentTicks == leftCooldownTicks) {
							
							ItemStack item = player.getInventory().getItem(0);
							item.setDurability((short) 0);
							leftCurrentTicks = 0;
							leftCooldownActive = false;
							cancel();
							return;
						}
						
						ItemStack item = player.getInventory().getItem(0);
						
						double percent = (leftCurrentTicks / leftCooldownTicks)*100;

						double maxDura = item.getType().getMaxDurability();
						
						double current = (maxDura/100)*percent;
						
						double setTo = -(current-maxDura);
						
						item.setDurability((short) setTo);
					
						
						leftCurrentTicks++;
						
					}
				}.runTaskTimer(Main.plugin, 0, 1);
			}
			
			
		} else {
			// Cooldown
			DecimalFormat format = new DecimalFormat("0.0");
			HotbarMessager.sendHotBarMessage(player, ChatColor.RED + "This ability is on cooldown for " + format.format((leftCooldownTicks-leftCurrentTicks)/20) + " more seconds.");
		}
	}
	public void tryUseRight() {
		if(!rightCooldownActive) {

			if(useRightAbility()) {
				
				if(rightAbilityName != "") {
					for(Entity entity : player.getNearbyEntities(10, 10, 10)) {
						if(entity instanceof Player) {
							Player nearPlayer = (Player) entity;
							nearPlayer.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ChatColor.GREEN + " has used " + ChatColor.GOLD + "" + ChatColor.BOLD + rightAbilityName + ChatColor.GREEN + ".");
						}
					}
					player.sendMessage(ChatColor.GREEN + "You used " + ChatColor.GOLD + "" + ChatColor.BOLD + rightAbilityName + ChatColor.GREEN + ".");
				}
				
				rightCooldownActive = true;
				
				new BukkitRunnable() {
					@SuppressWarnings("deprecation")
					public void run() {
						
						if(rightCurrentTicks == rightCooldownTicks) {

							ItemStack item = player.getInventory().getItem(0);
							item.setDurability((short) 0);
							rightCurrentTicks = 0;
							rightCooldownActive = false;
							cancel();
							return;
						}
						
						ItemStack item = player.getInventory().getItem(0);
						
						double percent = (rightCurrentTicks / rightCooldownTicks)*100;

						double maxDura = item.getType().getMaxDurability();
						
						double current = (maxDura/100)*percent;
						
						double setTo = -(current-maxDura);
						
						item.setDurability((short) setTo);
						
						rightCurrentTicks++;
						
					}
				}.runTaskTimer(Main.plugin, 0, 1);
			} else {
				if(this instanceof Necromancer) {
					Necromancer necroClass = (Necromancer) this;
					if(necroClass.minions.size() >= 3) {
						HotbarMessager.sendHotBarMessage(player, ChatColor.RED + "You cannot spawn anymore minions.");
					}
				}
			}
		} else {
			// Cooldown
			DecimalFormat format = new DecimalFormat("0.0");
			HotbarMessager.sendHotBarMessage(player, ChatColor.RED + "This ability is on cooldown for " + format.format((rightCooldownTicks-rightCurrentTicks)/20) + " more seconds.");
		}
	}

	public abstract ItemStack createAbilityItem();
	
	public abstract void startPassive();
	public abstract void stopPassive();
	
	public abstract boolean useLeftAbility();
	public abstract boolean useRightAbility();
	
	public abstract void switchAbility();
}
