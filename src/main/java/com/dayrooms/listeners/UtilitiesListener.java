package com.dayrooms.listeners;

import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.model.Room;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Set;

/**
 * Bloquea el uso de "utilidades" (lana y telaraña, típico de bridging
 * en BoxPvP) dentro de una room cuando esa room tiene utilities=false.
 */
public class UtilitiesListener implements Listener {

    private static final Set<Material> MATERIALES_UTILIDAD = Set.of(
            Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
            Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,
            Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL,
            Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
            Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL,
            Material.BLACK_WOOL, Material.COBWEB
    );

    private final RoomManager roomManager;
    private final MessageManager messageManager;

    public UtilitiesListener(RoomManager roomManager, MessageManager messageManager) {
        this.roomManager = roomManager;
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!MATERIALES_UTILIDAD.contains(event.getBlock().getType())) {
            return;
        }

        Room room = roomManager.encontrarRoomPorUbicacion(event.getBlock().getLocation());
        if (room == null) {
            return; // no está dentro de ninguna room, no aplica la restricción
        }

        if (!room.isUtilidadesHabilitadas()) {
            event.setCancelled(true);
            Player jugador = event.getPlayer();
            jugador.sendMessage(messageManager.get("utilities-bloqueado"));
        }
    }
}
