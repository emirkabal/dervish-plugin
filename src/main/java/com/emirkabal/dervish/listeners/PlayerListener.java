package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import com.emirkabal.dervish.core.CustomPlayer;
import com.emirkabal.dervish.utils.ConfigUtil;
import com.emirkabal.dervish.utils.PlayerPoints;
import com.emirkabal.dervish.utils.RandomColor;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerListener implements Listener {
    public static ArrayList<String> inventoryRemoveList = new ArrayList<>();
    public static ArrayList<String> fishingRodCombo = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        e.getDrops().clear();
        CustomPlayer p = new CustomPlayer(e.getEntity());
        e.setDeathMessage(null);
        if (e.getEntity().getKiller() != null) e.setDeathMessage("§a"+e.getEntity().getName()+"§7 killed by §c"+e.getEntity().getKiller().getName()+"§7 with §c"+String.format("%.1f", e.getEntity().getKiller().getHealth() / 2)+" heart");
        p.sendFirework(FireworkEffect.Type.CREEPER, RandomColor.dye(), RandomColor.dye(), 2);
        p.deathSpectate();
        p.getSpigotPlayer().setHealth(p.getSpigotPlayer().getMaxHealth());
        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
            // e.getEntity().getKiller().getInventory().addItem(CustomItem.of(Material.GOLDEN_APPLE, 1).get());
            CustomPlayer killer = new CustomPlayer(e.getEntity().getKiller());
            killer.getSpigotPlayer().getInventory().addItem(CustomItem.of(Material.ARROW, 1).get());
            killer.getSpigotPlayer().setHealth(e.getEntity().getMaxHealth());
            killer.getSpigotPlayer().playSound(e.getEntity().getKiller().getLocation(), Sound.NOTE_PIANO, 0.6F, 0.6F);
            killer.repairAllItems();
            PlayerPoints.addPoints(killer.getSpigotPlayer().getName());

            p.getSpigotPlayer().teleport(e.getEntity().getKiller(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(),()->{
                p.getSpigotPlayer().setSpectatorTarget(e.getEntity().getKiller());
            },8);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            p.forceRespawn();
        }, 20 * 4);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        double yCord = ConfigUtil.conf().getDouble("maxSafeLocation."+e.getPlayer().getWorld().getName());
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
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        e.setFormat((p.isOp() ? "§4" : "§a")+p.getDisplayName()+"§7:§r "+e.getMessage());
    }
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onCommand(PlayerCommandPreprocessEvent e){
//        if(lastdamage.get(e.getPlayer()) != null){
//            e.setCancelled(true);
//        }
//    }

    @EventHandler
    public void onDamage2(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
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

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Projectile && damager.getCustomName() != null && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getName() == damager.getCustomName()) {
                event.setCancelled(true);
            }
        }

        if (damager instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (((Player) damager).getGameMode() == GameMode.ADVENTURE) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
