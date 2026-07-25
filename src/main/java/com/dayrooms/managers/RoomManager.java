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

    public Room encontrarRoomPorUbicacion(Location ubicacion) {
        for (Room room : rooms.values()) {
            if (!room.isEsquinasDefinidas()) continue;
            if (dentroDeCuboid(room.getEsquina1(), room.getEsquina2(), ubicacion)) {
                return room;
            }
        }
        return null;
    }

    public Room encontrarRoomPorUbicacionEnBarrera(Location ubicacion) {
        for (Room room : rooms.values()) {
            if (!room.isBarreraDefinida()) continue;
            if (dentroDeCuboid(room.getBarreraEsquina1(), room.getBarreraEsquina2(), ubicacion)) {
                return room;
            }
        }
        return null;
    }

    private boolean dentroDeCuboid(Location loc1, Location loc2, Location ubicacion) {
        if (loc1 == null || loc2 == null || loc1.getWorld() == null) return false;
        if (!loc1.getWorld().equals(ubicacion.getWorld())) return false;

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        return ubicacion.getBlockX() >= minX && ubicacion.getBlockX() <= maxX
                && ubicacion.getBlockY() >= minY && ubicacion.getBlockY() <= maxY
                && ubicacion.getBlockZ() >= minZ && ubicacion.getBlockZ() <= maxZ;
    }
            }
