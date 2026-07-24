package com.dayrooms.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Convierte Location <-> ConfigurationSection para poder guardarlas
 * en rooms.yml y volver a cargarlas al reiniciar el server.
 */
public class LocationUtils {

    public static void guardar(ConfigurationSection seccion, String clave, Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return;
        }
        seccion.set(clave + ".world", loc.getWorld().getName());
        seccion.set(clave + ".x", loc.getX());
        seccion.set(clave + ".y", loc.getY());
        seccion.set(clave + ".z", loc.getZ());
    }

    public static Location cargar(ConfigurationSection seccion, String clave) {
        if (seccion == null || !seccion.isSet(clave + ".world")) {
            return null;
        }
        String nombreMundo = seccion.getString(clave + ".world");
        World mundo = Bukkit.getWorld(nombreMundo);
        if (mundo == null) {
            return null; // el mundo todavía no cargó o no existe
        }
        double x = seccion.getDouble(clave + ".x");
        double y = seccion.getDouble(clave + ".y");
        double z = seccion.getDouble(clave + ".z");
        return new Location(mundo, x, y, z);
    }
          }
