package com.emirkabal.dervish.utils;

import com.emirkabal.dervish.runnables.GameCycle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Sidebar {

    public static void setScoreboard(Player p) {
        ScoreboardManager sbm = Bukkit.getScoreboardManager();
        Scoreboard sb = sbm.getNewScoreboard();
        Objective obj = sb.registerNewObjective("Scoreboard", "dummy");
        obj.setDisplayName("§7"+p.getName() + " §r§8- §c"+ GameCycle.timerTitle);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score line1 = obj.getScore("§7§l» §rGame");
        Score line2 = obj.getScore("§r"+PlayerPoints.getFormattedPoints(0));
        Score line3 = obj.getScore("§r"+PlayerPoints.getFormattedPoints(1));
        Score line4 = obj.getScore("§r"+PlayerPoints.getFormattedPoints(2));
        Score line5 = obj.getScore("§r"+PlayerPoints.getFormattedPoints(3));
        Score line6 = obj.getScore("§r"+PlayerPoints.getFormattedPoints(4));
        Score line7 = obj.getScore("§3You§8:§r "+PlayerPoints.getPoints(p.getName()));

        line1.setScore(7);
        line2.setScore(6);
        line3.setScore(5);
        line4.setScore(4);
        line5.setScore(3);
        line6.setScore(2);
        line7.setScore(1);

        p.setScoreboard(sb);
    }
}
