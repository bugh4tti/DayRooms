package com.dayrooms.gui;

import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
import com.dayrooms.model.Room;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuListener implements Listener {

    private final RoomManager roomManager;
    private final SelectionManager selectionManager;

    public MenuListener(RoomManager roomManager, SelectionManager selectionManager) {
        this.roomManager = roomManager;
        this.selectionManager = selectionManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!MainMenu.TITULO.equals(event.getView().getTitle())) {
            return;
        }

        event.setCancelled(true);

        if (event.getRawSlot() < 0 || event.getRawSlot() >= MainMenu.TAMANIO) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player jugador)) {
            return;
        }

        switch (event.getRawSlot()) {
            case MainMenu.SLOT_CREAR_ROOM -> {
                jugador.closeInventory();
                jugador.sendMessage("Escribi: /dayrooms create <nombre>");
            }
            case MainMenu.SLOT_MIS_ROOMS -> {
                jugador.closeInventory();
                jugador.openInventory(RoomListMenu.construir(roomManager.listarDeJugador(jugador.getUniqueId())));
            }
            case MainMenu.SLOT_EFFECTS -> {
                jugador.closeInventory();
                String nombreRoom = selectionManager.getRoomEnEdicion(jugador.getUniqueId());
                if (nombreRoom == null) {
                    jugador.sendMessage("Primero crea o edita una room: /dayrooms create <nombre>");
                    return;
                }
                Room room = roomManager.obtener(nombreRoom);
                if (room == null) {
                    jugador.sendMessage("Esa room ya no existe.");
                    return;
                }
                jugador.openInventory(EffectsMenu.construir(room));
            }
            case MainMenu.SLOT_WAND -> {
                jugador.closeInventory();
                if (!validarRoomEnEdicion(jugador)) return;
                jugador.getInventory().addItem(item(Material.GOLDEN_AXE, "Wand de Room"));
                selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.ESQUINAS);
                jugador.sendMessage("Recibiste la wand. Click der. = lado 1, click izq. = lado 2.");
            }
            case MainMenu.SLOT_BARRIER -> {
                jugador.closeInventory();
                if (!validarRoomEnEdicion(jugador)) return;
                jugador.getInventory().addItem(item(Material.IRON_HOE, "Barrier Tool"));
                selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.BARRERA);
                jugador.sendMessage("Recibiste la herramienta de barrera.");
            }
            case MainMenu.SLOT_TELEPORT -> {
                jugador.closeInventory();
                if (!validarRoomEnEdicion(jugador)) return;
                jugador.getInventory().addItem(item(Material.DIAMOND_PICKAXE, "Teleport Tool"));
                selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.TELEPORT);
                jugador.sendMessage("Recibiste el pico. Click der. o izq. para marcar la zona de teleport.");
            }
            case MainMenu.SLOT_RELOAD -> {
                jugador.closeInventory();
                jugador.sendMessage("Usa /dayrooms reload para recargar la configuracion.");
            }
            default -> {
            }
        }
    }

    private boolean validarRoomEnEdicion(Player jugador) {
        if (selectionManager.getRoomEnEdicion(jugador.getUniqueId()) == null) {
            jugador.sendMessage("Primero crea o edita una room: /dayrooms create <nombre>");
            return false;
        }
        return true;
    }

    private ItemStack item(Material material, String nombre) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(nombre);
        item.setItemMeta(meta);
        return item;
    }
                        }
