package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player && e.getDamager().getType() == EntityType.ARROW) {
            ((CraftPlayer) e.getEntity()).getHandle().getDataWatcher().watch(9, (byte) -1);
        }
        if(e.getDamager() instanceof Player && e.getEntityType() == EntityType.ARMOR_STAND && e.getEntity().getCustomName() != null && e.getEntity().getCustomName().startsWith("§a") && ((Player) e.getDamager()).getItemInHand() != null && ((Player) e.getDamager()).getItemInHand().getItemMeta() != null && CustomItem.isEventItem(((Player) e.getDamager()).getItemInHand()) == true){
            e.getEntity().teleport(e.getEntity().getLocation().add(0, 10, 0));
            e.getEntity().remove();
            e.getDamager().sendMessage(Main.PREFIX + "Event deleted: "+e.getEntity().getLocation().getX()+", "+e.getEntity().getLocation().getY()+", "+e.getEntity().getLocation().getZ());
        }
    }

}
