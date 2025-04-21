package com.emirkabal.battlegrounds.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.emirkabal.battlegrounds.Core;
import com.emirkabal.battlegrounds.Main;
import com.emirkabal.battlegrounds.listeners.ConnectionListener;
import com.emirkabal.battlegrounds.utils.PlayerPoints;
import com.emirkabal.battlegrounds.utils.Sidebar;
import com.emirkabal.battlegrounds.utils.StatsManager;
import com.emirkabal.battlegrounds.utils.Utils;

public class GameCycle extends BukkitRunnable {

    public static int startTime = 300;
    public static int time = Integer.valueOf(startTime);
    public static boolean status = true;
    public static String timerTitle = Utils.convertTime(time);

    public GameCycle() {
        this.runTaskTimer(Main.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        if (status == false) {
            return;
        }

        int min = time / 60;

        if (time % 60 == 0) {
            Core.sendMessageToAll("§8[§6" + min + "§8]§e minutes until the next game§8!");
        }

        time -= 1;
        timerTitle = Utils.convertTime(time);
        for (Player player : ConnectionListener.readyPlayers) {
            Sidebar.setScoreboard(player);
        }

        if (time == 10) {
            Core.sendMessageToAll("§8[§610§8]§e seconds until the next game§8!");
        } else if (time == 5) {
            Core.sendMessageToAll("§6§lThe winner this round was...");
            for (Player player : ConnectionListener.readyPlayers) {
                StatsManager playerStats = new StatsManager(player);
                playerStats.addStat("games_played", 1);
            }
            if (PlayerPoints.hasWinner()) {
                Core.sendMessageToAll("§e" + PlayerPoints.getWinner() + "§6§l!");
                Player winner = Bukkit.getPlayer(PlayerPoints.getWinner());

                StatsManager winnerStats = new StatsManager(winner);
                winnerStats.addStat("games_won", 1);

                if (winner != null) {
                    Core.applyWinnerEffects(winner);
                }
            } else {
                Core.sendMessageToAll("No winner for this round.");
            }
            Core.sendMessageToAll("§6The map will rotate in 5 seconds.");
        } else if (time < 1) {
            Core.currentWorldNum++;
            if (Core.currentWorldNum > (Core.worldNames.size() - 1)) {
                Core.currentWorldNum = 0;
            }

            PlayerPoints.removeAllPoints();
            Core.changeWorld(Core.worldNames.get(Core.currentWorldNum), Bukkit.getWorld(Core.currentWorld));

            time = startTime;
        }

    }
}
