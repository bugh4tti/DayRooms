package com.dayrooms.commands;

import com.dayrooms.gui.EffectsMenu;
import com.dayrooms.managers.MessageManager;
import com.dayrooms.managers.RoomManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class DayRoomsTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMANDOS = List.of(
            "create", "editor", "delete", "wand", "barrier", "teleport",
            "keepinventory", "utilities", "commands", "effect", "pvp", "blocktype",
            "save", "stats", "reload", "help"
    );

    private static final List<String> REQUIEREN_ROOM_ARG2 = List.of(
            "editor", "delete", "keepinventory", "utilities", "commands", "pvp", "blocktype", "save"
    );

    private final RoomManager roomManager;
    private final MessageManager messageManager;

    public DayRoomsTabCompleter(RoomManager roomManager, MessageManager messageManager) {
        this.roomManager = roomManager;
        this.messageManager = messageManager;
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
            for (var jugadorOnline : org.bukkit.Bukkit.getOnlinePlayers()) {
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

        if (args.length == 3 && args[0].equalsIgnoreCase("commands")) {
            for (String opcion : List.of("true", "false", "blocker")) {
                if (opcion.startsWith(args[2].toLowerCase())) {
                    sugerencias.add(opcion);
                }
            }
            return sugerencias;
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("commands") && args[2].equalsIgnoreCase("blocker")) {
            String escrito = args[3].toLowerCase();
            for (String cmd : messageManager.getStringList("comandos-importantes")) {
                if (cmd.toLowerCase().startsWith(escrito)) {
                    sugerencias.add(cmd);
                }
            }
            return sugerencias;
        }

        if (args.length == 5 && args[0].equalsIgnoreCase("commands") && args[2].equalsIgnoreCase("blocker")) {
            sugerencias.add("true");
            sugerencias.add("false");
            return sugerencias;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("pvp")) {
            sugerencias.add("on");
            sugerencias.add("off");
            return sugerencias;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("blocktype")) {
            if ("hand".startsWith(args[2].toLowerCase())) {
                sugerencias.add("hand");
            }
            return sugerencias;
        }

        if (args.length == 3 && (args[0].equalsIgnoreCase("keepinventory") || args[0].equalsIgnoreCase("utilities"))) {
            sugerencias.add("true");
            sugerencias.add("false");
            return sugerencias;
        }

        return sugerencias;
    }
                }
