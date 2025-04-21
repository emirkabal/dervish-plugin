package com.emirkabal.battlegrounds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.emirkabal.battlegrounds.Core;
import com.emirkabal.battlegrounds.Main;

public class RemoveWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return false;
        }
        Player p = (Player) sender;
        if(!(p.hasPermission("admin.removeworld") || p.hasPermission("admin.*") || p.isOp())) {
            p.sendMessage(Main.NO_PERM);
            return false;
        }


        if (args.length == 0) {
            p.sendMessage("/getworld <[worldname]>");
            return false;
        }

        Core.removeWorld(args[0]);
        Bukkit.unloadWorld(args[0], false);

        p.sendMessage(Main.PREFIX+"Removed world: "+args[0]);


        return false;
    }
}
