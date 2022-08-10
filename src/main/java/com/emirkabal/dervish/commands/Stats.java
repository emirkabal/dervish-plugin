package com.emirkabal.dervish.commands;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return false;
        }


        Player self = (Player) sender;

        Player p = args.length != 0 ? Bukkit.getPlayer(args[0]) : (Player) sender;
        if (p == null || !p.isOnline()) {
            self.sendMessage("Player is offline.");
            return false;
        }
        self.sendMessage("this command is currently disabled.");

        return false;
    }
}
