package com.emirkabal.dervish.utils;

import com.emirkabal.dervish.Main;
import org.bukkit.configuration.file.FileConfiguration;


public class ConfigUtil {

    public static void saveAll(){
        Main.getInstance().getConfig().options().copyDefaults(true);
        Main.getInstance().saveConfig();
    }

    public static FileConfiguration conf(){
        return Main.getInstance().getConfig();
    }




}
