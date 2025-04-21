package com.emirkabal.battlegrounds.commands;

import com.emirkabal.battlegrounds.Core;
import com.emirkabal.battlegrounds.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ping implements CommandExecutor {
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
        int ping = Core.getPing(p);
        String color = ping > 90 ? "§c" : "§a";
        self.sendMessage(p.getDisplayName()+"§r's connection latency is "+color +ping+"§rms.");



        return false;
    }
}
