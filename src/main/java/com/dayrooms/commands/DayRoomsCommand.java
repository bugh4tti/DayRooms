package com.dayrooms.commands;

import com.dayrooms.gui.MainMenu;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
import com.dayrooms.model.Room;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DayRoomsCommand implements CommandExecutor {

    private final RoomManager roomManager;
    private final SelectionManager selectionManager;

    public DayRoomsCommand(RoomManager roomManager, SelectionManager selectionManager) {
        this.roomManager = roomManager;
        this.selectionManager = selectionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player jugador)) {
            sender.sendMessage("Este comando es solo para jugadores.");
            return true;
        }

        if (args.length == 0) {
            jugador.openInventory(MainMenu.construir(jugador, roomManager.listarDeJugador(jugador.getUniqueId())));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> crear(jugador, args);
            case "editor" -> editar(jugador, args);
            case "wand" -> darWand(jugador);
            case "barrier" -> darBarrier(jugador);
            case "teleport" -> darTeleportTool(jugador);
            default -> jugador.sendMessage("§7Subcomando aún no implementado: §e" + args[0]);
        }

        return true;
    }

    private void crear(Player jugador, String[] args) {
        if (args.length < 2) {
            jugador.sendMessage("§cUso: /dayrooms create <nombre>");
            return;
        }
        String nombre = args[1];
        if (roomManager.existe(nombre)) {
            jugador.sendMessage("§cYa existe una room con ese nombre.");
            return;
        }
        roomManager.crear(nombre, jugador.getUniqueId());
        selectionManager.setRoomEnEdicion(jugador.getUniqueId(), nombre);
        jugador.sendMessage("§a✔ Room §f" + nombre + " §acreada. Ahora estás en modo edición.");
        jugador.sendMessage("§7Usá §e/dayrooms wand§7, §e/dayrooms barrier §7y §e/dayrooms teleport §7para configurarla.");
    }

    private void editar(Player jugador, String[] args) {
        if (args.length < 2) {
            jugador.sendMessage("§cUso: /dayrooms editor <nombre>");
            return;
        }
        String nombre = args[1];
        Room room = roomManager.obtener(nombre);
        if (room == null) {
            jugador.sendMessage("§cNo existe una room con ese nombre.");
            return;
        }
        selectionManager.setRoomEnEdicion(jugador.getUniqueId(), nombre);
        jugador.sendMessage("§a✔ Ahora estás editando la room §f" + nombre);
    }

    private void darWand(Player jugador) {
        if (!tieneRoomEnEdicion(jugador)) return;
        jugador.getInventory().addItem(crearHerramienta(Material.GOLDEN_AXE, "§6§l⛏ Wand de Room"));
        selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.ESQUINAS);
        jugador.sendMessage("§6§l⛏ §7Wand recibida. Click der. = lado 1, click izq. = lado 2.");
    }

    private void darBarrier(Player jugador) {
        if (!tieneRoomEnEdicion(jugador)) return;
        jugador.getInventory().addItem(crearHerramienta(Material.IRON_HOE, "§f§l▦ Barrier Tool"));
        selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.BARRERA);
        jugador.sendMessage("§f§l▦ §7Herramienta de barrera recibida. Click der. = lado 1, click izq. = lado 2.");
    }

    private void darTeleportTool(Player jugador) {
        if (!tieneRoomEnEdicion(jugador)) return;
        jugador.getInventory().addItem(crearHerramienta(Material.DIAMOND_PICKAXE, "§b§l➤ Teleport Tool"));
        selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.TELEPORT);
        jugador.sendMessage("§b§l➤ §7Pico recibido. Click der. o izq. para marcar la zona de teleport.");
    }

    private boolean tieneRoomEnEdicion(Player jugador) {
        if (selectionManager.getRoomEnEdicion(jugador.getUniqueId()) == null) {
            jugador.sendMessage("§cPrimero usá §e/dayrooms create <nombre> §co §e/dayrooms editor <nombre>");
            return false;
        }
        return true;
    }

    private ItemStack crearHerramienta(Material material, String nombre) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(nombre);
        item.setItemMeta(meta);
        return item;
    }
                                               }
