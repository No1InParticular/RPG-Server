package com.no1inparticular.rpgserver.classes;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class None extends RPGClass {

	public None(String name, Player player) {
		super(name, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startPassive() {}
	@Override
	public void stopPassive() {}

	@Override
	public boolean useLeftAbility() {
		return false;
	}

	@Override
	public boolean useRightAbility() {
		return false;
	}

	@Override
	public void switchAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack createAbilityItem() {
		ItemStack item = new ItemStack(Material.BARRIER, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "No ability");
		meta.setLore(Arrays.asList(ChatColor.RED + "Please choose a class to get an ability."));
		
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		try {
			item.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		} catch (Exception e) {
			
		}
		return item;
	}

}
