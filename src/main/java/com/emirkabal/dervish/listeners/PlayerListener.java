package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import com.emirkabal.dervish.core.CustomPlayer;
import com.emirkabal.dervish.utils.ConfigUtil;
import com.emirkabal.dervish.utils.RandomColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerListener implements Listener {
    public static ArrayList<String> fishingRodCombo = new ArrayList<>();
    public static HashMap<Player, Player> lastdamage = new HashMap();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        e.getDrops().clear();
        CustomPlayer p = new CustomPlayer(e.getEntity());
        e.setDeathMessage(null);
        if (e.getEntity().getKiller() != null) Core.sendMessageToAll("§a"+e.getEntity().getName()+"§7 killed by §c"+e.getEntity().getKiller().getName()+"§7 with §c"+String.format("%.1f", e.getEntity().getKiller().getHealth() / 2)+"❤");
        p.sendFirework(FireworkEffect.Type.CREEPER, RandomColor.dye(), RandomColor.dye(), 2);
        p.deathSpectate();
        p.getSpigotPlayer().setHealth(p.getSpigotPlayer().getMaxHealth());
        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
            // e.getEntity().getKiller().getInventory().addItem(CustomItem.of(Material.GOLDEN_APPLE, 1).get());
            CustomPlayer killer = new CustomPlayer(e.getEntity().getKiller());
            killer.getSpigotPlayer().getInventory().addItem(CustomItem.of(Material.ARROW, 1).get());
            killer.getSpigotPlayer().setHealth(e.getEntity().getMaxHealth());

            p.getSpigotPlayer().teleport(e.getEntity().getKiller(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(),()->{
                p.getSpigotPlayer().setSpectatorTarget(e.getEntity().getKiller());
            },8);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            p.forceRespawn();
        }, 20 * 2);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(Core.getPosition("lobby.spawn"));
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
        }  else if (e.getItem().hasMetadata("no_pickup")) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        e.setFormat((p.isOp() ? "§4" : "§a")+p.getDisplayName()+"§7:§r "+e.getMessage());
    }

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

        if (damager instanceof Projectile && damager.getType() == EntityType.ARROW && ((Projectile) damager).getShooter() instanceof Player && !event.getEntity().isDead()) {
            Arrow arrow = (Arrow) damager;
            Player p = (Player) arrow.getShooter();
            CustomPlayer cp = new CustomPlayer(p);
            LivingEntity entity = (LivingEntity) event.getEntity();
            String message = "§a"+event.getEntity().getName()+"§r health is §c"+String.format("%.1f", entity.getHealth() / 2)+"❤";
            p.sendMessage(Main.PREFIX+ message);
            cp.sendActionBar(message);
        }

        if (damager instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (((Player) damager).getGameMode() == GameMode.ADVENTURE) {
                event.setCancelled(true);
            } else if (lastdamage.get(player) == null) {
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
