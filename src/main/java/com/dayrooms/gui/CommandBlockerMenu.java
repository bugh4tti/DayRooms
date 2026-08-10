package com.dayrooms.gui;

import com.dayrooms.managers.MessageManager;
import com.dayrooms.model.Room;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandBlockerMenu {

    public static final String TITULO = "DayRooms - Bloqueo de comandos";
    public static final int TAMANIO = 54;
    public static final int SLOT_VOLVER = 49;

    public static Inventory construir(Room room, MessageManager messageManager) {
        Inventory inv = Bukkit.createInventory(null, TAMANIO, TITULO);
        rellenarBordes(inv);

        List<String> comandos = messageManager.getStringList("comandos-importantes");

        int indice = 0;
        outer:
        for (int fila = 1; fila <= 4; fila++) {
            for (int col = 1; col <= 7; col++) {
                if (indice >= comandos.size()) break outer;
                String cmd = comandos.get(indice).toLowerCase();
                boolean bloqueado = room.isComandoBloqueado(cmd);

                inv.setItem(fila * 9 + col, new ItemBuilder(bloqueado ? Material.RED_WOOL : Material.LIME_WOOL)
                        .nombre("/" + cmd)
                        .lore(
                                "Estado: " + (bloqueado ? "Bloqueado" : "Permitido"),
                                "",
                                "Click para " + (bloqueado ? "permitir" : "bloquear")
                        ).build());

                indice++;
            }
        }

        inv.setItem(SLOT_VOLVER, new ItemBuilder(Material.ARROW).nombre("Volver al menu principal").build());
        return inv;
    }

    private static void rellenarBordes(Inventory inv) {
        ItemStack relleno = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).nombre(" ").build();
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, relleno);
            inv.setItem(45 + i, relleno);
        }
        for (int fila = 1; fila < 5; fila++) {
            inv.setItem(fila * 9, relleno);
            inv.setItem(fila * 9 + 8, relleno);
        }
    }
                  }
