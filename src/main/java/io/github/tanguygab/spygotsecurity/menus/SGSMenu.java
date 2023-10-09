package io.github.tanguygab.spygotsecurity.menus;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public abstract class SGSMenu {

    protected final Player player;
    protected final SpyGotSecurity plugin = SpyGotSecurity.getInstance();
    public Inventory inv;

    public abstract void open();
    public abstract void onClick(ItemStack item, int slot, ClickType click);

    public void close() {
        plugin.getInventoryListener().close(player);
    }

    public void fillSlots(int... slots) {
        ItemStack filler = getItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (Integer slot : slots) inv.setItem(slot,filler);
    }

    public void open(SGSMenu menu) {
        plugin.getInventoryListener().open(player,menu);
    }

    public ItemStack getItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&r"+name));
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack getItem(Material mat, String name) {
        return getItem(new ItemStack(mat),name);
    }

}
