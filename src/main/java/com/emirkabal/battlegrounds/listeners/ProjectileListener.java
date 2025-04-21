package com.emirkabal.battlegrounds.listeners;

import com.emirkabal.battlegrounds.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class ProjectileListener implements Listener {

    @EventHandler
    public void projectileHit(ProjectileHitEvent e) {
        if (e.getEntityType() != EntityType.ARROW) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getEntity().isOnGround() && !e.getEntity().isDead()) {
                    e.getEntity().remove();
                }
            }
        }.runTaskLater(Main.getInstance(), 20 * 3);
    }

    @EventHandler
    public void projectileLaunch(ProjectileLaunchEvent e){
        if(e.getEntityType().equals(EntityType.FISHING_HOOK)){
            Projectile hook = e.getEntity();
            hook.setVelocity(hook.getVelocity().multiply(1.38));
        }
        if (e.getEntityType().equals(EntityType.ARROW)) {
            Arrow arrow = (Arrow) e.getEntity();
            arrow.setMetadata("no_pickup", new FixedMetadataValue(Main.getInstance(), true));
        }
    }

}
