package com.emirkabal.battlegrounds.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private final JavaPlugin plugin;
    private final File dataFile;
    private final Gson gson;
    private Map<String, Object> data;

    public DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.json");
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.data = new HashMap<>();
        this.loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> rawData = gson.fromJson(reader, type);
            if (rawData != null) {
                data = rawData;

                plugin.getLogger().info("Data loaded successfully from data.json");
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to load data.json: " + e.getMessage());
        }
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(String key, Class<T> type) {
        Object value = data.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    public void removeData(String key) {
        data.remove(key);
    }

    public void save() {
        try {
            if (!dataFile.getParentFile().exists()) {
                dataFile.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(dataFile)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save data.json: " + e.getMessage());
        }
    }
}
