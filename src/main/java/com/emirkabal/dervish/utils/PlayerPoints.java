package com.emirkabal.dervish.utils;

import org.bukkit.Bukkit;
import java.util.*;

public class PlayerPoints {

    public static Map<String, Integer> points = new HashMap<>();

    public static List<Map.Entry<String, Integer>> getPoints() {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(points.entrySet());

        if (list.size() == 0) {
            return (new ArrayList());
        }

        Collections.sort(list, (o2, o1) -> {
            return o1.getValue().compareTo(o2.getValue());
        });

        return list;
    }

    public static String getWinner() {
        return getPoints().get(0).toString().split("=")[0];
    }

    public static boolean hasWinner() {
        return getPoints().size() != 0;
    }

    public static String getFormattedPoints(int rank) {
        try {
            List board = getPoints();
            if (board.size() == 0) return "#" + (rank + 1);
            if (board.get(rank) == null) return "#" + (rank + 1);
            String name = board.get(rank).toString().split("=" )[0];
            if ( hasPoints(name) == false) return "#"+(rank+1);
            return "§r"+name +"§8:§r "+getPoints(name);
        } catch (Exception e) {
            return "#"+(rank+1);
        }
    }

    public static boolean hasPoints(String name) {
        return points.get(name) != null;
    }
    public static int getPoints(String name) {
        return points.get(name) != null ? points.get(name) : 0;
    }

    public static void addPoints(String name) {
        if (points.get(name) == null) {
            points.put(name, 1);
        } else {
            points.put(name, points.get(name) + 1);
        }
    }

    public static void removeAllPoints() {
        points.clear();
    }

}
