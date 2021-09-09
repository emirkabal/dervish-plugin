package com.emirkabal.dervish.commands;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetPosition implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return true;
        }
        Player p = (Player) sender;
        if(!(p.hasPermission("admin.getpos") || p.hasPermission("admin.*") || p.isOp())) {
            p.sendMessage(Main.NO_PERM);
            return true;
        }
        Location loc = p.getLocation();
        if (Core.getPosition(args[0] != null ? args[0] : "spawn") == null) {
            p.sendMessage(Main.PREFIX+"Position not found.");
        }
        p.teleport(Core.getPosition(args[0] != null ? args[0] : "spawn"));
        p.sendMessage(Main.PREFIX+"You teleported to "+(args[0] != null ? args[0] : "spawn"));
        return false;
    }
}
