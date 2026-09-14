package com.dayrooms.listeners;

import com.dayrooms.managers.BarrierManager;
import com.dayrooms.managers.ManualBarrierManager;
import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SoloRestanteManager;
import com.dayrooms.model.EffectData;
import com.dayrooms.model.Room;
import com.dayrooms.utils.BypassUtils;
import com.dayrooms.utils.EffectTypeMapper;
import org.bukkit.Sound;
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
    private final SoloRestanteManager soloRestanteManager;

    private final Map<UUID, String> roomActualPorJugador = new HashMap<>();

    public RoomEntryListener(RoomManager roomManager, BarrierManager barrierManager,
                              MessageManager messageManager, ManualBarrierManager manualBarrierManager,
                              SoloRestanteManager soloRestanteManager) {
        this.roomManager = roomManager;
        this.barrierManager = barrierManager;
        this.messageManager = messageManager;
        this.manualBarrierManager = manualBarrierManager;
        this.soloRestanteManager = soloRestanteManager;
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
        boolean esBypass = BypassUtils.tieneBypass(jugador);

        // Staff en vanish/staffmode: no aplica ninguna restriccion ni tracking.
        if (esBypass) {
            return;
        }

        Room roomActual = roomManager.encontrarRoomPorUbicacion(event.getTo());

        // Si se para sobre el bloque de la barrera (con room ya definida y teleport zone lista), lo expulsa.
        Room roomEnBarrera = roomManager.encontrarRoomPorUbicacionEnBarrera(event.getTo());
        if (roomEnBarrera != null && roomEnBarrera.isTeleportZoneDefinida()) {
            jugador.teleport(roomEnBarrera.getTeleportLocation());
            jugador.sendMessage(messageManager.get("no-en-barrera"));
            return;
        }

        if (roomActual != null && roomActual.isBarreraDefinida() && !barrierManager.barreraEstaCerrada(roomActual)) {
            List<Player> presentes = barrierManager.obtenerJugadoresEnRoomSinBypass(roomActual);
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
                    List<Player> restantes = barrierManager.obtenerJugadoresEnRoomSinBypass(roomAnterior);
                    int segundos = messageManager.getInt("barrera-rotura-manual-segundos", 10);
                    manualBarrierManager.onRompedorSalio(roomAnterior, restantes, segundos);
                }

                // Si al irse queda 1 solo jugador y la barrera sigue cerrada, arranca el countdown de apertura.
                List<Player> quedanEnRoom = barrierManager.obtenerJugadoresEnRoomSinBypass(roomAnterior);
                if (quedanEnRoom.size() == 1 && barrierManager.barreraEstaCerrada(roomAnterior)) {
                    int segundosSalida = messageManager.getInt("barrera-countdown-salida-segundos", 15);
                    soloRestanteManager.iniciarCountdown(roomAnterior, quedanEnRoom, segundosSalida);
                }

                String mensajeSalida = messageManager.get("salida").replace("%room%", roomAnterior.getName());
                jugador.sendMessage(mensajeSalida);
                jugador.playSound(jugador.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, 0.6f);
            }
        }

        roomActualPorJugador.put(uuid, nombreRoomActual);

        if (roomActual == null) {
            return;
        }

        List<Player> jugadoresDentro = barrierManager.obtenerJugadoresEnRoomSinBypass(roomActual);

        // Si con este jugador ya hay mas de 2 (limite estricto de 1v1), se lo expulsa.
        if (jugadoresDentro.size() > 2 && roomActual.isTeleportZoneDefinida()) {
            jugador.teleport(roomActual.getTeleportLocation());
            jugador.sendMessage(messageManager.get("room-llena"));
            roomActualPorJugador.remove(uuid);
            return;
        }

        if (manualBarrierManager.esRompedor(roomActual, uuid)) {
            manualBarrierManager.onRompedorVolvio(roomActual, jugadoresDentro, jugador);
        }

        // Si volvio a haber 2+ jugadores, cancela el countdown de "quedo 1 solo" si estaba activo.
        if (jugadoresDentro.size() >= 2) {
            soloRestanteManager.cancelar(roomActual.getName());
        }

        aplicarEfectos(jugador, roomActual);

        String mensajeEntrada = messageManager.get("entrada").replace("%room%", roomActual.getName());
        jugador.sendMessage(mensajeEntrada);
        jugador.playSound(jugador.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, 1.4f);
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
