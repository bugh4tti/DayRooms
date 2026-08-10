package com.dayrooms.gui;

import com.dayrooms.model.Room;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockTypeMenu {

    public static final String TITULO = "DayRooms - Bloque de barrera";
    public static final int TAMANIO = 27;
    public static final int SLOT_VOLVER = 22;

    public static final Material[] BLOQUES = {
            Material.GLASS,
            Material.IRON_BARS,
            Material.OBSIDIAN,
            Material.BEDROCK,
            Material.RED_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.BARRIER
    };

    public static Inventory construir(Room room) {
        Inventory inv = Bukkit.createInventory(null, TAMANIO, TITULO);

        int slot = 10;
        for (Material material : BLOQUES) {
            inv.setItem(slot, new ItemBuilder(material)
                    .nombre(material.name())
                    .lore(room.getBarreraMaterial() == material ? "Actual" : "Click para usar este bloque")
                    .build());
            slot++;
        }

        inv.setItem(SLOT_VOLVER, new ItemBuilder(Material.ARROW).nombre("Volver al menu principal").build());

        ItemStack relleno = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).nombre(" ").build();
        for (int i : new int[]{0,1,2,3,4,5,6,7,8, 9,17,18,19,20,21,23,24,25,26}) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, relleno);
            }
        }

        return inv;
    }
}
