package com.dayrooms.listeners;

import com.dayrooms.managers.BarrierManager;
import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.StatsManager;
import com.dayrooms.model.Room;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class PvPListener implements Listener {

    private final RoomManager roomManager;
    private final BarrierManager barrierManager;
    private final MessageManager messageManager;
    private final StatsManager statsManager;

    public PvPListener(RoomManager roomManager, BarrierManager barrierManager,
                        MessageManager messageManager, StatsManager statsManager) {
        this.roomManager = roomManager;
        this.barrierManager = barrierManager;
        this.messageManager = messageManager;
        this.statsManager = statsManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player perdedor = event.getEntity();
        Room room = roomManager.encontrarRoomPorUbicacion(perdedor.getLocation());
        if (room == null) {
            return;
        }

        if (room.isKeepInventory()) {
            event.setKeepInventory(true);
            event.getDrops().clear();
            event.setDroppedExp(0);
        }

        Player ganador = perdedor.getKiller();
        if (ganador == null) {
            return;
        }

        statsManager.sumarVictoria(ganador.getUniqueId(), ganador.getName());

        String mensaje = messageManager.get("victoria")
                .replace("%ganador%", ganador.getName())
                .replace("%room%", room.getName())
                .replace("%perdedor%", perdedor.getName());

        Bukkit.broadcastMessage(mensaje);

        ganador.sendTitle("§a¡Ganaste!", "§7Venciste a §f" + perdedor.getName(), 10, 60, 10);

        List<Player> jugadoresEnRoom = barrierManager.obtenerJugadoresEnRoom(room);
        barrierManager.iniciarCountdownPostVictoria(room, jugadoresEnRoom);
    }
}
