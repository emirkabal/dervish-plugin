package com.emirkabal.dervish.utils;

import com.emirkabal.dervish.Main;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

public class Runnables {
    public static void startAll(){
//        everyTick();
    }

    public static void everyTick(){
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                for(World world : Bukkit.getWorlds()){
                    for(Entity entity : world.getEntities()){
                        if(entity instanceof Player){
                            Player p = (Player) entity;
                            if(p.isFlying()) p.getWorld().playEffect(p.getLocation(), Effect.NOTE, 10, 10);
                        }
                    }
                }
            }
            }, 0, 1);
    }
}
