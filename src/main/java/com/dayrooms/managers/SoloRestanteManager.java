package com.dayrooms.managers;

import com.dayrooms.model.Room;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cuando un jugador se va de una room cerrada y queda 1 solo adentro,
 * arranca un countdown configurable para volver a abrir la barrera.
 * Se cancela si vuelve a entrar un segundo jugador antes de tiempo.
 */
public class SoloRestanteManager {

    private final JavaPlugin plugin;
    private final BarrierManager barrierManager;
    private final MessageManager messageManager;

    private final Map<String, BukkitTask> tareasPendientes = new HashMap<>();

    public SoloRestanteManager(JavaPlugin plugin, BarrierManager barrierManager, MessageManager messageManager) {
        this.plugin = plugin;
        this.barrierManager = barrierManager;
        this.messageManager = messageManager;
    }

    public void iniciarCountdown(Room room, List<Player> jugadoresRestantes, int segundosTotales) {
        cancelar(room.getName());

        BukkitTask tarea = new BukkitRunnable() {
            int segundosRestantes = segundosTotales;

            @Override
            public void run() {
                if (segundosRestantes <= 0) {
                    barrierManager.romperBarrera(room);
                    String mensajeRota = messageManager.get("barrera-rota");
                    for (Player p : jugadoresRestantes) {
                        if (p.isOnline()) {
                            p.sendMessage(mensajeRota);
                        }
                    }
                    tareasPendientes.remove(room.getName());
                    this.cancel();
                    return;
                }

                String mensaje = messageManager.get("barrera-countdown")
                        .replace("%seg%", String.valueOf(segundosRestantes));

                for (Player p : jugadoresRestantes) {
                    if (p.isOnline()) {
                        p.sendMessage(mensaje);
                    }
                }

                segundosRestantes--;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        tareasPendientes.put(room.getName(), tarea);
    }

    public void cancelar(String nombreRoom) {
        BukkitTask tarea = tareasPendientes.remove(nombreRoom);
        if (tarea != null) {
            tarea.cancel();
        }
    }

    public boolean tieneCountdownActivo(String nombreRoom) {
        return tareasPendientes.containsKey(nombreRoom);
    }
                                 }
