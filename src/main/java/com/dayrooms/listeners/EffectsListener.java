package com.dayrooms.listeners;

import com.dayrooms.gui.EffectsMenu;
import com.dayrooms.gui.MainMenu;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
import com.dayrooms.model.EffectData;
import com.dayrooms.model.Room;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EffectsListener implements Listener {

    private final RoomManager roomManager;
    private final SelectionManager selectionManager;

    // jugador que está esperando escribir la duración en el chat -> key del efecto
    private final Map<UUID, String> esperandoDuracion = new HashMap<>();

    public EffectsListener(RoomManager roomManager, SelectionManager selectionManager) {
        this.roomManager = roomManager;
        this.selectionManager = selectionManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!EffectsMenu.TITULO.equals(event.getView().getTitle())) {
            return;
        }
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player jugador)) {
            return;
        }

        int slot = event.getRawSlot();

        if (slot == EffectsMenu.SLOT_VOLVER) {
            jugador.closeInventory();
            jugador.openInventory(MainMenu.construir(jugador, roomManager.listarDeJugador(jugador.getUniqueId())));
            return;
        }

        String nombreRoom = selectionManager.getRoomEnEdicion(jugador.getUniqueId());
        if (nombreRoom == null) {
            jugador.sendMessage("§cNo estás editando ninguna room.");
            jugador.closeInventory();
            return;
        }
        Room room = roomManager.obtener(nombreRoom);
        if (room == null) {
            jugador.sendMessage("§cEsa room ya no existe.");
            jugador.closeInventory();
            return;
        }

        for (int i = 0; i < EffectsMenu.EFECTOS.length; i++) {
            if (slot != EffectsMenu.slotDe(i)) continue;

            String key = EffectsMenu.EFECTOS[i][0];
            EffectData datos = room.obtenerOCrearEfecto(key);

            if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                datos.setNivel(0);
                jugador.sendMessage("§c" + EffectsMenu.EFECTOS[i][1] + " §7desactivado.");
            } else if (event.getClick() == ClickType.RIGHT) {
                esperandoDuracion.put(jugador.getUniqueId(), key);
                jugador.closeInventory();
                jugador.sendMessage("§eEscribí en el chat la duración en segundos para " + EffectsMenu.EFECTOS[i][1] + "§e (ej: 60
