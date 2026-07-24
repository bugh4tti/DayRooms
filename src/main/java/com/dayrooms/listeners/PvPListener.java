package com.dayrooms.listeners;

import com.dayrooms.managers.BarrierManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.model.Room;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

/**
 * Detecta cuando un jugador muere dentro de una room: el que lo mató
 * (o el otro jugador presente) es el ganador. Dispara el mensaje
 * global de victoria y arranca el cooldown de la barrera.
 */
public class PvPListener implements Listener {

    private final RoomManager roomManager;
    private final BarrierManager barrierManager;

    public PvPListener(RoomManager roomManager, BarrierManager barrierManager) {
        this.roomManager = roomManager;
        this.barrierManager = barrierManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player perdedor = event.getEntity();
        Room room = roomManager.encontrarRoomPorUbicacion(perdedor.getLocation());
        if (room == null) {
            return; // la muerte no fue dentro de ninguna room
        }

        Player ganador = perdedor.getKiller();
        if (ganador == null) {
            // Nadie lo mató directamente (caída, fuego, etc.) - no hay "ganador" de pvp
            return;
        }

        String mensaje = "&c&l[!] ¡Felicidades %ganador%! Ganaste el combate en la room %room% contra &e%perdedor%"
                .replace("%ganador%", ganador.getName())
                .replace("%room%", room.getName())
                .replace("%perdedor%", perdedor.getName())
                .replace('&', '§');

        Bukkit.broadcastMessage(mensaje);

        List<Player> jugadoresEnRoom = barrierManager.obtenerJugadoresEnRoom(room);
        barrierManager.iniciarCountdownPostVictoria(room, jugadoresEnRoom);
    }
  }
