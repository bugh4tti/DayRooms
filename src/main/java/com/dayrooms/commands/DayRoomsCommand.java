package com.dayrooms.commands;

import com.dayrooms.gui.MainMenu;
import com.dayrooms.managers.RoomManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayRoomsCommand implements CommandExecutor {

    private final RoomManager roomManager;

    public DayRoomsCommand(RoomManager roomManager) {
        this.roomManager = roomManager;
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
            default -> jugador.sendMessage("§7Subcomando aún no implementado: §e" + args[0]);
        }

        return true;
    }
              }
