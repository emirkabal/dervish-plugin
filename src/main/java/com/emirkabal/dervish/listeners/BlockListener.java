package com.emirkabal.dervish.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class BlockListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockChange(PlayerInteractEvent e){
        if(e.getAction() == Action.PHYSICAL && e.getPlayer().getGameMode() != GameMode.CREATIVE){
            e.setCancelled(true);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && e.getPlayer().getGameMode() != GameMode.CREATIVE &&
                (
                        e.getClickedBlock().getType() == Material.ANVIL ||
                        e.getClickedBlock().getType() == Material.WORKBENCH ||
                        e.getClickedBlock().getType() == Material.FURNACE ||
                        e.getClickedBlock().getType() == Material.BURNING_FURNACE ||
                        e.getClickedBlock().getType() == Material.CHEST ||
                        e.getClickedBlock().getType() == Material.ENDER_CHEST ||
                        e.getClickedBlock().getType() == Material.BED ||
                        e.getClickedBlock().getType() == Material.BED_BLOCK ||
                        e.getClickedBlock().getType() == Material.TRAPPED_CHEST
                )
        ) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent e){
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        e.setCancelled(e.toWeatherState());
    }

}

