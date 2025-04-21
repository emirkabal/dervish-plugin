package com.emirkabal.battlegrounds.commands;

import com.emirkabal.battlegrounds.Core;
import com.emirkabal.battlegrounds.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPosition implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return true;
        }
        Player p = (Player) sender;
        if(!(p.hasPermission("admin.setpos") || p.hasPermission("admin.*") || p.isOp())) {
            p.sendMessage(Main.NO_PERM);
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Main.PREFIX+"Where?");
            return false;
        }
        Location loc = p.getLocation();
        Core.setPosition(p.getWorld().getName()+"."+args[0], loc.getX(), loc.getY() + 1.5, loc.getZ(), loc.getYaw(), loc.getPitch(), loc.getWorld().getName());
        p.sendMessage(Main.PREFIX+"Position set: "+args[0]);
        return false;
    }
}
