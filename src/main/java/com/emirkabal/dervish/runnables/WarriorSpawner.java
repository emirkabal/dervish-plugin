package com.emirkabal.dervish.runnables;

import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import com.emirkabal.dervish.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;

public class WarriorSpawner extends BukkitRunnable {
    public static HashMap<String, Zombie> zombies = new HashMap<String, Zombie>();

    ArmorStand Block;
    int spawnTime = 120; // 900
    int timerTime = spawnTime;
    String timerTitle = Utils.convertTime(timerTime);
    double i = 0;
    int tick = 0;
    int oneTick = 0;
    Location loc;
    public WarriorSpawner(World world, double x, double y, double z){
        loc = new Location(world,x,y,z);
        ArmorStand main = (ArmorStand) world.spawnEntity(loc.add(0, 1.7, 0), EntityType.ARMOR_STAND);
        main.setGravity(false);
        main.setCustomName("§a"+timerTitle);
        main.setCustomNameVisible(true);
        main.setCanPickupItems(false);
        main.setVisible(false);
        main.setSmall(true);
        main.setHelmet(CustomItem.of(Material.SKULL_ITEM, 1, (short) 2).get());
        Block = main;
        this.runTaskTimer(Main.getInstance(), 0, 1);
    }
    @Override
    public void run(){
        if(Block.isDead() && Block.getLocation().getY() == loc.getY()){
            new WarriorSpawner(Block.getWorld(), Block.getLocation().getX(), Block.getLocation().getY() - 1.7, Block.getLocation().getZ());
            cancel();
        }else if(Block.getLocation().getY() != loc.getY()){
            cancel();
        }
        tick += 1;
        oneTick += 1;
        if(oneTick == 20){ // every 1 second
            timerTime -= 1;
            if(timerTime < 1) {
                timerTime = spawnTime;
            }
            timerTitle = Utils.convertTime(timerTime);
            Block.setCustomName("§a"+timerTitle);
            oneTick = 0;
        }
        if(tick == 20 * spawnTime){
            for (int j = 0; j < 3; j++) {
                Location spawnl = Block.getLocation();
                String locString = String.valueOf(spawnl.getX()+spawnl.getY()+spawnl.getZ());
                if (zombies.get(String.valueOf(j)+locString) != null) continue;

                spawnl = Utils.radiusLoc(j, spawnl);
                Zombie zombie = (Zombie) Block.getWorld().spawnEntity(spawnl, EntityType.ZOMBIE);
                zombie.setBaby(false);
                zombie.setCustomNameVisible(true);
                zombie.setVillager(false);
                zombie.setRemoveWhenFarAway(false);
                zombie.setCanPickupItems(false);
                if (zombie.getPassenger() != null) {
                    zombie.getPassenger().remove();
                }
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999, 2, true, true));
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, 1, true, true));
                EntityEquipment ee = zombie.getEquipment();
                ee.clear();
                ee.setChestplate(CustomItem.of(Material.DIAMOND_CHESTPLATE).get());
                ee.setHelmet(CustomItem.of(Material.RED_ROSE).get());
                ee.setItemInHand(CustomItem.of(Material.IRON_SWORD).withName(String.valueOf(j)+locString).get());
                zombie.setCustomName("§cZombie Warrior");
                zombies.put(String.valueOf(j)+locString, zombie);
            }
            tick = 0;
        }
        if(i < 360){
            i += 0.020;
            for (Location locc : Utils.getCircle(Block.getLocation(), 3, 16)) {
                for(Player all : Bukkit.getOnlinePlayers()){
                    all.spigot().playEffect(locc.add(0, 1, 0), Effect.INSTANT_SPELL, 1, 1, 0, 0,0, 1, 1, 16);
                }
            }
            Block.setHeadPose(new EulerAngle(0, i,0));
            return;
        }
        i = 0;
    }
}
