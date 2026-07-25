package com.dayrooms.listeners;

import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.model.Room;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlockListener implements Listener {

    private final RoomManager roomManager;
    private final MessageManager messageManager;

    public CommandBlockListener(RoomManager roomManager, MessageManager messageManager) {
        this.roomManager = roomManager;
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String mensaje = event.getMessage().toLowerCase();

        // El comando del propio plugin siempre se permite (para poder usar /dayrooms reload, etc.)
        if (mensaje.startsWith("/dayrooms")) {
            return;
        }

        Player jugador = event.getPlayer();
        Room room = roomManager.encontrarRoomPorUbicacion(jugador.getLocation());
        if (room == null) {
            return;
        }

        if (!room.isComandosHabilitados()) {
            event.setCancelled(true);
            jugador.sendMessage(messageManager.get("comandos-bloqueados"));
        }
    }
}
