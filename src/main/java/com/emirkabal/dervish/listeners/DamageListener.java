package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import com.emirkabal.dervish.core.CustomPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntityType() == EntityType.ARMOR_STAND && e.getEntity().getCustomName() != null && e.getEntity().getCustomName().startsWith("§a") && ((Player) e.getDamager()).getItemInHand() != null && ((Player) e.getDamager()).getItemInHand().getItemMeta() != null && CustomItem.isEventItem(((Player) e.getDamager()).getItemInHand()) == true){
            e.getEntity().teleport(e.getEntity().getLocation().add(0, 10, 0));
            e.getEntity().remove();
            ((Player) e.getDamager()).sendMessage(Main.PREFIX + "Event deleted: "+e.getEntity().getLocation().getX()+", "+e.getEntity().getLocation().getY()+", "+e.getEntity().getLocation().getZ());
        }
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            Entity entity = e.getEntity();
            Player defaultPlayer = (Player) e.getDamager();
            CustomPlayer player = CustomPlayer.customplayer.get(defaultPlayer);
//            player.sendParticle(EnumParticle.HEART, entity.getLocation().getX(), entity.getLocation().getY() + 1.7, entity.getLocation().getZ(), 0, 0,0,1, 1, false);
        }
    }

}
