package com.dayrooms.managers;

import com.dayrooms.utils.ColorUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageManager {

    private final JavaPlugin plugin;

    public MessageManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public String get(String key) {
        String crudo = plugin.getConfig().getString("mensajes." + key, "");
        return ColorUtils.traducir(crudo);
    }

    public void recargar() {
        plugin.reloadConfig();
    }
}
