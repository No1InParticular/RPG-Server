package com.no1inparticular.rpgserver.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Necromancer extends RPGClass {

	public List<Wolf> minions = new ArrayList<Wolf>();
	
	public Necromancer(String name, Player player) {
		super(name, player);
		rightCooldownTicks = 20*20;
		rightAbilityName = "Summon Minion";
	}

	@Override
	public ItemStack createAbilityItem() {
		ItemStack item = new ItemStack(Material.GOLDEN_HOE, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Necromancer's Scythe");
		meta.setLore(Arrays.asList(ChatColor.GREEN + "Right click to use " + rightAbilityName + " ability."));
		
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		item.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		return item;
	}

	@Override
	public void startPassive() {
		
	}

	@Override
	public void stopPassive() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean useLeftAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean useRightAbility() {
		if(minions.size() >= 3) {
			return false;
		} else {
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 2, 2);
			player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 5);
			Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
			wolf.setAngry(true);
			wolf.setOwner(player);
			wolf.setMaxHealth(5);
			wolf.setHealth(5);
			wolf.setCustomNameVisible(true);
			wolf.setCustomName(player.getName() + "'s Minion");
			minions.add(wolf);
			return true;
		}
	}

	@Override
	public void switchAbility() {
		// TODO Auto-generated method stub

	}
}
