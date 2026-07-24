package com.dayrooms.managers;

import com.dayrooms.model.Room;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Coloca/rompe el cristal de la barrera y maneja el cooldown de 15s
 * que arranca cuando alguien gana un 1v1 en la room.
 */
public class BarrierManager {

    private final JavaPlugin plugin;

    public BarrierManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void colocarBarrera(Room room) {
        rellenarCuboid(room.getBarreraEsquina1(), room.getBarreraEsquina2(), Material.GLASS);
    }

    public void romperBarrera(Room room) {
        rellenarCuboid(room.getBarreraEsquina1(), room.getBarreraEsquina2(), Material.AIR);
    }

    private void rellenarCuboid(Location loc1, Location loc2, Material material) {
        if (loc1 == null || loc2 == null || loc1.getWorld() == null) {
            return;
        }
        World world = loc1.getWorld();

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }

    /**
     * Junta los jugadores online que están parados dentro de la zona
     * de la room (entre esquina1 y esquina2).
     */
    public List<Player> obtenerJugadoresEnRoom(Room room) {
        List<Player> encontrados = new ArrayList<>();
        Location loc1 = room.getEsquina1();
        Location loc2 = room.getEsquina2();
        if (loc1 == null || loc2 == null || loc1.getWorld() == null) {
            return encontrados;
        }

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (Player jugador : loc1.getWorld().getPlayers()) {
            Location loc = jugador.getLocation();
            if (loc.getBlockX() >= minX && loc.getBlockX() <= maxX
                    && loc.getBlockY() >= minY && loc.getBlockY() <= maxY
                    && loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ) {
                encontrados.add(jugador);
            }
        }
        return encontrados;
    }

    /**
     * Se llama apenas hay un ganador de 1v1: cierra la barrera (cristal)
     * y cuenta 15s por chat a todos los jugadores de la room. Al llegar
     * a 0, rompe el cristal para que la room quede lista de nuevo.
     */
    public void iniciarCountdownPostVictoria(Room room, List<Player> jugadoresEnRoom) {
        colocarBarrera(room);

        new BukkitRunnable() {
            int segundosRestantes = 15;

            @Override
            public void run() {
                if (segundosRestantes <= 0) {
                    romperBarrera(room);
                    this.cancel();
                    return;
                }

                String mensaje = "§e§lLa barrera se romperá en §c§l" + segundosRestantes + "s§e§l!"
                        .replace("%seg%", String.valueOf(segundosRestantes));

                for (Player p : jugadoresEnRoom) {
                    if (p.isOnline()) {
                        p.sendMessage(mensaje);
                    }
                }

                segundosRestantes--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
          }
