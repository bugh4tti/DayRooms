package com.dayrooms.listeners;

import com.dayrooms.gui.CommandBlockerMenu;
import com.dayrooms.gui.MainMenu;
import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
import com.dayrooms.model.Room;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandBlockerListener implements Listener {

    private final RoomManager roomManager;
    private final SelectionManager selectionManager;
    private final MessageManager messageManager;

    public CommandBlockerListener(RoomManager roomManager, SelectionManager selectionManager, MessageManager messageManager) {
        this.roomManager = roomManager;
        this.selectionManager = selectionManager;
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!CommandBlockerMenu.TITULO.equals(event.getView().getTitle())) {
            return;
        }
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player jugador)) {
            return;
        }

        if (event.getRawSlot() == CommandBlockerMenu.SLOT_VOLVER) {
            jugador.closeInventory();
            jugador.openInventory(MainMenu.construir(jugador, roomManager.listarDeJugador(jugador.getUniqueId())));
            return;
        }

        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        String nombre = meta.getDisplayName();
        if (nombre == null || !nombre.startsWith("/")) {
            return;
        }
        String cmd = nombre.substring(1).toLowerCase();

        String nombreRoom = selectionManager.getRoomEnEdicion(jugador.getUniqueId());
        if (nombreRoom == null) {
            return;
        }
        Room room = roomManager.obtener(nombreRoom);
        if (room == null) {
            return;
        }

        if (room.isComandoBloqueado(cmd)) {
            room.desbloquearComando(cmd);
        } else {
            room.bloquearComando(cmd);
        }

        jugador.openInventory(CommandBlockerMenu.construir(room, messageManager));
    }
            }
