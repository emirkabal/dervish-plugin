package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import com.emirkabal.dervish.core.CustomPlayer;
import com.emirkabal.dervish.utils.WarriorSpawner;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Locale;

public class ActionListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClickEntity(PlayerInteractAtEntityEvent e){
        if(e.getRightClicked() instanceof ArmorStand && e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().startsWith("§a")){
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && e.getClickedBlock().getState() != null && (e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST)) {
            Sign sign = (Sign) e.getClickedBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase("[pos]") && sign.getLine(1) != null) {
                sign.setLine(0, "");
                sign.setLine(3, "");
                sign.setLine(2, sign.getLine(1));
                sign.setLine(1, "§1§lWarp");
                sign.update();
            } else if (sign.getLine(1).equalsIgnoreCase("§1§lWarp") && sign.getLine(2) != null && Core.getPosition(sign.getLine(2).toLowerCase(Locale.ROOT)) != null) {
                CustomPlayer p = new CustomPlayer(e.getPlayer());
                p.giveKit();
                e.getPlayer().teleport(Core.getPosition(sign.getLine(2).toLowerCase(Locale.ROOT)));
                e.getPlayer().sendMessage(Main.PREFIX+"You teleported to "+sign.getLine(2)+".");
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                    e.getPlayer().setGameMode(GameMode.SURVIVAL);
                }, 20 * 2);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(e.getItem() != null && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem().getItemMeta() != null && CustomItem.isEventItem(e.getItem()) == true){
            Player p = e.getPlayer();
            Location zombiposloc = p.getLocation();
            new WarriorSpawner(zombiposloc.getWorld(), zombiposloc.getX(), zombiposloc.getY(), zombiposloc.getZ());
            p.sendMessage(Main.PREFIX+"Event created: "+zombiposloc.getX()+", "+zombiposloc.getY()+", "+zombiposloc.getZ());
        }
    }

}
