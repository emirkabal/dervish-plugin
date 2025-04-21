package com.emirkabal.battlegrounds.core;

import com.emirkabal.battlegrounds.Core;
import com.emirkabal.battlegrounds.Main;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import org.bukkit.inventory.PlayerInventory;

public class CustomPlayer extends BukkitRunnable {

    private Player player;
    public static HashMap<Player, CustomPlayer> customplayer = new HashMap<>();

    public CustomPlayer(Player player) {
        if (customplayer.get(this.player) == null) {
            this.player = player;
            customplayer.put(player, this);
            runTaskTimer(Main.getInstance(), 0, 1);
        }
    }

    public Player getSpigotPlayer() {
        return this.player;
    }

    public void clearInventory() {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().clear();
    }

    public void sendFirework(FireworkEffect.Type type, Color color1, Color color2, Integer power) {
        Core.spawnFirework(this.player.getLocation(), type, color1, color2, power);
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

    public void sendSound(Location location, Sound sound, float bir, float iki) {
        player.playSound(location, sound, bir, iki);
    }

    public void giveKit() {

        HashMap<String, Integer> slots = Core.dataManager.getData("slots." + this.player.getUniqueId().toString(), HashMap.class);
        if (slots == null) {
            slots = new HashMap<>();
            slots.put("sword", 0);
            slots.put("food", 1);
            slots.put("rod", 2);
            slots.put("bow", 3);
            slots.put("arrow", 8);
        }

        PlayerInventory inventory = this.player.getInventory();

        this.clearInventory();
        inventory.setItem(slots.getOrDefault("sword", 0), CustomItem.of(Material.IRON_SWORD).setUnbreakable(true).withName("§7§lBattlegrounds§r Sword").get());
        inventory.setItem(slots.getOrDefault("rod", 2), CustomItem.of(Material.FISHING_ROD).setUnbreakable(true).withName("§7§lBattlegrounds§r Rod").get());
        inventory.setItem(slots.getOrDefault("bow", 3), CustomItem.of(Material.BOW).setUnbreakable(true).withName("§7§lBattlegrounds§r Bow").get());

        if (inventory.getItem(slots.getOrDefault("food", 1)) == null) {
            inventory.setItem(slots.getOrDefault("food", 1), CustomItem.getFood(10));
        } else {
            inventory.addItem(CustomItem.getFood(10));
        }

        if (inventory.getItem(slots.getOrDefault("arrow", 8)) == null) {
            inventory.setItem(slots.getOrDefault("arrow", 8), CustomItem.getArrow(7));
        } else {
            inventory.addItem(CustomItem.getArrow(7));
        }

        inventory.setHelmet(CustomItem.of(Material.IRON_HELMET).setUnbreakable(true).withName("§7§lBattlegrounds§r Helmet").get());
        inventory.setChestplate(CustomItem.of(Material.IRON_CHESTPLATE).setUnbreakable(true).withName("§7§lBattlegrounds§r Kevlar Vest").get());
        inventory.setLeggings(CustomItem.of(Material.IRON_LEGGINGS).setUnbreakable(true).withName("§7§lBattlegrounds§r Pants").get());
        inventory.setBoots(CustomItem.of(Material.IRON_BOOTS).setUnbreakable(true).withName("§7§lBattlegrounds§r Boots").get());
    }

    public void fix() {
        this.player.teleport(this.player);
        for (Player all : Bukkit.getOnlinePlayers()) {
            this.player.hidePlayer(this.player);
            this.player.hidePlayer(all);
            all.hidePlayer(this.player);
            this.player.showPlayer(all);
            this.player.showPlayer(this.player);
            all.showPlayer(this.player);
        }

    }

    public void removePotionEffects() {
        for (PotionEffect effect : this.player.getActivePotionEffects()) {
            this.player.removePotionEffect(effect.getType());
        }
    }

    public void repairAllItems() {
        for (ItemStack itemStack : this.player.getInventory().getContents()) {
            if (itemStack != null) {
                itemStack.setDurability((short) 0);
            }
        }
        for (ItemStack itemStack : this.player.getEquipment().getArmorContents()) {
            if (itemStack != null) {
                itemStack.setDurability((short) 0);
            }
        }
    }

    // respawn callers
    public void deathSpectate() {
        this.player.setMaxHealth(20.0D);
        this.player.setHealth(this.player.getMaxHealth());
        this.player.setGameMode(GameMode.SPECTATOR);
        if (!this.player.isFlying()) {
            this.player.setAllowFlight(true);
            this.player.setFlying(true);
        }
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 2));
    }

    public void forceRespawn() {
        this.player.hidePlayer(this.player);
        this.player.teleport(Core.getSpawn(this.player.getWorld(), ""));
        this.player.setGameMode(GameMode.ADVENTURE);
        if (this.player.isFlying()) {
            this.player.setFlying(false);
            this.player.setAllowFlight(false);
        }
        this.clearInventory();
        this.player.setMaxHealth(20);
        this.player.setHealth(20);
        this.player.setFoodLevel(20);
        this.player.setFireTicks(0);
        new BukkitRunnable() {
            @Override
            public void run() {
                CustomPlayer.this.player.showPlayer(CustomPlayer.this.player);
            }
        }.runTaskLater(Main.getInstance(), 10);
        this.removePotionEffects();
    }

    public void sendActionBar(String message) {
        CraftPlayer player = (CraftPlayer) this.player;
        IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
        player.getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

    @Override
    public void run() {
        if (player.isDead()) {
            cancel();
        }
    }
}
