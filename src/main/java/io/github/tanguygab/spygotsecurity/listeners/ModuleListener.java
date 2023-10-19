package io.github.tanguygab.spygotsecurity.listeners;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.managers.ItemManager;
import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ModuleListener implements Listener {

    private SpyGotSecurity plugin;
    private final List<Player> clickers = new ArrayList<>(); // accounting for when the event is fired twice

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.useItemInHand() == Event.Result.DENY || e.getHand() == EquipmentSlot.OFF_HAND
                || e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK || clickers.contains(player)) return;
        clickers.add(player);
        plugin.getServer().getScheduler().runTaskLater(plugin,()->clickers.remove(player),3);
        ItemManager im = plugin.getItemManager();
        ItemStack item = e.getItem();
        SGSModule module = im.getModuleFromItem(item);
        if (item == null || module == null || !im.getAllowedModules().contains(module.getType())) return;

        e.setCancelled(true);
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            LockedBlock locked = plugin.getBlockManager().getLockedBlocks().get(e.getClickedBlock());
            if (locked != null) {
                if (!locked.getOwner().equals(player.getUniqueId()) || !player.isSneaking()) {
                    locked.onClick(player);
                    return;
                }
                locked.addModule(player,module,item);
                Utils.send(player,"&aModule inserted!");
                return;
            }
        }
        SGSMenu moduleMenu = module.getMenu(player);
        if (moduleMenu != null) moduleMenu.open();
    }

}
