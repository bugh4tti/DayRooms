package com.dayrooms.commands;

import com.dayrooms.gui.EffectsMenu;
import com.dayrooms.managers.RoomManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class DayRoomsTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMANDOS = List.of(
            "create", "editor", "delete", "wand", "barrier", "teleport",
            "keepinventory", "utilities", "commands", "effect", "save", "stats", "reload", "help"
    );

    private static final List<String> REQUIEREN_ROOM_ARG2 = List.of(
            "editor", "delete", "keepinventory", "utilities", "commands", "save"
    );

    private final RoomManager roomManager;

    public DayRoomsTabCompleter(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> sugerencias = new ArrayList<>();

        if (args.length == 1) {
            String escrito = args[0].toLowerCase();
            for (String sub : SUBCOMANDOS) {
                if (sub.startsWith(escrito)) {
                    sugerencias.add(sub);
                }
            }
            return sugerencias;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("stats")) {
            String escrito = args[1].toLowerCase();
            for (var jugadorOnline : Bukkit.getOnlinePlayers()) {
                if (jugadorOnline.getName().toLowerCase().startsWith(escrito)) {
                    sugerencias.add(jugadorOnline.getName());
                }
            }
            return sugerencias;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("effect")) {
            for (String accion : List.of("add", "remove", "set")) {
                if (accion.startsWith(args[1].toLowerCase())) {
                    sugerencias.add(accion);
                }
            }
            return sugerencias;
        }

        if (args.length == 2 && REQUIEREN_ROOM_ARG2.contains(args[0].toLowerCase())) {
            String escrito = args[1].toLowerCase();
            for (var room : roomManager.listarTodas()) {
                if (room.getName().toLowerCase().startsWith(escrito)) {
                    sugerencias.add(room.getName());
                }
            }
            return sugerencias;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("effect")) {
            String escrito = args[2].toLowerCase();
            for (var room : roomManager.listarTodas()) {
                if (room.getName().toLowerCase().startsWith(escrito)) {
                    sugerencias.add(room.getName());
                }
            }
            return sugerencias;
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("effect")) {
            String escrito = args[3].toUpperCase();
            for (String[] efecto : EffectsMenu.EFECTOS) {
                if (efecto[0].startsWith(escrito)) {
                    sugerencias.add(efecto[0]);
                }
            }
            return sugerencias;
        }

        if (args.length == 3 && (args[0].equalsIgnoreCase("keepinventory")
                || args[0].equalsIgnoreCase("utilities")
                || args[0].equalsIgnoreCase("commands"))) {
            sugerencias.add("true");
            sugerencias.add("false");
            return sugerencias;
        }

        return sugerencias;
    }
            }
