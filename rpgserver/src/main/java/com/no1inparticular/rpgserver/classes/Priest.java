package com.no1inparticular.rpgserver.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.no1inparticular.rpgserver.Main;

import net.md_5.bungee.api.ChatColor;

public class Priest extends RPGClass {

	public Priest(String name, Player player) {
		super(name, player);
		rightCooldownTicks = 5*20;
		rightAbilityName = "Heal Spell";
	}

	@Override
	public ItemStack createAbilityItem() {
		ItemStack item = new ItemStack(Material.FISHING_ROD, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Priest's Staff");
		meta.setLore(Arrays.asList(ChatColor.GREEN + "Right click to use " + rightAbilityName + " ability."));
		
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		item.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		return item;
	}

	@Override
	public void startPassive() {
		passiveTask = new BukkitRunnable() {
			public void run() {
				for(Entity entity : player.getNearbyEntities(5, 2, 5)) {
					if(entity instanceof Player) {
						Player lEntity = (Player) entity;
						if(lEntity instanceof Player) {
							lEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5*20, 0));
						}
					}
				}
			}
		}.runTaskTimer(Main.plugin, 20, 5*20);
	}

	@Override
	public void stopPassive() {
		passiveTask.cancel();
	}

	@Override
	public boolean useLeftAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean useRightAbility() {
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 2, 2);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
		player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 5);
		Entity target = getTarget(player,5);
		if(target != null && target instanceof Player) {
			// Heal target
			Player targetP = (Player) target;
			Main.plugin.getPlayer(targetP.getUniqueId()).healPlayer(20);
		} else {
			// Heal self
			Main.plugin.getPlayer(player.getUniqueId()).healPlayer(20);
		}
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
