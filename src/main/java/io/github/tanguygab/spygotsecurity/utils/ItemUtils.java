package io.github.tanguygab.spygotsecurity.utils;

import io.github.tanguygab.spygotsecurity.managers.BlockManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
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

    public static void drop(ItemStack item, Block block) {
        Location location = block.getLocation();
        if (location.getWorld() != null)
            location.getWorld().dropItemNaturally(location,item);
    }

    public static ItemStack getReinforcedCopy(ItemStack item, boolean reinforce) {
        if (item == null || item.getType().isAir()) return null;
        ItemStack copy = item.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        meta.setDisplayName(reinforce ? Utils.color("&8&lReinforced " + getName(item)) : null);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (reinforce) pdc.set(BlockManager.REINFORCED, PersistentDataType.BOOLEAN,true);
        else pdc.remove(BlockManager.REINFORCED);
        copy.setItemMeta(meta);
        return copy;
    }

    private static String getName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) return meta.getDisplayName();
        StringBuilder name = new StringBuilder();
        for (String part : item.getType().toString().split("_"))
            name.append(" ").append(part.charAt(0)).append(part.substring(1).toLowerCase());
        return name.deleteCharAt(0).toString();
    }
}
