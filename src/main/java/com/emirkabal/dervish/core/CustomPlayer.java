package com.emirkabal.dervish.core;

import com.emirkabal.dervish.Core;
import com.emirkabal.dervish.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CustomPlayer extends BukkitRunnable {

    private Player player;
    public static HashMap<Player, CustomPlayer> customplayer = new HashMap<>();
    public CustomPlayer(Player player){
        if(this.customplayer.get(this.player) == null) {
            this.player = player;
            this.customplayer.put(player, this);
            runTaskTimer(Main.getInstance(), 0, 1);
        }
    }


    public Player getSpigotPlayer() {
        return this.player;
    }

    public void clearInventory(){
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().clear();
    }

    public void sendFirework(FireworkEffect.Type type, Color color1, Color color2, Integer power) {
        Location loc = player.getLocation();
        Firework fw = (Firework)player.getLocation().getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(power);
        fwm.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(type).withColor(color1).withFade(color2).build());
        fw.setFireworkMeta(fwm);
    }

    public void sendTitle(String label1, String label2) {
        player.sendTitle(label1, label2);
    }

//    public void sendActionMessage(String message) {
//        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, BaseComponent.toLegacyText(TextComponent.fromLegacyText(message)));
//    }

    public Location getLocation() {
        return player.getLocation();
    }

    public void sendPotion(PotionEffectType potioneffecttype, Integer label, Integer label2) {
        player.addPotionEffect(new PotionEffect(potioneffecttype, label, label2));
    }

    public void sendLightining() {
        World world = player.getWorld();
        world.strikeLightningEffect(player.getLocation());
        player.damage(3.5D);
    }

    public void sendSound(Location location, Sound sound, Integer bir, Integer iki) {
        player.playSound(location, sound, (float)bir, (float)iki);
    }
    public void giveAllahSword(){
        this.player.getLocation().getWorld().dropItem(player.getLocation(), CustomItem.of(Material.STICK).withName("§9Allahın sopası").get());
    }
    public void giveKit() {
        this.clearInventory();
        ItemStack ironSword = new ItemStack(Material.IRON_SWORD, 1);
        ItemStack steak = new ItemStack(Material.COOKED_BEEF, 10);
        ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemStack arrow = new ItemStack(Material.ARROW, 6);
        ItemStack ironHelmet = new ItemStack(Material.IRON_HELMET);
        ItemStack ironChestPlate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack ironLeggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
        this.player.getInventory().setItem(0, ironSword);
        this.player.getInventory().setItem(1, steak);
        this.player.getInventory().setItem(2, rod);
        this.player.getInventory().setItem(3, bow);
        this.player.getInventory().setItem(8, arrow);
        this.player.getInventory().setHelmet(ironHelmet);
        this.player.getInventory().setChestplate(ironChestPlate);
        this.player.getInventory().setLeggings(ironLeggings);
        this.player.getInventory().setBoots(ironBoots);

    }

    public void fix() {
        this.player.teleport(this.player);
        for(Player all : Bukkit.getOnlinePlayers()) {
            this.player.hidePlayer(this.player);
            this.player.hidePlayer(all);
            all.hidePlayer(this.player);
            this.player.showPlayer(all);
            this.player.showPlayer(this.player);
            all.showPlayer(this.player);
        }

    }

    public void deathSpectate() {
        this.player.setMaxHealth(20.0D);
        this.player.setHealth(20.0D);
        this.player.setGameMode(GameMode.SPECTATOR);
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 2));
    }

    public void forceRespawn() {
        this.player.setMaxHealth(20);
        this.player.setHealth(20);
        this.player.setFoodLevel(20);
        this.player.setFireTicks(0);
        this.clearInventory();
        this.player.setGameMode(GameMode.SURVIVAL);
        if (Core.getPosition("spawn") != null) this.player.teleport(Core.getPosition("spawn"));
        else this.player.teleport(this.player.getWorld().getSpawnLocation());
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.hidePlayer(this.player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            CustomPlayer.this.player.showPlayer(CustomPlayer.this.player);
            cancel();
        }, 10L);
        for(PotionEffect effect : this.player.getActivePotionEffects()) {
            this.player.removePotionEffect(effect.getType());
        }
    }


    @Override
    public void run() {
        if(player.isDead()) cancel();
    }
}
