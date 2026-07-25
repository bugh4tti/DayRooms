package com.dayrooms.listeners;

import com.dayrooms.managers.BarrierManager;
import com.dayrooms.managers.ManualBarrierManager;
import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.model.EffectData;
import com.dayrooms.model.Room;
import com.dayrooms.utils.EffectTypeMapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class RoomEntryListener implements Listener {

    private final RoomManager roomManager;
    private final BarrierManager barrierManager;
    private final MessageManager messageManager;
    private final ManualBarrierManager manualBarrierManager;

    private final Map<UUID, String> roomActualPorJugador = new HashMap<>();

    public RoomEntryListener(RoomManager roomManager, BarrierManager barrierManager,
                              MessageManager messageManager, ManualBarrierManager manualBarrierManager) {
        this.roomManager = roomManager;
        this.barrierManager = barrierManager;
        this.messageManager = messageManager;
        this.manualBarrierManager = manualBarrierManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Player jugador = event.getPlayer();
        UUID uuid = jugador.getUniqueId();

        Room roomActual = roomManager.encontrarRoomPorUbicacion(event.getTo());

        if (roomActual != null && roomActual.isBarreraDefinida() && !barrierManager.barreraEstaCerrada(roomActual)) {
            List<Player> presentes = barrierManager.obtenerJugadoresEnRoom(roomActual);
            if (presentes.size() >= 2) {
                barrierManager.colocarBarrera(roomActual);
            }
        }

        String nombreRoomActual = roomActual != null ? roomActual.getName() : null;
        String nombreRoomAnterior = roomActualPorJugador.get(uuid);

        if (Objects.equals(nombreRoomAnterior, nombreRoomActual)) {
            return;
        }

        if (nombreRoomAnterior != null) {
            Room roomAnterior = roomManager.obtener(nombreRoomAnterior);
            if (roomAnterior != null) {
                quitarEfectos(jugador, roomAnterior);

                if (manualBarrierManager.esRompedor(roomAnterior, uuid)) {
                    manualBarrierManager.onRompedorSalio(roomAnterior);
                }

                String mensajeSalida = messageManager.get("salida").replace("%room%", roomAnterior.getName());
                jugador.sendMessage(mensajeSalida);
            }
        }

        roomActualPorJugador.put(uuid, nombreRoomActual);

        if (roomActual == null) {
            return;
        }

        List<Player> jugadoresDentro = barrierManager.obtenerJugadoresEnRoom(roomActual);

        if (manualBarrierManager.esRompedor(roomActual, uuid)) {
            manualBarrierManager.onRompedorVolvio(roomActual, jugadoresDentro, jugador);
        }

        boolean roomOcupadaPeleando = jugadoresDentro.size() > 2;

        if (roomOcupadaPeleando && roomActual.isTeleportZoneDefinida()) {
            jugador.teleport(roomActual.getTeleportLocation());
            roomActualPorJugador.remove(uuid);
            return;
        }

        aplicarEfectos(jugador, roomActual);

        String mensajeEntrada = messageManager.get("entrada").replace("%room%", roomActual.getName());
        jugador.sendMessage(mensajeEntrada);
    }

    private void aplicarEfectos(Player jugador, Room room) {
        for (Map.Entry<String, EffectData> entrada : room.getEfectos().entrySet()) {
            EffectData datos = entrada.getValue();
            if (!datos.estaActivo() || datos.getDuracionSegundos() <= 0) {
                continue;
            }
            PotionEffectType tipo = EffectTypeMapper.map(entrada.getKey());
            if (tipo == null) {
                continue;
            }
            int amplificador = Math.max(0, datos.getNivel() - 1);
            int duracionTicks = (int) (datos.getDuracionSegundos() * 20);
            jugador.addPotionEffect(new PotionEffect(tipo, duracionTicks, amplificador));
        }
    }

    private void quitarEfectos(Player jugador, Room room) {
        for (String key : room.getEfectos().keySet()) {
            PotionEffectType tipo = EffectTypeMapper.map(key);
            if (tipo != null) {
                jugador.removePotionEffect(tipo);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        roomActualPorJugador.remove(event.getPlayer().getUniqueId());
    }
                        }
