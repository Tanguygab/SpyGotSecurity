package io.github.tanguygab.spygotsecurity.listeners;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;

public class InventoryListener extends SGSListener {

    public final Map<Player,SGSMenu> openedMenus = new HashMap<>();

    public InventoryListener(SpyGotSecurity plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (openedMenus.containsKey(p)) {
            openedMenus.get(p).onClick(e.getCurrentItem(),e.getRawSlot(),e.getClick());
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        SGSMenu menu = openedMenus.get(p);
        if (menu != null && menu.inv.equals(e.getInventory())) openedMenus.get(p).close();
    }

    public void open(Player p, SGSMenu menu) {
        openedMenus.put(p,menu);
        menu.open();
    }

    public void close(Player p) {
        openedMenus.remove(p);
        p.closeInventory();
    }

}
