package com.emirkabal.dervish.commands;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return false;
        }
        Player p = (Player) sender;
        if(!(p.hasPermission("admin.getworld") || p.hasPermission("admin.*") || p.isOp())) {
            p.sendMessage(Main.NO_PERM);
            return false;
        }

        if (args.length == 0) {
            p.sendMessage("/getworld <[worldname]:list>");
            return false;
        }

        if (args[0].equalsIgnoreCase("list")) {
            for (World world : Bukkit.getWorlds()) {
                p.sendMessage(world.getName());
            }
            return false;
        }

        if (Bukkit.getWorld(args[0]) == null) {
            p.sendMessage(Main.PREFIX+"\""+args[0]+"\" is not recognized.");
            return false;
        }

        p.sendMessage(Main.PREFIX+"You teleported to "+args[0]+" world.");
        p.teleport(Bukkit.getWorld(args[0]).getSpawnLocation());


        return false;
    }
}
