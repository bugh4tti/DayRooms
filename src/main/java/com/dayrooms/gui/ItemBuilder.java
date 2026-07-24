package com.dayrooms.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder nombre(String nombre) {
        meta.setDisplayName(nombre);
        return this;
    }

    public ItemBuilder lore(String... lineas) {
        meta.setLore(Arrays.asList(lineas));
        return this;
    }

    public ItemBuilder lore(List<String> lineas) {
        meta.setLore(lineas);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
