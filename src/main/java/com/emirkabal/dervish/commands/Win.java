package com.emirkabal.dervish.commands;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.runnables.GameCycle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Win implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return false;
        }


        Player p = (Player) sender;

        if(!(p.hasPermission("admin.win") || p.hasPermission("admin.*") || p.isOp())) {
            p.sendMessage(Main.NO_PERM);
            return false;
        }

        Core.applyWinnerEffects(p);
        p.sendMessage("Okay champ");

        return false;
    }
}
