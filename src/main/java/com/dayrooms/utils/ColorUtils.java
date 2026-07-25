package com.dayrooms.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern PATRON_HEX = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String traducir(String mensaje) {
        if (mensaje == null) {
            return "";
        }

        Matcher matcher = PATRON_HEX.matcher(mensaje);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder reemplazo = new StringBuilder("&x");
            for (char c : hex.toCharArray()) {
                reemplazo.append('&').append(c);
            }
            matcher.appendReplacement(buffer, reemplazo.toString());
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}
