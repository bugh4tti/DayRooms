package com.dayrooms.gui;

import com.dayrooms.model.Room;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class MainMenu {

    public static final String TITULO = "DayRooms - Menu principal";
    public static final int TAMANIO = 27;

    public static final int SLOT_CREAR_ROOM = 10;
    public static final int SLOT_MIS_ROOMS = 12;
    public static final int SLOT_EFFECTS = 14;
    public static final int SLOT_WAND = 16;
    public static final int SLOT_BARRIER = 19;
    public static final int SLOT_TELEPORT = 21;
    public static final int SLOT_RELOAD = 23;

    public static Inventory construir(Player jugador, Collection<Room> misRooms) {
        Inventory inv = Bukkit.createInventory(null, TAMANIO, TITULO);

        inv.setItem(SLOT_CREAR_ROOM, new ItemBuilder(Material.EMERALD_BLOCK)
                .nombre("+ Crear Room")
                .lore(
                        "Crea una nueva room y entra",
                        "automaticamente en modo edicion.",
                        "",
                        "Click para crear"
                ).build());

        inv.setItem(SLOT_MIS_ROOMS, itemMisRooms(misRooms.size()));

        inv.setItem(SLOT_EFFECTS, new ItemBuilder(Material.POTION)
                .nombre("Efectos")
                .lore(
                        "Fuerza, velocidad, invisibilidad,",
                        "vision nocturna, regeneracion y mas.",
                        "Configura nivel (1-10) y duracion.",
                        "",
                        "Click para abrir"
                ).build());

        inv.setItem(SLOT_WAND, new ItemBuilder(Material.GOLDEN_AXE)
                .nombre("Wand de seleccion")
                .lore(
                        "Te da el hacha para marcar",
                        "las 2 esquinas de la room.",
                        "Click der. = lado 1  |  Click izq. = lado 2",
                        "",
                        "Click para recibirla"
                ).build());

        inv.setItem(SLOT_BARRIER, new ItemBuilder(Material.IRON_HOE)
                .nombre("Barrier")
                .lore(
                        "Te da la azada para marcar",
                        "donde se cierra con cristal.",
                        "Click der. = lado 1  |  Click izq. = lado 2",
                        "",
                        "Click para recibirla"
                ).build());

        inv.setItem(SLOT_TELEPORT, new ItemBuilder(Material.DIAMOND_PICKAXE)
                .nombre("Teleport zone")
                .lore(
                        "Te da el pico para marcar",
                        "a donde se manda a quien intenta",
                        "entrar a una room ocupada.",
                        "",
                        "Click para recibirlo"
                ).build());

        inv.setItem(SLOT_RELOAD, new ItemBuilder(Material.REDSTONE)
                .nombre("Reload")
                .lore(
                        "Reinicia la configuracion",
                        "del plugin sin bajar el server.",
                        "",
                        "Click para reiniciar"
                ).build());

        ItemStack relleno = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).nombre(" ").build();
        for (int slot : new int[]{0,1,2,3,4,5,6,7,8, 9,17,18,20,22,24,25,26}) {
            inv.setItem(slot, relleno);
        }

        return inv;
    }

    private static ItemStack itemMisRooms(int cantidad) {
        return new ItemBuilder(Material.ENDER_CHEST)
                .nombre("Mis Rooms (" + cantidad + ")")
                .lore(
                        "Ver y gestionar las rooms",
                        "de las que sos owner o invitado.",
                        "",
                        "Click para ver el listado"
                ).build();
    }
                    }
