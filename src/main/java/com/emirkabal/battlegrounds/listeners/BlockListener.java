package com.emirkabal.battlegrounds.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
                        e.getClickedBlock().getType() == Material.TRAPPED_CHEST ||
                        e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE ||
                        e.getClickedBlock().getType() == Material.BREWING_STAND ||
                        e.getClickedBlock().getType() == Material.BREWING_STAND_ITEM ||
                        e.getClickedBlock().getType() == Material.ITEM_FRAME ||
                        e.getClickedBlock().getType() == Material.MINECART ||
                        e.getClickedBlock().getType() == Material.STORAGE_MINECART
                )
        ) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void FrameRotate(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) {
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent e){
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }


    // block item frame breaking
    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        if (e.getRemover() instanceof Player) {
            Player p = (Player) e.getRemover();
            if (p.getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
            }
        }
    }


    // block fire spreading
    @EventHandler
    public void igniteEvent(BlockIgniteEvent e) {
        if (e.getCause() == BlockIgniteEvent.IgniteCause.SPREAD || e.getCause() == BlockIgniteEvent.IgniteCause.LAVA) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        if (!(e.getEntity() instanceof Player))
            e.setCancelled(true);
    }

    @EventHandler
    public void LeavesDecay(LeavesDecayEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        e.setCancelled(e.toWeatherState());
    }

}

