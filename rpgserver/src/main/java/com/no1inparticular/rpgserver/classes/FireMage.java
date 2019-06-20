package com.no1inparticular.rpgserver.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class FireMage extends RPGClass {

	public FireMage(String name, Player player) {
		super(name, player);
		rightCooldownTicks = 5*20;
		rightAbilityName = "Fireball";
	}

	@Override
	public ItemStack createAbilityItem() {
		ItemStack item = new ItemStack(Material.FISHING_ROD, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "FireMage's Staff");
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

	@Override
	public boolean useRightAbility() {
		World world = player.getWorld();
		player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 5);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 0.1f);
		Fireball fireball = (Fireball) world.spawnEntity(player.getEyeLocation(), EntityType.FIREBALL);
		fireball.setShooter(player);
		fireball.setVelocity(player.getLocation().getDirection().multiply(2));
		fireball.setIsIncendiary(true);
		fireball.setYield(2);
		return true;
	}

	@Override
	public void switchAbility() {
		// TODO Auto-generated method stub

	}

	public static Entity getTarget(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight( (Set<Material>) null, range);
        ArrayList<Location> sight = new ArrayList<Location>();
        for (int i = 0;i<sightBlock.size();i++)
            sight.add(sightBlock.get(i).getLocation());
        for (int i = 0;i<sight.size();i++) {
            for (int k = 0;k<entities.size();k++) {
                if (Math.abs(entities.get(k).getLocation().getX()-sight.get(i).getX())<1.3) {
                    if (Math.abs(entities.get(k).getLocation().getY()-sight.get(i).getY())<1.5) {
                        if (Math.abs(entities.get(k).getLocation().getZ()-sight.get(i).getZ())<1.3) {
                            return entities.get(k);
                        }
                    }
                }
            }
        }
        return null; //Return null/nothing if no entity was found
    }

}
