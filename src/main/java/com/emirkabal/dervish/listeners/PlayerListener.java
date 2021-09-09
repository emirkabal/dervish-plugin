package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import com.emirkabal.dervish.core.CustomPlayer;
import com.emirkabal.dervish.utils.RandomColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerListener implements Listener {

    public static HashMap<Player, Player> lastdamage = new HashMap();
    public static HashMap<String, Chicken> chickens = new HashMap();
    public static HashMap<Chicken, Player> chickenPlayers = new HashMap();
    public static ArrayList<String> inventoryRemoveList = new ArrayList<>();


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        e.getDrops().clear();
        CustomPlayer p = new CustomPlayer(e.getEntity());
        lastdamage.remove(p.getSpigotPlayer());
        e.setDeathMessage(null);
        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) e.setDeathMessage("§a"+e.getEntity().getDisplayName()+"§7 killed by §c"+e.getEntity().getKiller().getDisplayName()+"§7 with §c"+String.format("%.1f", e.getEntity().getKiller().getHealth() / 2)+" heart");
        p.sendFirework(FireworkEffect.Type.CREEPER, RandomColor.dye(), RandomColor.dye(), 2);
        p.deathSpectate();
        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
            e.getEntity().getKiller().getInventory().addItem(CustomItem.of(Material.GOLDEN_APPLE, 1).get());
            p.getSpigotPlayer().teleport(e.getEntity().getKiller(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(),()->{
                p.getSpigotPlayer().setSpectatorTarget(e.getEntity().getKiller());
            },8);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            p.forceRespawn();
        }, 20 * 6);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getTo().getY() <= 115) {
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
            if (lastdamage.get(player) == null) {
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
