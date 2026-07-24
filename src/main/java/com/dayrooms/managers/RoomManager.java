package com.dayrooms.managers;

import com.dayrooms.model.Room;
import org.bukkit.Location;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class RoomManager {

    private final Map<String, Room> rooms = new LinkedHashMap<>();

    public Room crear(String nombre, UUID owner) {
        Room room = new Room(nombre, owner);
        rooms.put(nombre.toLowerCase(), room);
        return room;
    }

    public Room obtener(String nombre) {
        return rooms.get(nombre.toLowerCase());
    }

    public boolean existe(String nombre) {
        return rooms.containsKey(nombre.toLowerCase());
    }

    public void eliminar(String nombre) {
        rooms.remove(nombre.toLowerCase());
    }

    public Collection<Room> listarTodas() {
        return rooms.values();
    }

    public Collection<Room> listarDeJugador(UUID jugador) {
        return rooms.values().stream()
                .filter(r -> r.getOwner().equals(jugador) || r.getInvitados().contains(jugador))
                .toList();
    }

    /**
     * Busca la room cuyo cuboid (esquina1-esquina2) contiene la ubicación
     * dada. Se usa para saber, al morir un jugador, en qué room pasó.
     */
    public Room encontrarRoomPorUbicacion(Location ubicacion) {
        for (Room room : rooms.values()) {
            if (!room.isEsquinasDefinidas()) continue;

            Location loc1 = room.getEsquina1();
            Location loc2 = room.getEsquina2();
            if (loc1.getWorld() == null || !loc1.getWorld().equals(ubicacion.getWorld())) continue;

            int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
            int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
            int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
            int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
            int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
            int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

            if (ubicacion.getBlockX() >= minX && ubicacion.getBlockX() <= maxX
                    && ubicacion.getBlockY() >= minY && ubicacion.getBlockY() <= maxY
                    && ubicacion.getBlockZ() >= minZ && ubicacion.getBlockZ() <= maxZ) {
                return room;
            }
        }
        return null;
    }
                }
