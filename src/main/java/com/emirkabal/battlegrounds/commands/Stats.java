package com.emirkabal.battlegrounds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.emirkabal.battlegrounds.Main;
import com.emirkabal.battlegrounds.utils.StatsManager;

public class Stats implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.NO_CONSOLE);
            return false;
        }

        Player self = (Player) sender;

        Player p = args.length != 0 ? Bukkit.getPlayer(args[0]) : (Player) sender;
        if (p == null || !p.isOnline()) {
            self.sendMessage("Player is offline.");
            return false;
        }

        StatsManager stats = new StatsManager(p);
        String color = p.isOp() ? "§4" : "§a";
        self.sendMessage(color + p.getDisplayName() + "§r's stats: ");
        self.sendMessage("§6Games Played: §r" + stats.gamesPlayed());
        self.sendMessage("§6Games Won: §r" + stats.gamesWon());
        self.sendMessage("§6Kills: §r" + stats.kills());
        self.sendMessage("§6Deaths: §r" + stats.deaths());
        self.sendMessage("§6K/D Ratio: §r" + String.format("%.2f", stats.kdr()));
        self.sendMessage("§6W/L Ratio: §r" + String.format("%.2f", stats.wlr()));

        return true;
    }
}
