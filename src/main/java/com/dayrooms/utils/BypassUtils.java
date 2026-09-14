package com.dayrooms.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Determina si un jugador puede ignorar las restricciones de la room
 * (limite de 2 jugadores, no pararse en la barrera). Se considera
 * bypass a quien tenga el permiso dayrooms.bypass, este en modo
 * espectador (/gm spectator), o tenga la metadata "vanished"/"staffmode"
 * que setean plugins de vanish/staff como CMI, Essentials, SuperVanish, etc
 * (usados normalmente por /vanish y /staff).
 */
public class BypassUtils {

    public static boolean tieneBypass(Player jugador) {
        if (jugador.hasPermission("dayrooms.bypass")) {
            return true;
        }
        if (jugador.getGameMode() == GameMode.SPECTATOR) {
            return true;
        }
        if (jugador.hasMetadata("vanished") && !jugador.getMetadata("vanished").isEmpty()
                && jugador.getMetadata("vanished").get(0).asBoolean()) {
            return true;
        }
        if (jugador.hasMetadata("staffmode") && !jugador.getMetadata("staffmode").isEmpty()
                && jugador.getMetadata("staffmode").get(0).asBoolean()) {
            return true;
        }
        return false;
    }
            }
