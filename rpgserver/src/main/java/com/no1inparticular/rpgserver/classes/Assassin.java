package com.no1inparticular.rpgserver.classes;

import java.util.Arrays;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.no1inparticular.rpgserver.Main;

import net.md_5.bungee.api.ChatColor;

public class Assassin extends RPGClass {

	public Assassin(String name, Player player) {
		super(name, player);
		rightCooldownTicks = 10*20;
		rightAbilityName = "Void Walk";
	}

	@Override
	public ItemStack createAbilityItem() {
		ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Assassin's Blade");
		meta.setLore(Arrays.asList(ChatColor.GREEN + "Right click to use " + rightAbilityName + " ability."));
		
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		item.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		return item;
	}

	@Override
	public void startPassive() {
		// TODO Auto-generated method stub
		
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

	@Override
	public boolean useRightAbility() {
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5*20, 0));
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 0.1f);
		new BukkitRunnable() {
			int x = 0;
			public void run() {
				if(x >= 5*20) {
					cancel();
				}
				player.getWorld().playEffect(player.getLocation().add(0, 0, 1), Effect.SMOKE, 1);
				x++;
			}
		}.runTaskTimer(Main.plugin, 0, 1);
		return true;
	}

	@Override
	public void switchAbility() {
		// TODO Auto-generated method stub
		
	}

}
