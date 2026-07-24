package com.dayrooms.gui;

import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
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
                jugador.sendMessage("§7Escribí: §e/dayrooms create <nombre>");
            }
            case MainMenu.SLOT_MIS_ROOMS -> {
                jugador.closeInventory();
                jugador.sendMessage("§7Abriendo listado de tus rooms...");
                // TODO: sub-GUI con roomManager.listarDeJugador(jugador.getUniqueId())
            }
            case MainMenu.SLOT_EFFECTS -> {
                jugador.closeInventory();
                jugador.sendMessage("§7Abriendo menú de efectos...");
                // TODO: EffectsMenu
            }
            case MainMenu.SLOT_WAND -> {
                jugador.closeInventory();
                if (!validarRoomEnEdicion(jugador)) return;
                jugador.getInventory().addItem(item(Material.GOLDEN_AXE, "§6§l⛏ Wand de Room"));
                selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.ESQUINAS);
                jugador.sendMessage("§6§l⛏ §7Recibiste la wand. Click der. = lado 1, click izq. = lado 2.");
            }
            case MainMenu.SLOT_BARRIER -> {
                jugador.closeInventory();
                if (!validarRoomEnEdicion(jugador)) return;
                jugador.getInventory().addItem(item(Material.IRON_HOE, "§f§l▦ Barrier Tool"));
                selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.BARRERA);
                jugador.sendMessage("§f§l▦ §7Recibiste la herramienta de barrera.");
            }
            case MainMenu.SLOT_RELOAD -> {
                jugador.closeInventory();
                jugador.sendMessage("§c§l⟲ §7Recargando configuración...");
                // TODO: reload de config.yml y rooms.yml
            }
            default -> {
            }
        }
    }

    private boolean validarRoomEnEdicion(Player jugador) {
        if (selectionManager.getRoomEnEdicion(jugador.getUniqueId()) == null) {
            jugador.sendMessage("§cPrimero creá o editá una room: §e/dayrooms create <nombre>");
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
