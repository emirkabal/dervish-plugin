package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.utils.PlayerPoints;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;


public class EntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(EntityExplodeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickupProjectileHit(ProjectileHitEvent e) {
        if (e.getEntityType() != EntityType.ARROW) return;
        e.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNaturalSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL || e.getEntityType() == EntityType.SKELETON) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void fishingThrow(ProjectileLaunchEvent e){
        if(e.getEntityType().equals(EntityType.FISHING_HOOK)){
            Projectile hook = e.getEntity();
            hook.setVelocity(hook.getVelocity().multiply(1.38));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
//        Bukkit.broadcastMessage(e.getEntity().getName()+"   -   "+e.getCause().toString());
        if (e.getCause() == EntityDamageEvent.DamageCause.VOID && e.getEntity().isDead() == false) {
            LivingEntity l = (LivingEntity) e.getEntity();
            l.damage(99999999);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeathEntity(EntityDeathEvent e){
        if (e.getEntity().getKiller() instanceof Player) {
            PlayerPoints.addPoints(e.getEntity().getKiller().getName());
        }
        e.setDroppedExp(0);
        e.getDrops().clear();
    }



}
