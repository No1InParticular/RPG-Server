package com.no1inparticular.rpgserver.classes;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Ranger extends RPGClass {

	public Ranger(String name, Player player) {
		super(name, player);
		leftCooldownTicks = 5*20;
		rightCooldownTicks = 0.5*20;
		leftAbilityName = "Arrow Volley";
	}

	@Override
	public ItemStack createAbilityItem() {
		ItemStack item = new ItemStack(Material.BOW, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Ranger's Longbow");
		meta.setLore(Arrays.asList(ChatColor.GREEN + "Left click to use " + leftAbilityName + " ability."));
		
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		item.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		return item;
	}

	@Override
	public void startPassive() {
		player.setWalkSpeed(0.25f);
	}
	@Override
	public void stopPassive() {
		player.setWalkSpeed(0.2f);
	}

	@Override
	public boolean useLeftAbility() {
		World world = player.getWorld();
		int multiply = 5;
		int increaseWhen = 10;
		for(int x = 0; x <= 120; x++) {
			if(x == increaseWhen) {
				multiply += 2;
				increaseWhen += increaseWhen;
			}
			Location targetLoc = player.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(multiply));
			Location abovePlayer = player.getLocation().add(0, 20, 0);
			Random rnd = new Random();
			double random = (rnd.nextInt(11)+30)/10;
			Arrow arrow = world.spawnArrow(abovePlayer, targetLoc.subtract(abovePlayer).toVector(), (float) random, 5);
			arrow.setShooter(player);
		}
		return true;
	}

	@Override
	public boolean useRightAbility() {
		World world = player.getWorld();
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 2, 1);
		Arrow arrow = world.spawnArrow(player.getEyeLocation(), player.getLocation().getDirection(), (float) 3, 0);
		arrow.setShooter(player);
		return true;
	}

	@Override
	public void switchAbility() {
		// TODO Auto-generated method stub
	}

}
