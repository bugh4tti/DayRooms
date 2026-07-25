package com.dayrooms.managers;

import com.dayrooms.model.EffectData;
import com.dayrooms.model.Room;
import com.dayrooms.utils.LocationUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class RoomPersistenceManager {

    private final JavaPlugin plugin;
    private final File archivo;

    public RoomPersistenceManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.archivo = new File(plugin.getDataFolder(), "rooms.yml");
    }

    public void guardarUna(Room room) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(archivo);
        escribirRoom(yaml, room);
        guardarArchivo(yaml);
    }

    public void eliminarUna(String nombreRoom) {
        if (!archivo.exists()) {
            return;
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(archivo);
        yaml.set("rooms." + nombreRoom, null);
        guardarArchivo(yaml);
    }

    public void guardarTodas(RoomManager roomManager) {
        YamlConfiguration yaml = new YamlConfiguration();
        for (Room room : roomManager.listarTodas()) {
            escribirRoom(yaml, room);
        }
        guardarArchivo(yaml);
    }

    public void cargarTodas(RoomManager roomManager) {
        if (!archivo.exists()) {
            return;
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(archivo);
        ConfigurationSection raiz = yaml.getConfigurationSection("rooms");
        if (raiz == null) {
            return;
        }

        for (String nombre : raiz.getKeys(false)) {
            ConfigurationSection seccion = raiz.getConfigurationSection(nombre);
            if (seccion == null) continue;

            UUID owner = UUID.fromString(seccion.getString("owner"));
            Room room = roomManager.crear(nombre, owner);

            room.setKeepInventory(seccion.getBoolean("keepInventory", false));
            room.setUtilidadesHabilitadas(seccion.getBoolean("utilidadesHabilitadas", true));

            room.setEsquina1(LocationUtils.cargar(seccion, "esquina1"));
            room.setEsquina2(LocationUtils.cargar(seccion, "esquina2"));
            room.setEsquinasDefinidas(seccion.getBoolean("esquinasDefinidas", false));

            room.setBarreraEsquina1(LocationUtils.cargar(seccion, "barreraEsquina1"));
            room.setBarreraEsquina2(LocationUtils.cargar(seccion, "barreraEsquina2"));
            room.setBarreraDefinida(seccion.getBoolean("barreraDefinida", false));

            room.setTeleportLocation(LocationUtils.cargar(seccion, "teleportLocation"));
            room.setTeleportZoneDefinida(seccion.getBoolean("teleportZoneDefinida", false));

            ConfigurationSection efectos = seccion.getConfigurationSection("efectos");
            if (efectos != null) {
                for (String key : efectos.getKeys(false)) {
                    int nivel = efectos.getInt(key + ".nivel", 0);
                    long duracion = efectos.getLong(key + ".duracion", 0);
                    room.obtenerOCrearEfecto(key).setNivel(nivel);
                    room.obtenerOCrearEfecto(key).setDuracionSegundos(duracion);
                }
            }
        }

        plugin.getLogger().info("Cargadas " + raiz.getKeys(false).size() + " rooms desde rooms.yml");
    }

    private void escribirRoom(YamlConfiguration yaml, Room room) {
        String base = "rooms." + room.getName();
        yaml.set(base + ".owner", room.getOwner().toString());
        yaml.set(base + ".keepInventory", room.isKeepInventory());
        yaml.set(base + ".utilidadesHabilitadas", room.isUtilidadesHabilitadas());

        LocationUtils.guardar(yaml, base + ".esquina1", room.getEsquina1());
        LocationUtils.guardar(yaml, base + ".esquina2", room.getEsquina2());
        yaml.set(base + ".esquinasDefinidas", room.isEsquinasDefinidas());

        LocationUtils.guardar(yaml, base + ".barreraEsquina1", room.getBarreraEsquina1());
        LocationUtils.guardar(yaml, base + ".barreraEsquina2", room.getBarreraEsquina2());
        yaml.set(base + ".barreraDefinida", room.isBarreraDefinida());

        LocationUtils.guardar(yaml, base + ".teleportLocation", room.getTeleportLocation());
        yaml.set(base + ".teleportZoneDefinida", room.isTeleportZoneDefinida());

        for (var entrada : room.getEfectos().entrySet()) {
            EffectData datos = entrada.getValue();
            yaml.set(base + ".efectos." + entrada.getKey() + ".nivel", datos.getNivel());
            yaml.set(base + ".efectos." + entrada.getKey() + ".duracion", datos.getDuracionSegundos());
        }
    }

    private void guardarArchivo(YamlConfiguration yaml) {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            yaml.save(archivo);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "No se pudo guardar rooms.yml", e);
        }
    }
                }
