package com.dayrooms.commands;

import com.dayrooms.gui.MainMenu;
import com.dayrooms.managers.BarrierManager;
import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.RoomPersistenceManager;
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
    private final BarrierManager barrierManager;
    private final MessageManager messageManager;
    private final RoomPersistenceManager persistenceManager;

    public DayRoomsCommand(RoomManager roomManager, SelectionManager selectionManager,
                            BarrierManager barrierManager, MessageManager messageManager,
                            RoomPersistenceManager persistenceManager) {
        this.roomManager = roomManager;
        this.selectionManager = selectionManager;
        this.barrierManager = barrierManager;
        this.messageManager = messageManager;
        this.persistenceManager = persistenceManager;
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
            case "delete" -> eliminar(jugador, args);
            case "wand" -> darWand(jugador);
            case "barrier" -> darBarrier(jugador);
            case "teleport" -> darTeleportTool(jugador);
            case "keepinventory" -> setKeepInventory(jugador, args);
            case "utilities" -> setUtilities(jugador, args);
            case "save" -> guardar(jugador, args);
            case "reload" -> recargar(jugador);
            case "help" -> enviarAyuda(jugador);
            default -> jugador.sendMessage("Subcomando desconocido. Usa /dayrooms help para ver la lista.");
        }

        return true;
    }

    private void crear(Player jugador, String[] args) {
        if (args.length < 2) {
            jugador.sendMessage("Uso: /dayrooms create <nombre>");
            return;
        }
        String nombre = args[1];
        if (roomManager.existe(nombre)) {
            jugador.sendMessage("Ya existe una room con ese nombre.");
            return;
        }
        roomManager.crear(nombre, jugador.getUniqueId());
        selectionManager.setRoomEnEdicion(jugador.getUniqueId(), nombre);
        jugador.sendMessage("Room " + nombre + " creada. Ahora estas en modo edicion.");
        jugador.sendMessage("Usa /dayrooms wand, /dayrooms barrier y /dayrooms teleport para configurarla.");
        jugador.sendMessage("No te olvides de /dayrooms save " + nombre + " cuando termines de editarla.");
    }

    private void editar(Player jugador, String[] args) {
        if (args.length < 2) {
            jugador.sendMessage("Uso: /dayrooms editor <nombre>");
            return;
        }
        String nombre = args[1];
        Room room = roomManager.obtener(nombre);
        if (room == null) {
            jugador.sendMessage(messageManager.get("room-no-existe"));
            return;
        }
        selectionManager.setRoomEnEdicion(jugador.getUniqueId(), nombre);
        jugador.sendMessage("Ahora estas editando la room " + nombre);
    }

    private void eliminar(Player jugador, String[] args) {
        if (args.length < 2) {
            jugador.sendMessage("Uso: /dayrooms delete <nombre>");
            return;
        }
        String nombre = args[1];
        if (!roomManager.existe(nombre)) {
            jugador.sendMessage(messageManager.get("room-no-existe"));
            return;
        }
        roomManager.eliminar(nombre);
        persistenceManager.eliminarUna(nombre);
        jugador.sendMessage("Room " + nombre + " eliminada.");
    }

    private void darWand(Player jugador) {
        if (!tieneRoomEnEdicion(jugador)) return;
        jugador.getInventory().addItem(crearHerramienta(Material.GOLDEN_AXE, "Wand de Room"));
        selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.ESQUINAS);
        jugador.sendMessage("Wand recibida. Click der. = lado 1, click izq. = lado 2.");
    }

    private void darBarrier(Player jugador) {
        if (!tieneRoomEnEdicion(jugador)) return;
        jugador.getInventory().addItem(crearHerramienta(Material.IRON_HOE, "Barrier Tool"));
        selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.BARRERA);
        jugador.sendMessage("Herramienta de barrera recibida. Click der. = lado 1, click izq. = lado 2.");
    }

    private void darTeleportTool(Player jugador) {
        if (!tieneRoomEnEdicion(jugador)) return;
        jugador.getInventory().addItem(crearHerramienta(Material.DIAMOND_PICKAXE, "Teleport Tool"));
        selectionManager.setModo(jugador.getUniqueId(), SelectionManager.Modo.TELEPORT);
        jugador.sendMessage("Pico recibido. Click der. o izq. para marcar la zona de teleport.");
    }

    private void setKeepInventory(Player jugador, String[] args) {
        if (args.length < 3) {
            jugador.sendMessage(messageManager.get("uso-true-false").replace("%subcomando%", "keepinventory"));
            return;
        }
        Room room = roomManager.obtener(args[1]);
        if (room == null) {
            jugador.sendMessage(messageManager.get("room-no-existe"));
            return;
        }
        boolean valor = Boolean.parseBoolean(args[2]);
        room.setKeepInventory(valor);

        String key = valor ? "keepinventory-activado" : "keepinventory-desactivado";
        jugador.sendMessage(messageManager.get(key).replace("%room%", room.getName()));
    }

    private void setUtilities(Player jugador, String[] args) {
        if (args.length < 3) {
            jugador.sendMessage(messageManager.get("uso-true-false").replace("%subcomando%", "utilities"));
            return;
        }
        Room room = roomManager.obtener(args[1]);
        if (room == null) {
            jugador.sendMessage(messageManager.get("room-no-existe"));
            return;
        }
        boolean valor = Boolean.parseBoolean(args[2]);
        room.setUtilidadesHabilitadas(valor);

        String key = valor ? "utilities-activado" : "utilities-desactivado";
        jugador.sendMessage(messageManager.get(key).replace("%room%", room.getName()));
    }

    private void guardar(Player jugador, String[] args) {
        if (args.length < 2) {
            jugador.sendMessage("Uso: /dayrooms save <room>");
            return;
        }
        Room room = roomManager.obtener(args[1]);
        if (room == null) {
            jugador.sendMessage(messageManager.get("room-no-existe"));
            return;
        }
        persistenceManager.guardarUna(room);
        jugador.sendMessage("Room " + room.getName() + " guardada en rooms.yml");
    }

    private void recargar(Player jugador) {
        messageManager.recargar();
        jugador.sendMessage("config.yml recargado correctamente.");
    }

    private void enviarAyuda(Player jugador) {
        jugador.sendMessage("--- DayRooms - Ayuda ---");
        jugador.sendMessage("/dayrooms - Abre el menu principal");
        jugador.sendMessage("/dayrooms create <nombre> - Crea una room nueva");
        jugador.sendMessage("/dayrooms editor <nombre> - Edita una room existente");
        jugador.sendMessage("/dayrooms delete <nombre> - Elimina una room");
        jugador.sendMessage("/dayrooms wand - Herramienta para marcar esquinas");
        jugador.sendMessage("/dayrooms barrier - Herramienta para marcar la barrera");
        jugador.sendMessage("/dayrooms teleport - Herramienta para marcar zona de teleport");
        jugador.sendMessage("/dayrooms keepinventory <room> <true|false>");
        jugador.sendMessage("/dayrooms utilities <room> <true|false>");
        jugador.sendMessage("/dayrooms save <room> - Guarda los cambios de una room");
        jugador.sendMessage("/dayrooms reload - Recarga la configuracion");
        jugador.sendMessage("Creador: SoyBughatti");
    }

    private boolean tieneRoomEnEdicion(Player jugador) {
        if (selectionManager.getRoomEnEdicion(jugador.getUniqueId()) == null) {
            jugador.sendMessage(messageManager.get("no-tienes-room-en-edicion"));
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
