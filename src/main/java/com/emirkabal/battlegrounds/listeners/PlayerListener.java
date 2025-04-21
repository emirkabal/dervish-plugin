package com.emirkabal.battlegrounds.listeners;

import com.emirkabal.battlegrounds.Core;
import com.emirkabal.battlegrounds.Main;
import com.emirkabal.battlegrounds.core.CustomItem;
import com.emirkabal.battlegrounds.core.CustomPlayer;
import com.emirkabal.battlegrounds.utils.ConfigUtil;
import com.emirkabal.battlegrounds.utils.RandomColor;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    public static ArrayList<String> fishingRodCombo = new ArrayList<>();
    public static HashMap<Player, Player> lastdamage = new HashMap();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        e.getDrops().clear();
        CustomPlayer p = new CustomPlayer(e.getEntity());
        e.setDeathMessage(null);
        if (e.getEntity().getKiller() != null && e.getEntity().getUniqueId() != e.getEntity().getKiller().getUniqueId()) {
            Core.sendMessageToAll("§a" + e.getEntity().getName() + "§7 killed by §c" + e.getEntity().getKiller().getName() + "§7 with §c" + String.format("%.1f", e.getEntity().getKiller().getHealth() / 2) + "❤");
        }
        p.sendFirework(FireworkEffect.Type.CREEPER, RandomColor.dye(), RandomColor.dye(), 2);
        p.deathSpectate();
        p.getSpigotPlayer().setHealth(p.getSpigotPlayer().getMaxHealth());
        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player && e.getEntity().getUniqueId() != e.getEntity().getKiller().getUniqueId()) {
            // e.getEntity().getKiller().getInventory().addItem(CustomItem.of(Material.GOLDEN_APPLE, 1).get());
            p.sendActionBar("§c§lYou were slain by §e" + e.getEntity().getKiller().getName());
            CustomPlayer killer = new CustomPlayer(e.getEntity().getKiller());
            killer.getSpigotPlayer().getInventory().addItem(CustomItem.getArrow());
            killer.getSpigotPlayer().setHealth(e.getEntity().getMaxHealth());
            killer.sendActionBar("§a§lYou killed §e" + e.getEntity().getName());
            killer.getSpigotPlayer().getInventory().addItem(CustomItem.getFood(2));

            p.getSpigotPlayer().teleport(e.getEntity().getKiller(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                p.getSpigotPlayer().setSpectatorTarget(e.getEntity().getKiller());
            }, 8);
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            p.forceRespawn();
        }, 20 * 2);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getType() != InventoryType.CRAFTING || e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            Player p = (Player) e.getPlayer();
            String key = "slots." + p.getUniqueId().toString();
            HashMap<String, Integer> slots = new HashMap<>();

            for (int i = 0; i < p.getInventory().getContents().length; i++) {
                ItemStack invItem = p.getInventory().getItem(i);
                int slot = i;
                if (invItem != null && invItem.getType() != Material.AIR) {
                    switch (invItem.getType()) {
                        case IRON_SWORD:
                            slots.put("sword", slot);
                            break;
                        case GRILLED_PORK:
                            slots.put("food", slot);
                            break;
                        case FISHING_ROD:
                            slots.put("rod", slot);
                            break;
                        case BOW:
                            slots.put("bow", slot);
                            break;
                        case ARROW:
                            slots.put("arrow", slot);
                            break;
                        default:
                            break;
                    }
                }
            }

            Core.dataManager.setData(key, slots);
        }, 1);

    }

    @EventHandler
    public void onJoin(PlayerSpawnLocationEvent e) {
        e.setSpawnLocation(Core.getPosition("lobby.spawn"));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(Core.getSpawn(Bukkit.getWorld(Core.currentWorld), ""));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        double yCord = ConfigUtil.conf().getDouble("maxSafeLocation." + e.getPlayer().getWorld().getName());
        if (e.getTo().getY() <= yCord) {
            e.getPlayer().damage(99999);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (e.getItem().getType() == EntityType.DROPPED_ITEM && e.getItem().getItemStack().getType() == Material.DIAMOND_LEGGINGS) {
            e.setCancelled(true);
            e.getPlayer().getInventory().setLeggings(e.getItem().getItemStack());
            e.getItem().remove();
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.HORSE_ARMOR, 1, 1);
        } else if (e.getItem().hasMetadata("no_pickup")) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        e.setFormat((p.isOp() ? "§4" : "§a") + p.getDisplayName() + "§7:§r " + e.getMessage());
    }

    @EventHandler
    public void onDamage2(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getGameMode() == GameMode.ADVENTURE) {
                e.setCancelled(true);
            }
            if (fishingRodCombo.contains(p.getName())) {
                fishingRodCombo.remove(p.getName());
                p.setMaximumNoDamageTicks(20);
                p.setNoDamageTicks(20);
            }
            if (fishingRodCombo.contains(p.getName()) == false && e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                fishingRodCombo.add(p.getName());
                p.setMaximumNoDamageTicks(1);
                p.setNoDamageTicks(1);
            }

        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (e.getEntity().getGameMode() == GameMode.ADVENTURE) {
            e.setFoodLevel(20);
        }
    }

    //fishing
    @EventHandler
    public void onFishing(PlayerFishEvent e) {
        if (e.getCaught() instanceof Item) {
            Item stack = (Item) e.getCaught();
            double random = Math.random();
            if (random <= 0.001) {
                stack.setItemStack(CustomItem.of(Material.GOLDEN_APPLE).setDurability((short) 1).withName("0.001%").get());
                e.getPlayer().sendMessage("YOU ARE SO LUCKY!");
            } else if (random <= 0.05) {
                stack.setItemStack(CustomItem.of(Material.DIAMOND_SWORD).withName("0.05%").get());
                e.getPlayer().sendMessage("YOU ARE SO LUCKY!");
            } else if (random <= 0.4) {
                stack.setItemStack(CustomItem.of(Material.GOLDEN_APPLE).get());
            } else if (random <= 0.6) {
                stack.setItemStack(CustomItem.getArrow(2));
            } else if (random <= 1) {
                stack.setItemStack(CustomItem.getFood(2));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (event.getEntity() instanceof ItemFrame) {
            if (event.getDamager() instanceof Player) {
                if (((Player) event.getDamager()).getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            }
            if (damager instanceof Projectile) {
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    if (((Player) ((Projectile) event.getDamager()).getShooter()).getGameMode() != GameMode.CREATIVE) {
                        event.getDamager().remove();
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (damager instanceof Projectile && damager.getType() == EntityType.ARROW && ((Projectile) damager).getShooter() instanceof Player && !event.getEntity().isDead() && event.getEntity().getUniqueId() != ((Player) ((Projectile) damager).getShooter()).getUniqueId()) {
            Arrow arrow = (Arrow) damager;
            Player p = (Player) arrow.getShooter();
            CustomPlayer cp = new CustomPlayer(p);
            LivingEntity entity = (LivingEntity) event.getEntity();
            double entityHealth = entity.getHealth() - event.getFinalDamage();
            if (entityHealth > 0) {
                String message = "§e" + event.getEntity().getName() + "§r's health is §c" + String.format("%.1f", entityHealth / 2) + "❤";
                p.sendMessage(message);
                cp.sendActionBar(message);
            }
        }

        if (damager instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (((Player) damager).getGameMode() == GameMode.ADVENTURE) {
                event.setCancelled(true);
            } else if (lastdamage.get(player) == null && event.getDamager().getUniqueId() != player.getUniqueId()) {
                Player lastdamager = (Player) event.getDamager();
                lastdamage.put(player, lastdamager);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                    if (lastdamage.get(player) != null) {
                        lastdamage.remove(player);
                    }
                }, 20 * 20);
            }
        }
    }
}
