package com.dayrooms.listeners;

import com.dayrooms.managers.BarrierManager;
import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.model.Room;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Detecta cuándo un jugador entra a una room caminando:
 * - Si la room ya tiene 2+ jugadores peleando, lo manda a la
 *   zona de teleport (si está definida) en vez de dejarlo entrar.
 * - Si no, le manda el mensaje de bienvenida personalizado.
 */
public class RoomEntryListener implements Listener {

    private final RoomManager roomManager;
    private final BarrierManager barrierManager;
    private final MessageManager messageManager;

    // Jugador -> nombre de la room en la que está parado ahora mismo (o null si no está en ninguna)
    private final Map<UUID, String> roomActualPorJugador = new HashMap<>();

    public RoomEntryListener(RoomManager roomManager, BarrierManager barrierManager, MessageManager messageManager) {
        this.roomManager = roomManager;
        this.barrierManager = barrierManager;
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // Solo evaluamos si cambió de bloque (evita recalcular en cada micro-movimiento)
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Player jugador = event.getPlayer();
        UUID uuid = jugador.getUniqueId();

        Room roomActual = roomManager.encontrarRoomPorUbicacion(event.getTo());
        String nombreRoomActual = roomActual != null ? roomActual.getName() : null;
        String nombreRoomAnterior = roomActualPorJugador.get(uuid);

        if (Objects.equals(nombreRoomAnterior, nombreRoomActual)) {
            return; // sigue en la misma room (o sigue afuera de todas), nada que hacer
        }

        roomActualPorJugador.put(uuid, nombreRoomActual);

        if (roomActual == null) {
            return; // se fue de una room, no hace falta avisar nada
        }

        // Recién entró a roomActual
        List<Player> jugadoresDentro = barrierManager.obtenerJugadoresEnRoom(roomActual);

        boolean roomOcupadaPeleando = jugadoresDentro.size() > 2; // ya había 2 (o más) antes de que él entrara

        if (roomOcupadaPeleando && roomActual.isTeleportZoneDefinida()) {
            jugador.teleport(roomActual.getTeleportLocation());
            roomActualPorJugador.remove(uuid);
            return;
        }

        String mensaje = messageManager.get("entrada").replace("%room%", roomActual.getName());
        jugador.sendMessage(mensaje);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        roomActualPorJugador.remove(event.getPlayer().getUniqueId());
    }
  }
