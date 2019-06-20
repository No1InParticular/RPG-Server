package com.no1inparticular.rpgserver.players;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.no1inparticular.rpgserver.Main;
import com.no1inparticular.rpgserver.apis.InventoryHandler;
import com.no1inparticular.rpgserver.apis.armourequip.ArmourEquipEvent;
import com.no1inparticular.rpgserver.apis.armourequip.ArmourType;
import com.no1inparticular.rpgserver.classes.Necromancer;

import net.md_5.bungee.api.ChatColor;

public class PlayerEvents implements Listener {

	private Main main;

	public PlayerEvents(Main instance) {
		main = instance;
	}

	// Join to make player object
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		main.getPlayers().put(uuid, new RPGPlayer(uuid));
		main.getPlayers().get(event.getPlayer().getUniqueId()).loadInventory();
	}

	// Leave to save information
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		main.getPlayers().get(uuid).saveTask.cancel();
		main.getPlayers().get(uuid).savePlayer();
		main.getPlayers().remove(uuid);
		
	}

	// Interact to handle left or right click ability
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		if (event.getHand() == EquipmentSlot.HAND) {
			if (player.getInventory().getHeldItemSlot() == 0) {
				if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
					main.getPlayers().get(uuid).getPlayerClass().tryUseLeft();
				} else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					event.setCancelled(true);
					main.getPlayers().get(uuid).getPlayerClass().tryUseRight();
				}
			}
		}
	}
	
	// Healing
	@EventHandler
	public void onPlayerHeal(EntityRegainHealthEvent event) {
		if(event.getEntity() instanceof Player) {
			event.setCancelled(true);
			Player player = (Player) event.getEntity();
			main.getPlayer(player.getUniqueId()).healPlayer(event.getAmount());
		}
	}
	
	// Entity death event
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntity() instanceof Wolf) {
			Wolf wolf = (Wolf) event.getEntity();
			if(wolf.getOwner() != null && wolf.getOwner() instanceof Player) {
				Player owner = (Player) wolf.getOwner();

				if(main.getPlayers().get(owner.getUniqueId()).getPlayerClass().getName().equals("Necromancer")) {
					Necromancer beastClass = (Necromancer) main.getPlayers().get(owner.getUniqueId()).getPlayerClass();
					beastClass.minions.remove(wolf);
				}
			}
		}
	}

	// Player attacking Entity
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player attacker = (Player) event.getDamager();
			Entity damaged = event.getEntity();
			
			if(main.getPlayers().get(attacker.getUniqueId()).getPlayerClass().getName().equals("Assassin")) {
				Double angle = attacker.getLocation().getDirection().angle(damaged.getLocation().getDirection()) / 180 * Math.PI;
				if(angle*100 < 1.5) {
					event.setDamage(event.getFinalDamage() * 2);
				}
				
			}
		} else if (event.getDamager() instanceof Wolf) {
			Wolf wolf = (Wolf) event.getDamager();
			if(wolf.getOwner() != null && wolf.getOwner() instanceof Player) {
				Player owner = (Player) wolf.getOwner();

				if(main.getPlayers().get(owner.getUniqueId()).getPlayerClass().getName().equals("Necromancer")) {
					 main.getPlayers().get(owner.getUniqueId()).healPlayer(event.getFinalDamage());
				}
			}
		}
	}

	// Changing armour for the limited inventory slots
	@EventHandler
	public void onEquipmentChange(ArmourEquipEvent event) {
		if (event.getType() == ArmourType.CHESTPLATE) {
			main.getServer().getScheduler().runTaskLater(main, new Runnable() {
				public void run() {
					main.getPlayers().get(event.getPlayer().getUniqueId()).loadInventory();
				}
			}, 5);
		}
	}

	// Entity damage
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			if(main.getPlayers().get(player.getUniqueId()).getPlayerClass().getName().equals("FireMage")) {
				if(event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
					event.setCancelled(true);
					return;
				}
			}
			
			main.getPlayers().get(player.getUniqueId()).damagePlayer((int) event.getFinalDamage());
			event.setDamage(0.1);
			if (event.getFinalDamage() > player.getHealth()) {
				event.setCancelled(true);
			}
		}
	}

	// Respawn to reset the health
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		main.getPlayers().get(event.getPlayer().getUniqueId()).setPlayerHealth(100);
		main.getPlayers().get(event.getPlayer().getUniqueId()).loadInventory();
	}

	// Player killed by player for pvp stats
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (player.getKiller() != null && player.getKiller() instanceof Player) {
			main.getPlayers().get(player.getUniqueId()).addPvpDeath();
			main.getPlayers().get(player.getKiller().getUniqueId()).addPvpKill();
		}
	}

	// Stop durability of item in first slot
	@EventHandler
	public void onItemDurabilityChange(PlayerItemDamageEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getHeldItemSlot() == 0) {
			event.setCancelled(true);
		}
	}

	// Block break from explosions
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.blockList().clear();
	}
	
	// Events to stop moving of item in first slot
	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		if (event.getSlot() == 0 && event.getClickedInventory() != null
				&& event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
			event.setCancelled(true);
			return;
		} else if (event.getCurrentItem() != null && (event.getCurrentItem().equals(InventoryHandler.getItem("blackGlass"))
				|| event.getCurrentItem().equals(InventoryHandler.getItem("redGlass")))) {
			event.setCancelled(true);
			return;
		}
		
		// Class selector inv
		if(event.getClickedInventory() != null && event.getClickedInventory().equals(InventoryHandler.getClassInv())) {
			event.setCancelled(true);
			Player player = (Player) event.getWhoClicked();
			String className = "";
			if(event.getSlot() == 10) {
				className = "Assassin";
			} else if(event.getSlot() == 11) {
				className = "Berserker";
			} else if(event.getSlot() == 12) {
				className = "FireMage";
			} else if(event.getSlot() == 14) {
				className = "Necromancer";
			} else if(event.getSlot() == 15) {
				className = "Priest";
			} else if(event.getSlot() == 16) {
				className = "Ranger";
			}
			if(!className.equals("")) {
				main.getPlayers().get(player.getUniqueId()).changeClass(className);
				player.sendMessage(ChatColor.GREEN + "Class Selected.");
				player.closeInventory();
			}
		}
	}

	@EventHandler
	public void onSwitchHand(PlayerSwapHandItemsEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getHeldItemSlot() == 0) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getHeldItemSlot() == 0) {
			main.getPlayers().get(event.getPlayer().getUniqueId()).getPlayerClass().switchAbility();
			event.setCancelled(true);
		}
	}
}
