package com.dayrooms.gui;

import com.dayrooms.model.Room;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoomListMenu {

    public static final String TITULO = "DayRooms - Mis Rooms";
    public static final int TAMANIO = 27;
    public static final int SLOT_VOLVER = 22;

    private static final List<String> nombresPorSlot = new ArrayList<>();

    public static Inventory construir(Collection<Room> misRooms) {
        Inventory inv = Bukkit.createInventory(null, TAMANIO, TITULO);
        nombresPorSlot.clear();

        int slot = 0;
        for (Room room : misRooms) {
            if (slot >= 18) break;

            inv.setItem(slot, new ItemBuilder(Material.ENDER_CHEST)
                    .nombre(room.getName())
                    .lore(
                            "Estado: " + (room.isCompleta() ? "Lista" : "Incompleta"),
                            "",
                            "Click para editar esta room"
                    ).build());

            while (nombresPorSlot.size() <= slot) {
                nombresPorSlot.add(null);
            }
            nombresPorSlot.set(slot, room.getName());
            slot++;
        }

        inv.setItem(SLOT_VOLVER, new ItemBuilder(Material.ARROW)
                .nombre("« Volver al menu principal")
                .build());

        ItemStack relleno = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).nombre(" ").build();
        for (int i : new int[]{18, 19, 20, 21, 23, 24, 25, 26}) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, relleno);
            }
        }

        return inv;
    }

    public static String nombreEnSlot(int slot) {
        if (slot < 0 || slot >= nombresPorSlot.size()) {
            return null;
        }
        return nombresPorSlot.get(slot);
    }
             }
