package com.dayrooms.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Guarda victorias por jugador en stats.yml (plugins/DayRooms/stats.yml).
 */
public class StatsManager {

    private final JavaPlugin plugin;
    private final File archivo;
    private YamlConfiguration yaml;

    public StatsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.archivo = new File(plugin.getDataFolder(), "stats.yml");
        cargar();
    }

    private void cargar() {
        if (!archivo.exists()) {
            yaml = new YamlConfiguration();
            return;
        }
        yaml = YamlConfiguration.loadConfiguration(archivo);
    }

    public void sumarVictoria(UUID jugador, String nombreJugador) {
        String ruta = "victorias." + jugador;
        int actuales = yaml.getInt(ruta, 0);
        yaml.set(ruta, actuales + 1);
        yaml.set("nombres." + jugador, nombreJugador);
        guardar();
    }

    public int getVictorias(UUID jugador) {
        return yaml.getInt("victorias." + jugador, 0);
    }

    public int getVictorias(String nombreJugador) {
        var seccionNombres = yaml.getConfigurationSection("nombres");
        if (seccionNombres == null) return 0;

        for (String uuidStr : seccionNombres.getKeys(false)) {
            String nombreGuardado = seccionNombres.getString(uuidStr);
            if (nombreGuardado != null && nombreGuardado.equalsIgnoreCase(nombreJugador)) {
                return yaml.getInt("victorias." + uuidStr, 0);
            }
        }
        return 0;
    }

    private void guardar() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            yaml.save(archivo);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "No se pudo guardar stats.yml", e);
        }
    }
              }
