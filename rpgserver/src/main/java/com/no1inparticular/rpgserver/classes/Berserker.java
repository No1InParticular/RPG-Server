package com.no1inparticular.rpgserver.classes;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class Berserker extends RPGClass {

	public Berserker(String name, Player player) {
		super(name, player);
		rightCooldownTicks = 5*20;
		rightAbilityName = "Ground Slam";
	}


	@Override
	public ItemStack createAbilityItem() {
		ItemStack item = new ItemStack(Material.GOLDEN_AXE, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Beserker's Warhammer");
		meta.setLore(Arrays.asList(ChatColor.GREEN + "Right click to use " + rightAbilityName + " ability."));
		
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		item.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		return item;
	}
	
	@Override
	public void startPassive() {}
	@Override
	public void stopPassive() {}
	
	@Override
	public boolean useRightAbility() {
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 2, 0.1f);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 2, 0.1f);
		player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 5);
		for(Entity entity : player.getNearbyEntities(5, 2, 5)) {
			if(entity instanceof LivingEntity) {
				LivingEntity lEntity = (LivingEntity) entity;
				lEntity.setVelocity(lEntity.getLocation().toVector().subtract(player.getLocation().toVector()).add(new Vector(0,0.4,0)));
				lEntity.damage(5);
				if(lEntity instanceof Player) {
				}
			}
		}
		
		return true;
		
	}


	@Override
	public boolean useLeftAbility() {
		return false;
	}

	@Override
	public void switchAbility() {
		// TODO Auto-generated method stub
		
	}

}
