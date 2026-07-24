package com.dayrooms.managers;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Maneja el estado de selección de cada jugador: en qué room está
 * editando, qué herramienta tiene activa (esquinas/barrera/teleport)
 * y los puntos que va marcando con click der./izq.
 */
public class SelectionManager {

    public enum Modo {
        NINGUNO, ESQUINAS, BARRERA, TELEPORT
    }

    private final Map<UUID, String> roomEnEdicion = new HashMap<>();
    private final Map<UUID, Modo> modoActual = new HashMap<>();
    private final Map<UUID, Location> puntoLado1 = new HashMap<>();
    private final Map<UUID, Location> puntoLado2 = new HashMap<>();

    public void setRoomEnEdicion(UUID jugador, String nombreRoom) {
        roomEnEdicion.put(jugador, nombreRoom);
    }

    public String getRoomEnEdicion(UUID jugador) {
        return roomEnEdicion.get(jugador);
    }

    public void setModo(UUID jugador, Modo modo) {
        modoActual.put(jugador, modo);
        puntoLado1.remove(jugador);
        puntoLado2.remove(jugador);
    }

    public Modo getModo(UUID jugador) {
        return modoActual.getOrDefault(jugador, Modo.NINGUNO);
    }

    public void marcarLado1(UUID jugador, Location loc) {
        puntoLado1.put(jugador, loc);
    }

    public void marcarLado2(UUID jugador, Location loc) {
        puntoLado2.put(jugador, loc);
    }

    public boolean tieneAmbosLados(UUID jugador) {
        return puntoLado1.containsKey(jugador) && puntoLado2.containsKey(jugador);
    }

    public Location getLado1(UUID jugador) {
        return puntoLado1.get(jugador);
    }

    public Location getLado2(UUID jugador) {
        return puntoLado2.get(jugador);
    }

    public void limpiarSeleccion(UUID jugador) {
        modoActual.put(jugador, Modo.NINGUNO);
        puntoLado1.remove(jugador);
        puntoLado2.remove(jugador);
    }
  }
