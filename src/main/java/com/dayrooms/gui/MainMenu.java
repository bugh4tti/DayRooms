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
    public static final int TAMANIO = 54;

    public static final int SLOT_CREAR_ROOM = 19;
    public static final int SLOT_MIS_ROOMS = 21;
    public static final int SLOT_EFFECTS = 23;
    public static final int SLOT_WAND = 25;
    public static final int SLOT_BARRIER = 29;
    public static final int SLOT_TELEPORT = 31;
    public static final int SLOT_RELOAD = 33;

    public static Inventory construir(Player jugador, Collection<Room> misRooms) {
        Inventory inv = Bukkit.createInventory(null, TAMANIO, TITULO);

        rellenarBordes(inv);

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

        return inv;
    }

    private static void rellenarBordes(Inventory inv) {
        ItemStack relleno = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).nombre(" ").build();

        // Fila superior e inferior completas
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, relleno);
            inv.setItem(45 + i, relleno);
        }

        // Columnas laterales (izquierda y derecha) en las filas del medio
        for (int fila = 1; fila < 5; fila++) {
            inv.setItem(fila * 9, relleno);
            inv.setItem(fila * 9 + 8, relleno);
        }
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
