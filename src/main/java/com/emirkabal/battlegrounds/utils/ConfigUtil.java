package com.emirkabal.battlegrounds.utils;

import com.emirkabal.battlegrounds.Main;
import org.bukkit.configuration.file.FileConfiguration;


public class ConfigUtil {

    public static void saveAll(){
        Main.getInstance().getConfig().options().copyDefaults(true);
        Main.getInstance().saveConfig();
    }

    public static FileConfiguration conf(){
        return Main.getInstance().getConfig();
    }


    public static void reloadAll(){
        Main.getInstance().reloadConfig();
        Main.getInstance().getConfig().options().copyDefaults(true);
        Main.getInstance().saveConfig();
    }

}
