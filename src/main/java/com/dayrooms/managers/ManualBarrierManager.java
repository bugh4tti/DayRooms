package com.dayrooms.managers;

import com.dayrooms.model.Room;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ManualBarrierManager {

    private final JavaPlugin plugin;
    private final BarrierManager barrierManager;
    private final MessageManager messageManager;

    private final Map<String, UUID> rompedorPorRoom = new HashMap<>();
    private final Map<String, BukkitTask> tareasPendientes = new HashMap<>();

    public ManualBarrierManager(JavaPlugin plugin, BarrierManager barrierManager, MessageManager messageManager) {
        this.plugin = plugin;
        this.barrierManager = barrierManager;
        this.messageManager = messageManager;
    }

    public void registrarRotura(Room room, Player rompedor) {
        rompedorPorRoom.put(room.getName(), rompedor.getUniqueId());
    }

    public boolean esRompedor(Room room, UUID jugador) {
        UUID rompedor = rompedorPorRoom.get(room.getName());
        return rompedor != null && rompedor.equals(jugador);
    }

    public void onRompedorSalio(Room room) {
        cancelarTarea(room.getName());

        BukkitTask tarea = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            barrierManager.romperBarrera(room);
            rompedorPorRoom.remove(room.getName());
            tareasPendientes.remove(room.getName());
        }, 10L * 20L);

        tareasPendientes.put(room.getName(), tarea);
    }

    public void onRompedorVolvio(Room room, List<Player> jugadoresEnRoom, Player rompedor) {
        cancelarTarea(room.getName());
        rompedorPorRoom.remove(room.getName());

        barrierManager.colocarBarrera(room);

        String mensaje = messageManager.get("barrera-recerrada");
        for (Player p : jugadoresEnRoom) {
            if (p.isOnline() && !p.getUniqueId().equals(rompedor.getUniqueId())) {
                p.sendMessage(mensaje);
            }
        }
    }

    private void cancelarTarea(String nombreRoom) {
        BukkitTask tarea = tareasPendientes.remove(nombreRoom);
        if (tarea != null) {
            tarea.cancel();
        }
    }
                                }
