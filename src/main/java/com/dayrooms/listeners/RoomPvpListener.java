package com.dayrooms.listeners;

import com.dayrooms.managers.RoomManager;
import com.dayrooms.model.Room;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class RoomPvpListener implements Listener {

    private final RoomManager roomManager;

    public RoomPvpListener(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player victima)) {
            return;
        }

        Room room = roomManager.encontrarRoomPorUbicacion(victima.getLocation());
        if (room == null) {
            return;
        }

        if (!room.isPvpHabilitado()) {
            event.setCancelled(true);
        }
    }
}
