package com.emirkabal.dervish.utils;
import java.util.Random;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;

public class RandomColor {

    public static Color dye() {
        Random random = new Random();
        int chance = random.nextInt(16);
        if (chance <= 2) {
            return Color.AQUA;
        } else if (chance <= 3) {
            return Color.YELLOW;
        } else if (chance <= 4) {
            return Color.BLUE;
        } else if (chance <= 5) {
            return Color.FUCHSIA;
        } else if (chance <= 6) {
            return Color.GRAY;
        } else if (chance <= 7) {
            return Color.GREEN;
        } else if (chance <= 8) {
            return Color.LIME;
        } else if (chance <= 9) {
            return Color.MAROON;
        } else if (chance <= 10) {
            return Color.NAVY;
        } else if (chance <= 11) {
            return Color.OLIVE;
        } else if (chance <= 12) {
            return Color.ORANGE;
        } else if (chance <= 13) {
            return Color.PURPLE;
        } else if (chance <= 14) {
            return Color.RED;
        } else if (chance <= 15) {
            return Color.SILVER;
        } else if (chance <= 16) {
            return Color.TEAL;
        } else {
            return Color.SILVER;
        }
    }

    public static ChatColor chat() {
        Random random = new Random();
        int chance = random.nextInt(16);
        if (chance <= 6) {
            return ChatColor.AQUA;
        } else if (chance <= 7) {
            return ChatColor.BLUE;
        } else if (chance <= 8) {
            return ChatColor.DARK_AQUA;
        } else if (chance <= 9) {
            return ChatColor.DARK_BLUE;
        } else if (chance <= 10) {
            return ChatColor.DARK_GREEN;
        } else if (chance <= 11) {
            return ChatColor.DARK_PURPLE;
        } else if (chance <= 12) {
            return ChatColor.DARK_RED;
        } else if (chance <= 13) {
            return ChatColor.GOLD;
        } else if (chance <= 14) {
            return ChatColor.GREEN;
        } else if (chance <= 15) {
            return ChatColor.LIGHT_PURPLE;
        } else if (chance <= 16) {
            return ChatColor.RED;
        } else {
            return ChatColor.YELLOW;
        }
    }
}
