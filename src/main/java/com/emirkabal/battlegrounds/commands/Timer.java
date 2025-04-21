package com.emirkabal.battlegrounds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.emirkabal.battlegrounds.Main;
import com.emirkabal.battlegrounds.listeners.ConnectionListener;
import com.emirkabal.battlegrounds.runnables.GameCycle;
import com.emirkabal.battlegrounds.utils.Sidebar;

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

        if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("resume")) {
            GameCycle.status = true;
            p.sendMessage("Timer resumed.");
        } else if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("pause")) {
            GameCycle.status = false;
            p.sendMessage("Timer paused.");
            for (Player player : ConnectionListener.readyPlayers) {
                Sidebar.setScoreboard(player);
            }
        } else {
            try {
                int time = Integer.parseInt(args[0]);
                GameCycle.time = time;
                p.sendMessage("Timer time changed to "+time+" second(s).");
            } catch (NumberFormatException e) {
                p.sendMessage("Invalid parameter.");
            }
        }




        return false;
    }
}
