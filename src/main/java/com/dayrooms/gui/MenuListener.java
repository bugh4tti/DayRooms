package com.dayrooms.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

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
            }
            case MainMenu.SLOT_EFFECTS -> {
                jugador.closeInventory();
                jugador.sendMessage("§7Abriendo menú de efectos...");
            }
            case MainMenu.SLOT_WAND -> {
                jugador.closeInventory();
                jugador.sendMessage("§6§l⛏ §7Recibiste la wand de selección de room.");
            }
            case MainMenu.SLOT_BARRIER -> {
                jugador.closeInventory();
                jugador.sendMessage("§f§l▦ §7Recibiste la herramienta de barrier.");
            }
            case MainMenu.SLOT_RELOAD -> {
                jugador.closeInventory();
                jugador.sendMessage("§c§l⟲ §7Recargando configuración...");
            }
            default -> {
            }
        }
    }
                  }
