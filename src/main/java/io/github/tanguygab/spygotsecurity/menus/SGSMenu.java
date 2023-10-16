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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class SGSMenu {

    private final static Map<String,PlayerProfile> profiles = new HashMap<>();

    protected final Player player;
    protected final SpyGotSecurity plugin = SpyGotSecurity.getInstance();
    public Inventory inv;

    public abstract void onOpen();
    public abstract void onClick(ItemStack item, int slot, ClickType click);

    public void open() {
        plugin.getInventoryListener().open(player,this);
    }
    public void close() {
        plugin.getInventoryListener().close(player);
    }

    public void fillMenu() {
        ItemStack filler = getItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getType().isAir())
                inv.setItem(i,filler);
        }
    }

    public void fillBorders() {
        fillSlots(Material.GRAY_STAINED_GLASS_PANE,0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,
                46,47,48,49,50,51,52,53);
    }

    public void fillSlots(Material mat, int... slots) {
        ItemStack filler = getItem(mat, " ");
        for (Integer slot : slots) inv.setItem(slot,filler);
    }

    protected ItemStack getItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&r"+name));
        item.setItemMeta(meta);
        return item;
    }
    protected ItemStack getItem(Material mat, String name) {
        return getItem(new ItemStack(mat),name);
    }
    protected ItemStack getHead(String texture, String name) {
        ItemStack item = getItem(Material.PLAYER_HEAD, "&f"+name);
        if (item.getItemMeta() instanceof SkullMeta skull) {
            skull.setOwnerProfile(getProfile(texture));
            item.setItemMeta(skull);
        }
        return item;
    }
    private PlayerProfile getProfile(String texture) {
        if (profiles.containsKey(texture)) return profiles.get(texture);
        PlayerProfile profile = plugin.getServer().createPlayerProfile(UUID.randomUUID());
        try {
            profile.getTextures().setSkin(new URL("http://textures.minecraft.net/texture/"+texture));
        } catch (Exception ignored) {}
        profiles.put(texture,profile);
        return profile;
    }

}
