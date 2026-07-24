package com.dayrooms.gui;

import com.dayrooms.model.Room;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class MainMenu {

    public static final String TITULO = "§b§lDayRooms §7» Menú principal";
    public static final int TAMANIO = 27;

    public static final int SLOT_CREAR_ROOM = 10;
    public static final int SLOT_MIS_ROOMS = 12;
    public static final int SLOT_EFFECTS = 14;
    public static final int SLOT_WAND = 16;
    public static final int SLOT_BARRIER = 20;
    public static final int SLOT_RELOAD = 24;

    public static Inventory construir(Player jugador, Collection<Room> misRooms) {
        Inventory inv = Bukkit.createInventory(null, TAMANIO, TITULO);

        inv.setItem(SLOT_CREAR_ROOM, new ItemBuilder(Material.EMERALD_BLOCK)
                .nombre("§a§l+ Crear Room")
                .lore(
                        "§7Crea una nueva room y entra",
                        "§7automáticamente en modo edición.",
                        "",
                        "§eClick para crear"
                ).build());

        inv.setItem(SLOT_MIS_ROOMS, itemMisRooms(misRooms.size()));

        inv.setItem(SLOT_EFFECTS, new ItemBuilder(Material.POTION)
                .nombre("§d§l✦ Efectos")
                .lore(
                        "§7Fuerza, velocidad, invisibilidad,",
                        "§7visión nocturna, regeneración y más.",
                        "§7Configurá nivel (1-10) y duración.",
                        "",
                        "§eClick para abrir"
                ).build());

        inv.setItem(SLOT_WAND, new ItemBuilder(Material.GOLDEN_AXE)
                .nombre("§6§l⛏ Wand de selección")
                .lore(
                        "§7Te da el hacha para marcar",
                        "§7las 2 esquinas de la room.",
                        "§7Click der. = lado 1  |  Click izq. = lado 2",
                        "",
                        "§eClick para recibirla"
                ).build());

        inv.setItem(SLOT_BARRIER, new ItemBuilder(Material.IRON_HOE)
                .nombre("§f§l▦ Barrier")
                .lore(
                        "§7Te da la azada para marcar",
                        "§7dónde se cierra con cristal.",
                        "§7Click der. = lado 1  |  Click izq. = lado 2",
                        "",
                        "§eClick para recibirla"
                ).build());

        inv.setItem(SLOT_RELOAD, new ItemBuilder(Material.REDSTONE)
                .nombre("§c§l⟲ Reload")
                .lore(
                        "§7Reinicia la configuración",
                        "§7del plugin sin bajar el server.",
                        "",
                        "§eClick para reiniciar"
                ).build());

        ItemStack relleno = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).nombre(" ").build();
        for (int slot : new int[]{0,1,2,3,4,5,6,7,8, 9,17,18,26, 19,21,22,23,25}) {
            inv.setItem(slot, relleno);
        }

        return inv;
    }

    private static ItemStack itemMisRooms(int cantidad) {
        return new ItemBuilder(Material.ENDER_CHEST)
                .nombre("§b§l⌂ Mis Rooms §7(" + cantidad + ")")
                .lore(
                        "§7Ver y gestionar las rooms",
                        "§7de las que sos owner o invitado.",
                        "",
                        "§eClick para ver el listado"
                ).build();
    }
}
