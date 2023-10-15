package io.github.tanguygab.spygotsecurity.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtils {

    public static ItemStack getItem(Material material, String name, NamespacedKey key, String data, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Utils.color(name));
        meta.setLore(Utils.color(lore));
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING,data);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemMeta setData(ItemMeta meta, NamespacedKey key, String data) {
        if (meta == null) return null;
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING,data);
        return meta;
    }

    public static String getTypeFromItem(NamespacedKey key, ItemStack item) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) return null;
        return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }
}
