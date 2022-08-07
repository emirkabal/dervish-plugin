package com.emirkabal.dervish.runnables;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class Sidebar extends BukkitRunnable {
    int time = 600;
    int timerTime = Integer.valueOf(time);
    String timerTitle = Utils.convertTime(timerTime);
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();
    Objective objective = board.registerNewObjective("Sidebar", "dummy");

    public Sidebar(){
        this.runTaskTimer(Main.getInstance(), 0, 20);
    }

    @Override
    public void run(){
        timerTime -= 1;
        if(timerTime < 1) {
            Core.currentWorldNum++;
            if (Core.currentWorldNum > (Core.worldNames.size() - 1)) {
                Core.currentWorldNum = 0;
            }

            Core.changeWorld(Core.worldNames.get(Core.currentWorldNum), Bukkit.getWorld(Core.currentWorld));

            timerTime = time;
        }
        timerTitle = Utils.convertTime(timerTime);

        objective.setDisplayName("§7BG §8- §c"+timerTitle);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore("§7Online: ");
        score.setScore(Bukkit.getOnlinePlayers().size());

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(board);
        }


    }
}
