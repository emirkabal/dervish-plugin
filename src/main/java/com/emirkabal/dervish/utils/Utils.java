package com.emirkabal.dervish.utils;

import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomPlayer;
import com.emirkabal.dervish.runnables.Sidebar;
import com.emirkabal.dervish.runnables.WarriorSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Utils {
    public static String convertTime(Integer time){
        int sec = time % 60;
        int min = time / 60;
        return (min < 10 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec;
    }
    public static Location radiusLoc(Integer j, Location spawnl){
        double a = Math.round(Math.random() * 2);
        double b = Math.round(Math.random() * 2);
        switch (j){
            case 1:
                spawnl.add(-a, 0, b);
                break;
            case 2:
                spawnl.add(a, 0, -b);
                break;
            default:
                spawnl.add(a, 0, b);
                break;
        }
        return spawnl;
    }
    public static void pluginEnabled(){
        new Sidebar();
        for(World world : Bukkit.getWorlds()){
            for(Entity entity: world.getEntities()){
                if(entity instanceof Player){
                    CustomPlayer cp = new CustomPlayer((Player) entity);
                    cp.getSpigotPlayer().sendMessage(Main.PREFIX+"Plugin reloaded.");
                }
                if(entity instanceof ArmorStand && entity.getCustomName() != null && entity.getCustomName().startsWith("§a")) {
                    Location loc = entity.getLocation();
                    entity.remove();
                    new WarriorSpawner(loc.getWorld(), loc.getX(), loc.getY() - 1.7, loc.getZ());
                }
            }
        }
    }

    public static ArrayList<Location> getCircle(Location center, double radius, int amount)
    {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<Location>();
        for(int i = 0;i < amount; i++)
        {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

}
