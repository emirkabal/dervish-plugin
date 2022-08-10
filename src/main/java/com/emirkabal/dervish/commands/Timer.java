package com.emirkabal.dervish.commands;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.runnables.GameCycle;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Timer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return false;
        }


        Player p = (Player) sender;

        if(!(p.hasPermission("admin.timer") || p.hasPermission("admin.*") || p.isOp())) {
            p.sendMessage(Main.NO_PERM);
            return false;
        }

        if (args.length == 0) {
            p.sendMessage("/timer <start:stop:[seconds]>");
            return false;
        }

        if (args[0].equalsIgnoreCase("start")) {
            GameCycle.status = true;
            p.sendMessage("Timer started.");
        } else if (args[0].equalsIgnoreCase("stop")) {
            GameCycle.status = false;
            p.sendMessage("Timer stopped.");
        } else {
            try {
                int time = (int) Integer.parseInt(args[0]);
                GameCycle.time = time;
                p.sendMessage("Timer time changed to "+time+" second(s).");
            } catch (Exception e) {
                p.sendMessage("Invalid paramater.");
            }
        }




        return false;
    }
}
