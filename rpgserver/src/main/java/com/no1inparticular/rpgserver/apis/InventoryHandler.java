package com.no1inparticular.rpgserver.apis;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class InventoryHandler {

	private static Inventory classSelector;
	
	public InventoryHandler() {
		classSelector = Bukkit.createInventory(null, 27, "Select a class");
		// Borders
		ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta blackMeta = blackGlass.getItemMeta();
		blackMeta.setDisplayName(" ");
		blackGlass.setItemMeta(blackMeta);

		ItemStack whiteGlass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
		ItemMeta whiteMeta = whiteGlass.getItemMeta();
		whiteMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "<-| |->");
		whiteMeta.setLore(Arrays.asList(ChatColor.GREEN + "Choose a class."));
		whiteGlass.setItemMeta(whiteMeta);
		
		for(int x = 0; x<=9; x++) {
			classSelector.setItem(x, blackGlass);
		}
		classSelector.setItem(9, blackGlass);
		classSelector.setItem(17, blackGlass);
		classSelector.setItem(18, blackGlass);
		classSelector.setItem(26, blackGlass);
		classSelector.setItem(22, blackGlass);
		
		classSelector.setItem(13, whiteGlass);
		
		// Class items

		// Assassin
		ItemStack assassin = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta assassinmeta = assassin.getItemMeta();
		assassinmeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Assassin");

		assassinmeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Passive Ability:", 
				ChatColor.YELLOW + "Back Stab",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Active Ability:",
				ChatColor.YELLOW + "Void Walk"
				));
		
		assassinmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		assassin.setItemMeta(assassinmeta);
		assassin.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		classSelector.setItem(10, assassin);
		
		// Berserker
		ItemStack berserker = new ItemStack(Material.GOLDEN_AXE, 1);
		ItemMeta berserkermeta = berserker.getItemMeta();
		berserkermeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Beserker");
		
		berserkermeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Passive Ability:", 
				ChatColor.YELLOW + "Thick Skin",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Active Ability:",
				ChatColor.YELLOW + "Ground Slam"
				));
		
		berserkermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		berserker.setItemMeta(berserkermeta);
		berserker.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		classSelector.setItem(11, berserker);

		// Mage
		ItemStack mage = new ItemStack(Material.FISHING_ROD, 1);
		ItemMeta magemeta = mage.getItemMeta();
		magemeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Fire Mage");
		
		magemeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Passive Ability:", 
				ChatColor.YELLOW + "Fire Resistance",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Active Ability:",
				ChatColor.YELLOW + "Fireball"
				));
		
		magemeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		mage.setItemMeta(magemeta);
		mage.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		classSelector.setItem(12, mage);

		// Necromancer
		ItemStack necromancer = new ItemStack(Material.GOLDEN_HOE, 1);
		ItemMeta necromancermeta = necromancer.getItemMeta();
		necromancermeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Necromancer");
		
		necromancermeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Passive Ability:", 
				ChatColor.YELLOW + "Minion Life Steal",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Active Ability:",
				ChatColor.YELLOW + "Summon Minion"
				));
		
		necromancermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		necromancer.setItemMeta(necromancermeta);
		necromancer.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		classSelector.setItem(14, necromancer);

		// Priest
		ItemStack priest = new ItemStack(Material.FISHING_ROD, 1);
		ItemMeta priestmeta = priest.getItemMeta();
		priestmeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Priest");
		
		priestmeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Passive Ability:", 
				ChatColor.YELLOW + "Healing Aura",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Active Ability:",
				ChatColor.YELLOW + "Heal Spell"
				));
		
		priestmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		priest.setItemMeta(priestmeta);
		priest.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		classSelector.setItem(15, priest);
		
		// Ranger
		ItemStack ranger = new ItemStack(Material.BOW, 1);
		ItemMeta rangermeta = ranger.getItemMeta();
		rangermeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Ranger");
		
		rangermeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Passive Ability:", 
				ChatColor.YELLOW + "Lightweight",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Active Ability:",
				ChatColor.YELLOW + "Arrow Volley"
				));
		
		rangermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ranger.setItemMeta(rangermeta);
		ranger.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		classSelector.setItem(16, ranger);
		
		// Class Information Item
		ItemStack infoHead = new ItemStack(Material.PAPER, 1);
		ItemMeta infoHeadMeta = infoHead.getItemMeta();
		infoHeadMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Class Information");
		
		// Assassin
		infoHeadMeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Back Stab:", 
				ChatColor.YELLOW + "Do more damage when",
				ChatColor.YELLOW + "attacking from behind.",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Void Walk:",
				ChatColor.YELLOW + "Go Invisible for a",
				ChatColor.YELLOW + "short amount of time."
				));
		infoHead.setItemMeta(infoHeadMeta);
		classSelector.setItem(19, infoHead);
		
		// Berserker
		infoHeadMeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Thick Skin:", 
				ChatColor.YELLOW + "Reduced incoming damage.",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Ground Slam:",
				ChatColor.YELLOW + "Deal damage and push back",
				ChatColor.YELLOW + "anything within a 5 block radius."
				));
		infoHead.setItemMeta(infoHeadMeta);
		classSelector.setItem(20, infoHead);
		
		// FireMage
		infoHeadMeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Fire Resistance:", 
				ChatColor.YELLOW + "Permanent fire resistance.",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Fireball:",
				ChatColor.YELLOW + "Launch a fireball in",
				ChatColor.YELLOW + "the direction you are looking."
				));
		infoHead.setItemMeta(infoHeadMeta);
		classSelector.setItem(21, infoHead);
		
		// Necromancer
		infoHeadMeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Minion Life Steal:", 
				ChatColor.YELLOW + "When a minion does damage",
				ChatColor.YELLOW + "gain this back as health.",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Summon Minion:",
				ChatColor.YELLOW + "Summon a minion to",
				ChatColor.YELLOW + "fight with you."
				));
		infoHead.setItemMeta(infoHeadMeta);
		classSelector.setItem(23, infoHead);
		
		// Priest
		infoHeadMeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Healing Aura:", 
				ChatColor.YELLOW + "Players around you get regeneration.",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Heal Spell:",
				ChatColor.YELLOW + "Heal a target within 5 blocks,",
				ChatColor.YELLOW + "Yourself if no target selected."
				));
		infoHead.setItemMeta(infoHeadMeta);
		classSelector.setItem(24, infoHead);
		
		// Ranger
		infoHeadMeta.setLore(Arrays.asList(
				ChatColor.GOLD + "" + ChatColor.BOLD + "Lightweight:", 
				ChatColor.YELLOW + "Move faster than others.",
				ChatColor.GOLD + "" + ChatColor.BOLD + "Arrow Volley:",
				ChatColor.YELLOW + "Summon a barrage of",
				ChatColor.YELLOW + "arrows infront of you."
				));
		infoHead.setItemMeta(infoHeadMeta);
		classSelector.setItem(25, infoHead);
	}
	
	public static Inventory getClassInv() {
		return classSelector;
	}
	
	public static ItemStack getItem(String name) {
		ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta blackMeta = blackGlass.getItemMeta();
		blackMeta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "X");
		blackGlass.setItemMeta(blackMeta);
		
		ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta redMeta = redGlass.getItemMeta();
		redMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Locked");
		redMeta.setLore(Arrays.asList(ChatColor.RED + "Get a backpack to unlock these slots."));
		redGlass.setItemMeta(redMeta);
		
		if(name.equalsIgnoreCase("redGlass")) {
			return redGlass;
		} else if (name.equalsIgnoreCase("blackGlass")) {
			return blackGlass;
		}
		return null;
	}
}
