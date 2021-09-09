package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.core.CustomItem;
import com.emirkabal.dervish.utils.RandomColor;
import com.emirkabal.dervish.utils.WarriorSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Arrays;


public class EntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(EntityExplodeEvent e){
        e.setCancelled(true);
    }
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onDeath(EntityDeathEvent e){
//        if(!(e.getEntity() instanceof Player) && e.getEntity().getKiller() instanceof Player){
//            Entity entity = e.getEntity();
//            Player defaultPlayer = e.getEntity().getKiller();
//            BloodyPlayer player = new BloodyPlayer(defaultPlayer);
//            player.sendParticle(EnumParticle.NOTE, entity.getLocation().getX(), entity.getLocation().getY() + 2, entity.getLocation().getZ(), 0, 0,0,1, false);
//
//            e.getDrops().removeAll(e.getDrops());
//            e.getDrops().add(getRandomItem());
//        }
//    }
//
//    private static final ItemStack[] RANDOM_ITEM = new ItemStack[]{
//            CustomItem.of(Material.GOLDEN_APPLE).get(),
//
//            CustomItem.of(Material.DIAMOND_BOOTS).get(),
//            CustomItem.of(Material.DIAMOND_SWORD).get(),
//            CustomItem.of(Material.DIAMOND_HELMET).get(),
//            CustomItem.of(Material.IRON_CHESTPLATE).get(),
//            CustomItem.of(Material.CHAINMAIL_HELMET).get()
//    };
//
//    public static ItemStack getRandomItem() {
//        return (ItemStack) RANDOM.nextObject(RANDOM_ITEM);
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTarget(EntityTargetLivingEntityEvent e){
        if(e.getEntity().getType() == EntityType.ZOMBIE && e.getEntity().isInsideVehicle() && e.getEntity().getVehicle().getCustomName() != null && e.getEntity().getVehicle().getCustomName().startsWith("§7Chicken")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onNaturalSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void fishingThrow(ProjectileLaunchEvent e){
        if(e.getEntityType().equals(EntityType.FISHING_HOOK)){
            Projectile hook = e.getEntity();
            hook.setVelocity(hook.getVelocity().multiply(1.4));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeathEntity(EntityDeathEvent e){
        e.setDroppedExp(0);
        switch (e.getEntityType()){
            case CHICKEN:
                if(e.getEntity().getCustomName() != null && e.getEntity().getCustomName().startsWith("§7Chicken")){
                    e.getDrops().removeAll(e.getDrops());
                }
                break;
            case ARMOR_STAND:
                e.getEntity().teleport(e.getEntity().getLocation().add(0, 10, 0));
                break;
            case ZOMBIE:
                if (e.getEntity().isInsideVehicle() && e.getEntity().getVehicle().getCustomName() != null && e.getEntity().getVehicle().getCustomName().startsWith("§7Chicken")){
                    Chicken chicken = (Chicken) e.getEntity().getVehicle();
                    if(chicken.getCustomName().startsWith("§7Chicken")) e.getDrops().removeAll(e.getDrops());
                    Player victim = PlayerListener.chickenPlayers.get(chicken);
//                    e.getDrops().addAll(Arrays.asList(victim.getInventory().getArmorContents()));
//                    e.getDrops().addAll(Arrays.asList(victim.getInventory().getContents()));
                    PlayerListener.inventoryRemoveList.add(victim.getName());
                    if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
                        Player killer = e.getEntity().getKiller();
                        Bukkit.broadcastMessage("§7"+victim.getDisplayName()+"§a killed by §c"+e.getEntity().getKiller().getDisplayName()+"§7 with §c"+String.format("%.1f", e.getEntity().getKiller().getHealth() / 2)+" heart");;
                        killer.getInventory().addItem(CustomItem.of(Material.GOLDEN_APPLE).get());
                    }
                    Firework fw = (Firework) chicken.getWorld().spawnEntity(chicken.getLocation(), EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();
                    fwm.setPower(2);
                    fwm.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.STAR).withColor(Color.LIME).withFade(Color.PURPLE).build());
                    fw.setFireworkMeta(fwm);
                    chicken.remove();

                    PlayerListener.chickens.remove(victim.getName());
                    PlayerListener.chickenPlayers.remove(chicken);
                    return;
                }
                if(e.getEntity().getCustomName() != null && e.getEntity().getCustomName().startsWith("§cZombie Warrior")){
                    WarriorSpawner.zombies.remove(e.getEntity().getEquipment().getItemInHand().getItemMeta().getDisplayName());
                    e.getDrops().removeAll(e.getDrops());
                    Double random = Math.random();
                    if (random <= 0.85) {
                        e.getDrops().add(CustomItem.of(Material.COOKED_BEEF, 2).get());
                    }
                    if (random <= 0.40) {
                        e.getDrops().add(CustomItem.of(Material.GOLDEN_APPLE, 1).get());
                    }
                    if (random <= 0.75) {
                        e.getDrops().add(CustomItem.of(Material.ARROW, 3).get());
                    }
                    if (random <= 0.05) {
                        e.getDrops().add(CustomItem.of(Material.DIAMOND_LEGGINGS, 1).get());
                    }
                }
                break;
        }
    }



}
