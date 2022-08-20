package com.emirkabal.dervish.listeners;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import com.emirkabal.dervish.core.CustomItem;
import com.emirkabal.dervish.core.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
            } else if (sign.getLine(1).equalsIgnoreCase("§1§lWarp") && sign.getLine(2) != null && Core.getPosition(sign.getWorld().getName()+"."+sign.getLine(2).toLowerCase(Locale.ROOT)) != null) {
                CustomPlayer p = new CustomPlayer(e.getPlayer());
                p.giveKit();
                p.getSpigotPlayer().setFoodLevel(30);
                Location loc = Core.getPosition(sign.getWorld().getName()+"."+sign.getLine(2).toLowerCase(Locale.ROOT));
                if (p.getSpigotPlayer().getName().equalsIgnoreCase("EmaxTR")) {
                    p.sendActionBar("zumzum -kabal");
                }
                p.getSpigotPlayer().teleport(loc);
                p.getSpigotPlayer().sendMessage("§rWarped to "+sign.getLine(2)+"!");
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                    e.getPlayer().setGameMode(GameMode.SURVIVAL);
                }, 20);
            }
        }
    }

}
