/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emirkabal.battlegrounds.utils;

import java.util.HashMap;
import java.util.Objects;

import org.bukkit.entity.Player;

import com.emirkabal.battlegrounds.Core;

public class StatsManager {

    private HashMap<String, Integer> stats;
    private String key = "stats.";

    public StatsManager(Player player) {
        this.key = key + player.getUniqueId().toString();
        this.stats = Core.dataManager.getData(this.key, HashMap.class);
        if (this.stats == null) {
            this.stats = new HashMap<>();
            this.stats.put("kills", 0);
            this.stats.put("deaths", 0);
            this.stats.put("games_played", 0);
            this.stats.put("games_won", 0);
        }
    }

    public void setStat(String stat, int value) {
        this.stats.put(stat, value);
        Core.dataManager.setData(this.key, this.stats);
    }

    public int getStat(String stat) {
        return Objects.requireNonNullElse(this.stats.get(stat), 0);
    }

    public void addStat(String stat, int value) {
        this.stats.put(stat, this.stats.get(stat) + value);
        Core.dataManager.setData(this.key, this.stats);
    }

    public void removeStat(String stat, int value) {
        this.stats.put(stat, this.stats.get(stat) - value);
        Core.dataManager.setData(this.key, this.stats);
    }

    public HashMap<String, Integer> getStats() {
        return this.stats;
    }

    public int kills() {
        return this.stats.get("kills");
    }

    public int deaths() {
        return this.stats.get("deaths");
    }

    public int gamesPlayed() {
        return this.stats.get("games_played");
    }

    public int gamesWon() {
        return this.stats.get("games_won");
    }

    public int gamesLost() {
        return this.stats.get("games_played") - this.stats.get("games_won");
    }

    public float kdr() {
        if (this.deaths() == 0) {
            return this.kills();
        }
        return (float) Math.round(this.kills() / (float) this.deaths() * 100) / 100;
    }

    public float wlr() {
        if (this.gamesLost() == 0) {
            return this.gamesWon();
        }
        return (float) Math.round(this.gamesWon() / (float) this.gamesLost() * 100) / 100;
    }

}
