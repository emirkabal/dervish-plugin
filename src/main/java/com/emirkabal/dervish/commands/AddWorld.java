package com.emirkabal.dervish.commands;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return false;
        }
        Player p = (Player) sender;
        if(!(p.hasPermission("admin.addworld") || p.hasPermission("admin.*") || p.isOp())) {
            p.sendMessage(Main.NO_PERM);
            return false;
        }

        if (args.length == 0) {
            p.sendMessage("/getworld <[worldname]>");
            return false;
        }


        Core.addWorld(args[0]);
        new WorldCreator(args[0]).createWorld();
        p.sendMessage(Main.PREFIX+"Added world: "+args[0]);

        return false;
    }
}
