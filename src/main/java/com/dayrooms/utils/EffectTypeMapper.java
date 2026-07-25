package com.dayrooms.utils;

import org.bukkit.potion.PotionEffectType;

public class EffectTypeMapper {

    public static PotionEffectType map(String key) {
        return switch (key.toUpperCase()) {
            case "FUERZA" -> PotionEffectType.STRENGTH;
            case "VELOCIDAD" -> PotionEffectType.SPEED;
            case "INVISIBILIDAD" -> PotionEffectType.INVISIBILITY;
            case "VISION_NOCTURNA" -> PotionEffectType.NIGHT_VISION;
            case "REGENERACION" -> PotionEffectType.REGENERATION;
            case "RESISTENCIA" -> PotionEffectType.RESISTANCE;
            case "SALTO" -> PotionEffectType.JUMP_BOOST;
            case "PRISA" -> PotionEffectType.FAST_DIGGING;
            default -> null;
        };
    }
              }
