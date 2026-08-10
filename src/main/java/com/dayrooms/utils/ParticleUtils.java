package com.dayrooms.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtils {

    public static void spawnColor(Location loc, Color color) {
        if (loc.getWorld() == null) {
            return;
        }
        loc.getWorld().spawnParticle(
                Particle.REDSTONE,
                loc.clone().add(0.5, 0.5, 0.5),
                12, 0.2, 0.2, 0.2, 0,
                new Particle.DustOptions(color, 1.2f)
        );
    }
    }
