package com.emirkabal.battlegrounds.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.emirkabal.battlegrounds.Core;
import com.emirkabal.battlegrounds.Main;

public class ChangeWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return true;
        }
        Player p = (Player) sender;
        if(!(p.hasPermission("admin.changeworld") || p.hasPermission("admin.*") || p.isOp())) {
            p.sendMessage(Main.NO_PERM);
            return true;
        }


        if (args.length == 0 || Bukkit.getWorld(args[0]) == null) {
            for (World world : Bukkit.getWorlds()) {
                p.sendMessage(world.getName());
            }
            return false;
        }

        Core.changeWorld(args[0], p.getWorld());
        return false;
    }
}
