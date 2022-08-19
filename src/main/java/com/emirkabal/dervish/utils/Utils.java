package com.emirkabal.dervish.utils;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomPlayer;
import com.emirkabal.dervish.runnables.GameCycle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
        new GameCycle();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            CustomPlayer cp = new CustomPlayer(player);
            String msg = "Battlegrounds reloaded.";
            cp.getSpigotPlayer().sendMessage(msg);
            cp.sendActionBar(msg);
            cp.getSpigotPlayer().teleport(Core.getPosition("lobby.spawn"));
            cp.getSpigotPlayer().sendMessage("§6§lWarning:§r §eRelogin required! /login <password>");
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
