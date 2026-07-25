package com.dayrooms.listeners;

import com.dayrooms.gui.MainMenu;
import com.dayrooms.gui.RoomListMenu;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RoomListListener implements Listener {

    private final RoomManager roomManager;
    private final SelectionManager selectionManager;

    public RoomListListener(RoomManager roomManager, SelectionManager selectionManager) {
        this.roomManager = roomManager;
        this.selectionManager = selectionManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!RoomListMenu.TITULO.equals(event.getView().getTitle())) {
            return;
        }
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player jugador)) {
            return;
        }

        int slot = event.getRawSlot();

        if (slot == RoomListMenu.SLOT_VOLVER) {
            jugador.closeInventory();
            jugador.openInventory(MainMenu.construir(jugador, roomManager.listarDeJugador(jugador.getUniqueId())));
            return;
        }

        String nombreRoom = RoomListMenu.nombreEnSlot(slot);
        if (nombreRoom == null) {
            return;
        }

        selectionManager.setRoomEnEdicion(jugador.getUniqueId(), nombreRoom);
        jugador.closeInventory();
        jugador.sendMessage("Ahora estas editando la room " + nombreRoom);
    }
}
