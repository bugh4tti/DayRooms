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

public class BarrierManager {

    private final JavaPlugin plugin;
    private final MessageManager messageManager;

    public BarrierManager(JavaPlugin plugin, MessageManager messageManager) {
        this.plugin = plugin;
        this.messageManager = messageManager;
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
        int maxX = Math.max(loc1.getB
