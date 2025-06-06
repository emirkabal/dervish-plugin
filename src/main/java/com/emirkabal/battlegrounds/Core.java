package com.emirkabal.battlegrounds;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.emirkabal.battlegrounds.utils.ConfigUtil;
import com.emirkabal.battlegrounds.utils.DataManager;
import com.emirkabal.battlegrounds.utils.RandomColor;
import com.emirkabal.battlegrounds.utils.Utils;

public class Core {

    public static String currentWorld;
    public static int currentWorldNum = 0;
    public static ArrayList<String> worldNames = new ArrayList<>();
    public static FileConfiguration cfg = ConfigUtil.conf();
    public static DataManager dataManager = new DataManager(Main.getInstance());

    public static void setPosition(String positionName, Double x, Double y, Double z, Float yaw, Float pitch, String world) {
        cfg.set("positions." + positionName + ".x", x);
        cfg.set("positions." + positionName + ".y", y);
        cfg.set("positions." + positionName + ".z", z);
        cfg.set("positions." + positionName + ".yaw", yaw);
        cfg.set("positions." + positionName + ".pitch", pitch);
        cfg.set("positions." + positionName + ".world", world);
        ConfigUtil.saveAll();
    }

    public static Location getPosition(String positionName) {
        if (cfg.getString("positions." + positionName + ".world") == null || cfg.getString("positions." + positionName + ".x") == null || cfg.getString("positions." + positionName + ".y") == null || cfg.getString("positions." + positionName + ".z") == null || cfg.getString("positions." + positionName + ".yaw") == null || cfg.getString("positions." + positionName + ".pitch") == null) {
            return null;
        }
        Location loc = new Location(Bukkit.getWorld(cfg.getString("positions." + positionName + ".world")), cfg.getDouble("positions." + positionName + ".x"), cfg.getDouble("positions." + positionName + ".y") - 1.5, cfg.getDouble("positions." + positionName + ".z"), cfg.getLong("positions." + positionName + ".yaw"), cfg.getLong("positions." + positionName + ".pitch"));
        return loc;
    }

    public static void loadWorlds() {
        System.out.println(cfg.getStringList("worlds"));
        worldNames = (ArrayList<String>) cfg.getStringList("worlds");
        if (worldNames == null || worldNames.toArray().length == 0) {
            return;
        }
        currentWorld = worldNames.get(0);
        System.out.println(currentWorld);
        for (World world : Bukkit.getWorlds()) {
            Bukkit.unloadWorld(world, false);
        }
        for (String worldName : worldNames) {
            new WorldCreator(worldName).createWorld();
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Loaded world: " + worldName);
        }
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Loaded total " + worldNames.toArray().length + " world(s).");
        new WorldCreator("lobby").createWorld();
        new WorldCreator("arena").createWorld();
    }

    public static void addWorld(String worldName) {
        worldNames.add(worldName);
        cfg.set("worlds", worldNames);
        ConfigUtil.saveAll();
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Added world: " + worldName);
    }

    public static void removeWorld(String worldName) {
        worldNames.remove(worldName);
        cfg.set("worlds", worldNames);
        ConfigUtil.saveAll();
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Removed world: " + worldName);
    }

    public static void changeWorld(String worldName, World world) {

        currentWorld = worldName;

        for (Player player : world.getPlayers()) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                player.teleport(Core.getSpawn(world, ""));
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                player.getInventory().setHelmet(null);
                player.getInventory().setChestplate(null);
                player.getInventory().setLeggings(null);
                player.getInventory().setBoots(null);
                player.setMaxHealth(20);
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.setFireTicks(0);
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }

        Core.dataManager.save();
        Core.sendMessageToAll(Main.PREFIX + "Map changed to " + worldName + "!");
    }

    public static int getPing(Player player) {
        try {
            Method handle = player.getClass().getMethod("getHandle");
            Object nmsHandle = handle.invoke(player);
            Field pingField = nmsHandle.getClass().getField("ping");
            return pingField.getInt(nmsHandle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
                | NoSuchFieldException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception while trying to get player ping.", e);
        }

        return -1;
    }

    public static void sendMessageToAll(String message) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(message);
        }
    }

    public static Location getSpawn(World w, String position) {
        if (getPosition(position.length() > 0 ? position : currentWorld + ".spawn") == null) {
            return w.getSpawnLocation();
        } else {
            return getPosition(position.length() > 0 ? position : currentWorld + ".spawn");
        }
    }

    public static void spawnFirework(Location loc, FireworkEffect.Type type, Color color1, Color color2, Integer power) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(power);
        fwm.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(type).withColor(color1).withFade(color2).build());
        fw.setFireworkMeta(fwm);
    }

    public static void applyWinnerEffects(Player p) {

        for (Location loc : Utils.getCircle(p.getLocation().add(0, 1, 0), 2, 10)) {
            spawnFirework(loc, FireworkEffect.Type.STAR, RandomColor.dye(), RandomColor.dye(), 1);
            spawnFirework(p.getLocation(), FireworkEffect.Type.STAR, RandomColor.dye(), RandomColor.dye(), 1);
        }
        new BukkitRunnable() {
            int fireworks = 0;

            @Override
            public void run() {
                if (fireworks >= 9) {
                    cancel();
                }
                fireworks++;
                spawnFirework(p.getLocation(), FireworkEffect.Type.STAR, RandomColor.dye(), RandomColor.dye(), 1);
                for (Location loc : Utils.getCircle(p.getLocation().add(0, 1, 0), 2, 10)) {
                    spawnFirework(loc, FireworkEffect.Type.STAR, RandomColor.dye(), RandomColor.dye(), 1);
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 13);
    }

}
