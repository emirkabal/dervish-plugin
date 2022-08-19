package com.emirkabal.dervish.runnables;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomPlayer;
import com.emirkabal.dervish.utils.PlayerPoints;
import com.emirkabal.dervish.utils.Sidebar;
import com.emirkabal.dervish.utils.Utils;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCycle extends BukkitRunnable {
    public static int startTime = 300;
    public static int time = Integer.valueOf(startTime);
    public static boolean status = true;
    public static String timerTitle = Utils.convertTime(time);

    public GameCycle(){
        this.runTaskTimer(Main.getInstance(), 0, 20);
    }

    @Override
    public void run(){
        if (status == false) return;



//        int min = time / 60;
//
//        if (time == startTime || min == 5 || min == 4 || min == 3 || min == 2 || min == 1) {
//            Core.sendMessageToAll("§8[§6"+min+"§8]§e minutes until the next game§8!");
//        }

        time -= 1;
        timerTitle = Utils.convertTime(time);
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (AuthMeApi.getInstance().isAuthenticated(players)) {
                Sidebar.setScoreboard(players);
            }
        }

        if (time == 10) {
            Core.sendMessageToAll("§8[§610§8]§e seconds until the next game§8!");
        }
        else if (time == 5) {
            Core.sendMessageToAll("§6§lThe winner this round was...");
            if (PlayerPoints.hasWinner()) {
                Core.sendMessageToAll("§e"+PlayerPoints.getWinner()+"§6§l!");
                Player winner = Bukkit.getPlayer(PlayerPoints.getWinner());
                if (winner != null) {
                    Core.applyWinnerEffects(winner);
                }
            } else {
                Core.sendMessageToAll("No winner for this round.");
            }
            Core.sendMessageToAll("§6The map will rotate in 5 seconds.");
        }
        else if(time < 1) {
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
