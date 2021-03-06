package com.emirkabal.dervish;

import com.emirkabal.dervish.commands.*;
import com.emirkabal.dervish.listeners.*;
import com.emirkabal.dervish.utils.Runnables;
import com.emirkabal.dervish.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;
    public static final String PREFIX = "§8[§7Dervish§8]§r ";
    public static final String NO_PERM = PREFIX + "§cYou don't have permission.";
    public static final String NO_CONSOLE = PREFIX + "You don't run this command in console.";
    private static final ConsoleCommandSender cs = Bukkit.getConsoleSender();
    private static final PluginManager pm = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
        instance = this;
        cs.sendMessage(PREFIX+"§aEnabled");
        saveDefaultConfig();
        loadCmds();
        loadListeners();
        Runnables.startAll();
        Utils.pluginEnabled();
    }


    @Override
    public void onDisable() {
        cs.sendMessage(PREFIX+"§cDisabled");
    }



    void loadCmds(){
        getCommand("setposition").setExecutor(new SetPosition());
        getCommand("getposition").setExecutor(new GetPosition());
        getCommand("eventitem").setExecutor(new EventItem());
    }

    void loadListeners(){
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new EntityListener(), this);
        pm.registerEvents(new BlockListener(), this);
        pm.registerEvents(new ConnectionListener(), this);
        pm.registerEvents(new ActionListener(), this);
        pm.registerEvents(new DamageListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }

}
