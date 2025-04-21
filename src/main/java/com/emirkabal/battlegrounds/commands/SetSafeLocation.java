package com.emirkabal.battlegrounds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.emirkabal.battlegrounds.Main;
import com.emirkabal.battlegrounds.utils.ConfigUtil;

public class SetSafeLocation implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return true;
        }
        Player p = (Player) sender;
        if(!(p.hasPermission("admin.setsafelocation") || p.hasPermission("admin.*") || p.isOp())) {
            p.sendMessage(Main.NO_PERM);
            return true;
        }


        double safeCord = args.length == 0 ? p.getLocation().getY() : Double.parseDouble(args[0]);

        ConfigUtil.conf().set("maxSafeLocation."+p.getWorld().getName(), safeCord);
        ConfigUtil.saveAll();

        p.sendMessage(Main.PREFIX+"Max safe location changed to "+safeCord+"y on "+p.getWorld().getName());

        return false;
    }
}
