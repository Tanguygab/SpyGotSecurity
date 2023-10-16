package io.github.tanguygab.spygotsecurity.listeners;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.managers.ItemManager;
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

@AllArgsConstructor
public class ModuleListener implements Listener {

    private SpyGotSecurity plugin;

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(PlayerInteractEvent e) {
        if (e.useItemInHand() == Event.Result.DENY || e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemManager im = plugin.getItemManager();
        SGSModule module = im.getModuleFromItem(e.getItem());
        if (module == null) return;

        e.setCancelled(true);
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            LockedBlock locked = plugin.getBlockManager().getLockedBlocks().get(e.getClickedBlock());
            if (locked != null) {
                if (!locked.getOwner().equals(player.getUniqueId()) || !player.isSneaking()) {
                    locked.onClick(player);
                    return;
                }
                if (!locked.hasModule(module)) {
                    locked.getModules().add(module);
                    Utils.send(player, "&aModule inserted!");
                    return;
                }
            }
        }
        module.getMenu(player).open();
    }

}
