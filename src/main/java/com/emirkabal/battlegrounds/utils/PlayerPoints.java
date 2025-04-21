package com.emirkabal.battlegrounds.utils;

import org.bukkit.Bukkit;

import java.util.*;

public class PlayerPoints {

    public static Map<String, Integer> points = new HashMap<>();

    public static List<Map.Entry<String, Integer>> getPoints() {
        LinkedList<Map.Entry<String, Integer>> list = new LinkedList(points.entrySet());

        if (list.isEmpty()) {
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
        return !getPoints().isEmpty();
    }

    public static String getFormattedPoints(int rank) {
        try {
            List board = getPoints();
            if (board.isEmpty()) {
                return "#" + (rank + 1);
            }
            if (board.get(rank) == null) {
                return "#" + (rank + 1);
            }
            String name = board.get(rank).toString().split("=")[0];
            if (hasPoints(name) == false) {
                return "#" + (rank + 1);
            }
            return "§r" + name + "§8:§r " + getPoints(name);
        } catch (Exception e) {
            return "#" + (rank + 1);
        }
    }

    public static int getRank(String name) {
        List board = getPoints();
        if (!hasPoints(name) || board.size() == 0) {
            return -1;
        }
        int result = 0;
        for (int i = 0; i < board.toArray().length; i++) {
            if (board.get(i) != null) {
                String indexName = board.get(i).toString().split("=")[0];
                if (indexName.equalsIgnoreCase(name)) {
                    result = i;
                }
            }
        }
        return result + 1;
    }

    public static boolean hasPoints(String name) {
        return points.get(name) != null;
    }

    public static int getPoints(String name) {
        return Objects.requireNonNullElse(points.get(name), 0);
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
