package com.dayrooms.gui;

import com.dayrooms.model.Room;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EffectsMenu {

    public static final String TITULO = "DayRooms - Efectos";
    public static final int TAMANIO = 27;
    public static final int SLOT_VOLVER = 22;

    public static final String[][] EFECTOS = {
            {"FUERZA", "Fuerza", "IRON_SWORD"},
            {"VELOCIDAD", "Velocidad", "SUGAR"},
            {"INVISIBILIDAD", "Invisibilidad", "GLASS"},
            {"VISION_NOCTURNA", "Vision Nocturna", "ENDER_EYE"},
            {"REGENERACION", "Regeneracion", "GHAST_TEAR"},
            {"RESISTENCIA", "Resistencia", "SHIELD"},
            {"SALTO", "Salto", "RABBIT_FOOT"},
            {"PRISA", "Prisa", "GOLDEN_PICKAXE"}
    };

    public static int slotDe(int indice) {
        return 10 + indice;
    }

    public static Inventory construir(Room room) {
        Inventory inv = Bukkit.createInventory(null, TAMANIO, TITULO);

        for (int i = 0; i < EFECTOS.length; i++) {
            String key = EFECTOS[i][0];
            String nombre = EFECTOS[i][1];
            Material material = Material.valueOf(EFECTOS[i][2]);

            var datos = room.obtenerOCrearEfecto(key);

            inv.setItem(slotDe(i), new ItemBuilder(material)
                    .nombre(nombre)
                    .lore(
                            "Nivel actual: " + (datos.estaActivo() ? String.valueOf(datos.getNivel()) : "Desactivado"),
                            "Duracion: " + (datos.getDuracionSegundos() > 0 ? datos.getDuracionSegundos() + "s" : "Sin definir"),
                            "",
                            "Click izq: subir nivel (1-10)",
                            "Shift+click: desactivar",
                            "Click der: definir duracion (chat)"
                    ).build());
        }

        inv.setItem(SLOT_VOLVER, new ItemBuilder(Material.ARROW)
                .nombre("Volver al menu principal")
                .build());

        ItemStack relleno = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).nombre(" ").build();
        for (int slot : new int[]{0,1,2,3,4,5,6,7,8, 9,18,19,20,21,23,24,25,26}) {
            inv.setItem(slot, relleno);
        }

        return inv;
    }
          }
