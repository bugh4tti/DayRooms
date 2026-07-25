package com.dayrooms.utils;

import org.bukkit.potion.PotionEffectType;

public class EffectTypeMapper {

    public static PotionEffectType map(String key) {
        return switch (key.toUpperCase()) {
            case "FUERZA" -> PotionEffectType.INCREASE_DAMAGE;
            case "VELOCIDAD" -> PotionEffectType.SPEED;
            case "INVISIBILIDAD" -> PotionEffectType.INVISIBILITY;
            case "VISION_NOCTURNA" -> PotionEffectType.NIGHT_VISION;
            case "REGENERACION" -> PotionEffectType.REGENERATION;
            case "RESISTENCIA" -> PotionEffectType.DAMAGE_RESISTANCE;
            case "SALTO" -> PotionEffectType.JUMP;
            case "PRISA" -> PotionEffectType.FAST_DIGGING;
            default -> null;
        };
    }
    }
