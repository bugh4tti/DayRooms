package com.dayrooms.managers;

import com.dayrooms.model.Room;

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
                        }
