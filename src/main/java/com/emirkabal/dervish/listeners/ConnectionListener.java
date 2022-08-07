package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import com.emirkabal.dervish.core.CustomPlayer;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RegisterEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EntityEquipment;

public class ConnectionListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage(null);
        Player p = e.getPlayer();
        e.setQuitMessage((p.isOp() ? "§4" : "§a")+p.getDisplayName()+" §7left the game.");
        if(PlayerListener.lastdamage.get(p) != null && p.getGameMode() == GameMode.SURVIVAL){
            Chicken chicken = (Chicken) p.getWorld().spawnEntity(p.getLocation(), EntityType.CHICKEN);
            Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
            EntityEquipment ee = zombie.getEquipment();
            ee.setHelmet(CustomItem.getPlayerSkull(p.getName(), 1));
            ee.setChestplate(p.getInventory().getChestplate());
            ee.setLeggings(p.getInventory().getLeggings());
            ee.setBoots(p.getInventory().getBoots());
            ee.setItemInHand(p.getInventory().getItemInHand());
            zombie.setVillager(false);
            zombie.setBaby(true);
            zombie.setCustomName(p.getName());
            zombie.setCustomNameVisible(false);
            chicken.setPassenger(zombie);
            chicken.setMaxHealth(p.getMaxHealth());
            chicken.setHealth(p.getHealth());
            zombie.setMaxHealth(p.getMaxHealth());
            zombie.setHealth(p.getHealth());
            chicken.setCustomName("§7Chicken");
            chicken.setCustomNameVisible(true);
            PlayerListener.chickens.put(p.getName(), chicken);
            PlayerListener.chickenPlayers.put(chicken, p);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                zombie.damage(99999, p);
                PlayerListener.inventoryRemoveList.remove(p.getName());
            }, 20 * 10 * 60 * 3);
        } else {
            PlayerListener.lastdamage.remove(e.getPlayer());
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        e.setJoinMessage((p.isOp() ? "§4" : "§a")+p.getDisplayName()+" §7joined the game.");
        CustomPlayer cp = new CustomPlayer(p);
        for ( Player  players : Bukkit.getOnlinePlayers()) {
            players.playSound(p.getLocation(), Sound.NOTE_PIANO, 0.3F, 0.3F);
        }
        if(PlayerListener.chickens.get(p.getName()) != null) {
            AuthMeApi.getInstance().forceLogin(p);
            Chicken chicken = PlayerListener.chickens.get(p.getName());
            p.teleport(chicken.getLocation());
            chicken.getPassenger().remove();
            chicken.remove();
            PlayerListener.chickenPlayers.remove(chicken);
        }else{
            p.teleport(Core.getSpawn(Bukkit.getWorld("lobby"), "lobby"));
            cp.clearInventory();
            cp.removePotionEffects();
            p.setGameMode(GameMode.ADVENTURE);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(1000);
        }
    }

    @EventHandler
    public void onLogin(LoginEvent e) {
        if(PlayerListener.chickens.get(e.getPlayer().getName()) == null) {
            e.getPlayer().teleport(Core.getSpawn(Bukkit.getWorld(Core.currentWorld), ""));
        } else {
            PlayerListener.chickens.remove(e.getPlayer().getName());
        }
    }

    @EventHandler
    public void onRegister(RegisterEvent e) {
        AuthMeApi.getInstance().forceLogin(e.getPlayer());
    }


    @EventHandler
    public void onPing(ServerListPingEvent e) {
        e.setMaxPlayers(24);
        e.setMotd("§e§lThe Battlegrounds Server §6[Kit PvP]\n§r§bCustom PvP Hits - Improved Fishing Rod ");
    }


}
