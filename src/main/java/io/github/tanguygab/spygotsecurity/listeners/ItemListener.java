package io.github.tanguygab.spygotsecurity.listeners;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.managers.BlockManager;
import io.github.tanguygab.spygotsecurity.managers.ItemManager;
import io.github.tanguygab.spygotsecurity.menus.BlockReinforcerMenu;
import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import io.github.tanguygab.spygotsecurity.utils.ItemUtils;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import java.util.Map;
import java.util.UUID;

public class ItemListener implements Listener {

    private final SpyGotSecurity plugin;
    private final ItemManager im;
    private final BlockManager bm;
    private final List<Player> clickers = new ArrayList<>(); // accounting for when the event is fired twice

    public ItemListener(SpyGotSecurity plugin) {
        this.plugin = plugin;
        im = plugin.getItemManager();
        bm = plugin.getBlockManager();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.useItemInHand() == Event.Result.DENY || e.getHand() == EquipmentSlot.OFF_HAND || clickers.contains(player)) return;
        clickers.add(player);
        plugin.getServer().getScheduler().runTaskLater(plugin,()->clickers.remove(player),3);

        Block block = e.getClickedBlock();
        ItemStack item = e.getItem();
        String type = ItemUtils.getTypeFromItem(ItemManager.ITEM,item);
        if (type == null) return;
        e.setCancelled(true);
        Map<Block,UUID> blocks = bm.getReinforcedBlocks();
        switch (type) {
            case "lockpick","codebreaker" -> {}
            case "remover" -> {
                if (block == null || !player.getUniqueId().equals(blocks.get(block))) return;
                ItemUtils.drop(ItemUtils.getReinforcedCopy(new ItemStack(block.getType()),true),block);
                block.setType(Material.AIR);
            }
            case "reinforcer" -> {
                switch (e.getAction()) {
                    case RIGHT_CLICK_AIR -> new BlockReinforcerMenu(player).open();
                    case LEFT_CLICK_BLOCK -> {
                        if (block == null) return;
                        if (player.isSneaking() && player.getUniqueId().equals(blocks.get(block))) {
                            Utils.actionbar(player,"&cBlock Turned back to normal!");
                            blocks.remove(block);
                            return;
                        }
                        if (blocks.containsKey(block) || !bm.canReinforce(block.getType())) return;
                        blocks.put(block,player.getUniqueId());
                        Utils.actionbar(player,"&aBlock Reinforced!");
                    }
                }
            }
            default -> {
                if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                SGSModule module = im.getModuleFromItem(item);
                if (module != null && im.getAllowedModules().contains(module.getType()))
                    onModule(module, player, item, block);
            }
        }
    }

    public void onModule(SGSModule module, Player player, ItemStack item, Block block) {
        if (block != null) {
            LockedBlock locked = bm.getLockedBlocks().get(block);
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
