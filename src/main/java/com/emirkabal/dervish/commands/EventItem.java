package com.emirkabal.dervish.commands;

import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class EventItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return true;
        }
        Player p = (Player) sender;
        if(!(p.hasPermission("eventitem.setter") || p.hasPermission("eventitem.*") || p.isOp())){
            p.sendMessage(Main.NO_PERM);
            return true;
        }
        p.getInventory().addItem(CustomItem.eventItem);
        return false;
    }
}
