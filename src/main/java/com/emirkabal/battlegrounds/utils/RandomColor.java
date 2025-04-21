package com.emirkabal.battlegrounds.utils;
import java.util.Random;
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
}
