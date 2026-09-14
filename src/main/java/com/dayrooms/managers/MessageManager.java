package com.dayrooms.managers;

import com.dayrooms.utils.ColorUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MessageManager {

    private final JavaPlugin plugin;

    public MessageManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Devuelve el mensaje con el prefix del plugin antepuesto.
     * Usar para todo lo que se manda por chat.
     */
    public String get(String key) {
        String prefix = plugin.getConfig().getString("prefix", "");
        String crudo = plugin.getConfig().getString("mensajes." + key, "");
        return ColorUtils.traducir(prefix + crudo);
    }

    /**
     * Devuelve el mensaje SIN el prefix. Usar para titulos/subtitulos
     * (sendTitle) donde el prefix de chat no tiene sentido visual.
     */
    public String getSinPrefix(String key) {
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
