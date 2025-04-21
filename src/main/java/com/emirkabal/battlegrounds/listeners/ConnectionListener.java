package com.emirkabal.battlegrounds.listeners;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import com.emirkabal.battlegrounds.Core;
import com.emirkabal.battlegrounds.Main;
import com.emirkabal.battlegrounds.core.CustomPlayer;
import com.emirkabal.battlegrounds.utils.Sidebar;
import com.emirkabal.battlegrounds.utils.StatsManager;

public class ConnectionListener implements Listener {

    public static ArrayList<Player> readyPlayers = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player p = e.getPlayer();
        readyPlayers.remove(p);
        p.setLevel(0);
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.NOTE_STICKS, 1, 1);
        }
        if (PlayerListener.lastdamage.get(p) != null && p.getGameMode() == GameMode.SURVIVAL) {
            p.setHealth(0);
        }

        e.setQuitMessage((p.isOp() ? "§4" : "§a") + p.getDisplayName() + " §7left the game.");
    }

    public static void readyPlayer(Player p) {
        readyPlayers.add(p);
        p.teleport(Core.getSpawn(Bukkit.getWorld(Core.currentWorld), ""));
        Sidebar.setScoreboard(p);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage((p.isOp() ? "§4" : "§a") + p.getDisplayName() + " §7joined the game.");
        CustomPlayer cp = new CustomPlayer(p);
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.NOTE_PIANO, 1, 1);
        }
        p.teleport(Core.getPosition("lobby.spawn"));
        cp.clearInventory();
        cp.removePotionEffects();
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(1000);
        p.setLevel(0);
        p.setExp(0);

        p.sendMessage("§eWelcome to The Battlegrounds§8!");
        p.sendMessage("§7Please do not use any cheats or hacks.");
        p.sendMessage("§7If you do, you will be banned from the server.");
        p.sendMessage("§7If you have any questions, please ask a staff member.");
        p.sendMessage("§7If you want to report a player, please use /report <player> <reason>");
        p.sendMessage("Warning: Redirecting to the battlegrounds...");

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            readyPlayer(p);
        }, 100L);
    }

    // @EventHandler
    // public void onLogin(LoginEvent e) {
    //     e.getPlayer().teleport(Core.getSpawn(Bukkit.getWorld(Core.currentWorld), ""));
    //     Sidebar.setScoreboard(e.getPlayer());
    // }
    // @EventHandler
    // public void onRegister(RegisterEvent e) {
    //     AuthMeApi.getInstance().forceLogin(e.getPlayer());
    // }
    @EventHandler
    public void onConnect(PlayerLoginEvent e) {
        if (e.getPlayer().isOp() && Bukkit.getServer().getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
            e.allow();
            ArrayList<Player> allPlayers = new ArrayList<>();
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!players.isOp()) {
                    allPlayers.add(players);
                }
            }
            int random = new Random().nextInt(allPlayers.size());
            Player picked = allPlayers.get(random);
            picked.kickPlayer("A staff member logged into the server.");
        }
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        e.setMaxPlayers(32);
        e.setMotd("§e§lThe Battlegrounds Server §6[Kit PvP]\n§r§bCustom PvP Hits - Improved Fishing Rod ");
    }

}
