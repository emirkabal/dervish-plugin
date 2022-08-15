package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.core.CustomPlayer;
import com.emirkabal.dervish.utils.Sidebar;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RegisterEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.ArrayList;
import java.util.Random;

public class ConnectionListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage(null);
        Player p = e.getPlayer();
        if (PlayerListener.lastdamage.get(p) != null) {
            p.setHealth(0);
        }
        e.setQuitMessage((p.isOp() ? "§4" : "§a")+p.getDisplayName()+" §7left the game.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        e.setJoinMessage((p.isOp() ? "§4" : "§a")+p.getDisplayName()+" §7joined the game.");
        CustomPlayer cp = new CustomPlayer(p);
        for ( Player  players : Bukkit.getOnlinePlayers()) {
            players.playSound(p.getLocation(), Sound.NOTE_PIANO, 0.3F, 0.3F);
        }
        p.teleport(Core.getPosition("lobby.spawn"));
        cp.clearInventory();
        cp.removePotionEffects();
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(1000);
    }

    @EventHandler
    public void onLogin(LoginEvent e) {
        e.getPlayer().teleport(Core.getSpawn(Bukkit.getWorld(Core.currentWorld), ""));
        Sidebar.setScoreboard(e.getPlayer());
    }

    @EventHandler
    public void onRegister(RegisterEvent e) {
        AuthMeApi.getInstance().forceLogin(e.getPlayer());
    }

    @EventHandler
    public void onConnect(PlayerLoginEvent e) {
        if (e.getPlayer().isOp() && Bukkit.getServer().getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
            e.allow();
            ArrayList<Player> allPlayers = new ArrayList<Player>();
            for(Player players : Bukkit.getOnlinePlayers()) {
                if (!players.isOp()) allPlayers.add(players);
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
