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
        String mensaje = event.getMessage();

        if (mensaje.toLowerCase().startsWith("/dayrooms") || mensaje.toLowerCase().startsWith("/dr ")
                || mensaje.equalsIgnoreCase("/dr")) {
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
            return;
        }

        String comando = extraerComando(mensaje);
        if (room.isComandoBloqueado(comando)) {
            event.setCancelled(true);
            jugador.sendMessage(messageManager.get("comandos-bloqueados"));
        }
    }

    private String extraerComando(String mensaje) {
        String sinBarra = mensaje.substring(1);
        int espacio = sinBarra.indexOf(' ');
        String base = espacio == -1 ? sinBarra : sinBarra.substring(0, espacio);
        return base.toLowerCase();
    }
}
