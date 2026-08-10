package com.dayrooms.managers;

import com.dayrooms.utils.ColorUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MessageManager {

    private final JavaPlugin plugin;

    public MessageManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public String get(String key) {
        String crudo = plugin.getConfig().getString("mensajes." + key, "");
        return ColorUtils.traducir(crudo);
    }

    public int getInt(String key, int valorPorDefecto) {
        return plugin.getConfig().getInt(key, valorPorDefecto);
    }

    public List<String> getStringList(String key) {
        return plugin.getConfig().getStringList(key);
    }

    public void recargar() {
        plugin.reloadConfig();
    }
}
