package com.emirkabal.dervish;

import com.emirkabal.dervish.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Core {
    static FileConfiguration cfg = ConfigUtil.conf();

    public static void setPosition(String positionName, Double x, Double y, Double z, Float yaw, Float pitch, String world){
        cfg.set(positionName+".x", x);
        cfg.set(positionName+".y", y);
        cfg.set(positionName+".z", z);
        cfg.set(positionName+".yaw", yaw);
        cfg.set(positionName+".pitch", pitch);
        cfg.set(positionName+".world", world);
        ConfigUtil.saveAll();
    }
    public static Location getPosition(String positionName){
        if(cfg.getString(positionName+".world") == null || cfg.getString(positionName+".x") == null || cfg.getString(positionName+".y") == null || cfg.getString(positionName+".z") == null || cfg.getString(positionName+".yaw") == null || cfg.getString(positionName+".pitch") == null) return null;
        Location loc = new Location(Bukkit.getWorld(cfg.getString(positionName+".world")), cfg.getDouble(positionName+".x"), cfg.getDouble(positionName+".y"), cfg.getDouble(positionName+".z"), cfg.getLong(positionName+".yaw"), cfg.getLong(positionName+".pitch"));
        return loc;
    }

}
