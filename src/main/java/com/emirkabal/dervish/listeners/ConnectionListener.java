package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.core.CustomPlayer;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class ConnectionListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e){
        e.setQuitMessage(null);
        Player p = e.getPlayer();
        p.setHealth(0);
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
        p.teleport(Core.getSpawn(Bukkit.getWorld("lobby"), "lobby"));
        cp.clearInventory();
        cp.removePotionEffects();
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(1000);
    }

    @EventHandler
    public void onLogin(LoginEvent e) {
        e.getPlayer().teleport(Core.getSpawn(Bukkit.getWorld(Core.currentWorld), ""));
    }

    @EventHandler
    public void onRegister(RegisterEvent e) {
        AuthMeApi.getInstance().forceLogin(e.getPlayer());
    }


    @EventHandler
    public void onPing(ServerListPingEvent e) {
        e.setMaxPlayers(24);
        e.setMotd("§e§lThe Battlegrounds Server §6[Kit PvP]\n§r§bCustom PvP Hits - Improved Fishing Rod ");
    }


}
