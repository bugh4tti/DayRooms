package com.dayrooms.listeners;

import com.dayrooms.managers.RoomManager;
import com.dayrooms.managers.SelectionManager;
import com.dayrooms.model.Room;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class SelectionListener implements Listener {

    private final SelectionManager selectionManager;
    private final RoomManager roomManager;

    public SelectionListener(SelectionManager selectionManager, RoomManager roomManager) {
        this.selectionManager = selectionManager;
        this.roomManager = roomManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player jugador = event.getPlayer();
        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }

        UUID uuid = jugador.getUniqueId();
        SelectionManager.Modo modo = selectionManager.getModo(uuid);
        if (modo == SelectionManager.Modo.NINGUNO) {
            return;
        }

        String nombreRoom = selectionManager.getRoomEnEdicion(uuid);
        if (nombreRoom == null) {
            jugador.sendMessage("§cPrimero tenés que estar editando una room. Usá §e/dayrooms editor <nombre>");
            return;
        }

        Room room = roomManager.obtener(nombreRoom);
        if (room == null) {
            jugador.sendMessage("§cLa room que estabas editando ya no existe.");
            return;
        }

        event.setCancelled(true);
        Location loc = event.getClickedBlock().getLocation();

        switch (modo) {
            case ESQUINAS -> manejarEsquinas(jugador, room, action, loc);
            case BARRERA -> manejarBarrera(jugador, room, action, loc);
            case TELEPORT -> manejarTeleport(jugador, room, loc);
            default -> {}
        }
    }

    private void manejarEsquinas(Player jugador, Room room, Action action, Location loc) {
        UUID uuid = jugador.getUniqueId();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            selectionManager.marcarLado1(uuid, loc);
            jugador.sendMessage("§a✔ Lado 1 de la room marcado.");
        } else {
            selectionManager.marcarLado2(uuid, loc);
            jugador.sendMessage("§a✔ Lado 2 de la room marcado.");
        }

        if (selectionManager.tieneAmbosLados(uuid)) {
            room.setEsquina1(selectionManager.getLado1(uuid));
            room.setEsquina2(selectionManager.getLado2(uuid));
            room.setEsquinasDefinidas(true);
            jugador.sendMessage("§b§l¡Esquinas de la room §f" + room.getName() + "§b§l guardadas!");
            selectionManager.limpiarSeleccion(uuid);
        }
    }

    private void manejarBarrera(Player jugador, Room room, Action action, Location loc) {
        UUID uuid = jugador.getUniqueId();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            selectionManager.marcarLado1(uuid, loc);
            jugador.sendMessage("§a✔ Lado 1 de la barrera marcado.");
        } else {
            selectionManager.marcarLado2(uuid, loc);
            jugador.sendMessage("§a✔ Lado 2 de la barrera marcado.");
        }

        if (selectionManager.tieneAmbosLados(uuid)) {
            room.setBarreraEsquina1(selectionManager.getLado1(uuid));
            room.setBarreraEsquina2(selectionManager.getLado2(uuid));
            room.setBarreraDefinida(true);
            jugador.sendMessage("§b§l¡Barrera de la room §f" + room.getName() + "§b§l guardada!");
            selectionManager.limpiarSeleccion(uuid);
        }
    }

    private void manejarTeleport(Player jugador, Room room, Location loc) {
        room.setTeleportLocation(loc);
        room.setTeleportZoneDefinida(true);
        jugador.sendMessage("§b§l¡Zona de teleport de la room §f" + room.getName() + "§b§l guardada!");
        selectionManager.limpiarSeleccion(jugador.getUniqueId());
    }
    }
