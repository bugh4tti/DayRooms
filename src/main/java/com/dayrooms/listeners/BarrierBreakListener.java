package com.dayrooms.listeners;

import com.dayrooms.managers.ManualBarrierManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.model.Room;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BarrierBreakListener implements Listener {

    private final RoomManager roomManager;
    private final ManualBarrierManager manualBarrierManager;

    public BarrierBreakListener(RoomManager roomManager, ManualBarrierManager manualBarrierManager) {
        this.roomManager = roomManager;
        this.manualBarrierManager = manualBarrierManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.GLASS) {
            return;
        }

        Room room = roomManager.encontrarRoomPorUbicacionEnBarrera(event.getBlock().getLocation());
        if (room == null) {
            return;
        }

        manualBarrierManager.registrarRotura(room, event.getPlayer());
    }
}
