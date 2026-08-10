package com.dayrooms.listeners;

import com.dayrooms.gui.BlockTypeMenu;
import com.dayrooms.gui.MainMenu;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
import com.dayrooms.model.Room;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BlockTypeListener implements Listener {

    private final RoomManager roomManager;
    private final SelectionManager selectionManager;

    public BlockTypeListener(RoomManager roomManager, SelectionManager selectionManager) {
        this.roomManager = roomManager;
        this.selectionManager = selectionManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!BlockTypeMenu.TITULO.equals(event.getView().getTitle())) {
            return;
        }
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player jugador)) {
            return;
        }

        if (event.getRawSlot() == BlockTypeMenu.SLOT_VOLVER) {
            jugador.closeInventory();
            jugador.openInventory(MainMenu.construir(jugador, roomManager.listarDeJugador(jugador.getUniqueId())));
            return;
        }

        int index = event.getRawSlot() - 10;
        if (index < 0 || index >= BlockTypeMenu.BLOQUES.length) {
            return;
        }

        String nombreRoom = selectionManager.getRoomEnEdicion(jugador.getUniqueId());
        if (nombreRoom == null) {
            return;
        }
        Room room = roomManager.obtener(nombreRoom);
        if (room == null) {
            return;
        }

        Material elegido = BlockTypeMenu.BLOQUES[index];
        room.setBarreraMaterial(elegido);
        jugador.sendMessage("Bloque de la barrera de " + room.getName() + " actualizado a " + elegido.name());
        jugador.openInventory(BlockTypeMenu.construir(room));
    }
}
