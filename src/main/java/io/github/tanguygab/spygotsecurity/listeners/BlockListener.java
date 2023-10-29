package io.github.tanguygab.spygotsecurity.listeners;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.managers.BlockManager;
import io.github.tanguygab.spygotsecurity.utils.ItemUtils;
import io.github.tanguygab.spygotsecurity.utils.MultiBlockUtils;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.List;

import static io.github.tanguygab.spygotsecurity.utils.Utils.*;

public class BlockListener implements Listener {

    private final SpyGotSecurity plugin;
    private final BlockManager bm;

    public BlockListener(SpyGotSecurity plugin) {
        this.plugin = plugin;
        bm = plugin.getBlockManager();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if (bm.isReinforcedBlockItem(e.getItemInHand())) {
            bm.getReinforcedBlocks().put(block,player.getUniqueId());
            return;
        }

        LockedBlock locked = bm.getBlockFromItem(block,player,e.getItemInHand());
        plugin.getServer().getScheduler().runTaskLater(plugin,()-> { // runTaskLater because otherwise the DoubleChest instance isn't created
            Block side = MultiBlockUtils.getSide(e.getBlock());
            if (locked == null) {
                if (bm.getLockedBlocks().get(side) != null)
                    MultiBlockUtils.setSingle(side, block);
                return;
            }
            if (side != null) MultiBlockUtils.setSingle(block, side);
            bm.addLockedBlock(locked);
            send(player, "&aLocked Block placed! Right-Click to add a password");
        },1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        if (bm.getReinforcedBlocks().containsKey(block) && (player.getGameMode() != GameMode.CREATIVE || !player.isSneaking())) {
            Utils.actionbar(player,"&cThis block is reinforced! "+(player.getGameMode() == GameMode.CREATIVE ? "Shift-Click to break" : "Can't break"));
            e.setCancelled(true);
            return;
        }
        if (!bm.getLockedBlocks().containsKey(block)) return;
        LockedBlock locked = bm.getLockedBlocks().get(block);
        if (!locked.isOwner(player)) {
            e.setCancelled(true);
            send(player,"&cYou are not the owner of this block!");
            return;
        }
        locked.resetBlock();
        plugin.getBlockManager().getLockedBlocks().remove(block);
        ItemStack drop = bm.getItem(locked);
        if (drop == null) return; // Block was changed while plugin was unloaded
        e.setDropItems(false);
        if (drop.getType() == Material.SHULKER_BOX) drop.setType(block.getType());
        BlockStateMeta meta = drop.getItemMeta() instanceof BlockStateMeta bsm ? bsm : null;
        ShulkerBox box = meta != null && meta.getBlockState() instanceof ShulkerBox sBox ? sBox : null;

        if (block.getState() instanceof Container container) {
            for (ItemStack item : container.getInventory().getStorageContents()) {
                if (item == null) continue;
                if (box != null) {
                    box.getInventory().addItem(item);
                    continue;
                }
                ItemUtils.drop(item, block);
            }
        }
        locked.getModules().values().forEach(module->{
            ItemStack item = plugin.getItemManager().getItemFromModule(module);
            ItemUtils.drop(item, block);
        });

        send(player,"&cLocked Block deleted!");
        if (box != null) {
            if (box.getInventory().isEmpty() && player.getGameMode() == GameMode.CREATIVE)
                return;
            meta.setBlockState(box);
            drop.setItemMeta(meta);
        }
        ItemUtils.drop(drop, block);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK  || !bm.getLockedBlocks().containsKey(block) || e.getHand() == EquipmentSlot.OFF_HAND) return;
        e.setCancelled(true);
        LockedBlock locked = bm.getLockedBlocks().get(block);
        locked.onClick(e.getPlayer());
    }

    @EventHandler
    public void onBlockExploded(BlockExplodeEvent e) {
        e.blockList().removeIf(this::isSGSBlock);
    }
    @EventHandler
    public void onBlockExploded(EntityExplodeEvent e) {
        e.blockList().removeIf(this::isSGSBlock);
    }
    @EventHandler(ignoreCancelled = true)
    public void onBlockMoved(BlockPistonExtendEvent e) {
        if (pistonEvent(e.getBlocks())) e.setCancelled(true);
    }
    @EventHandler(ignoreCancelled = true)
    public void onBlockMoved(BlockPistonRetractEvent e) {
        if (pistonEvent(e.getBlocks())) e.setCancelled(true);
    }
    private boolean pistonEvent(List<Block> blocks) {
        return blocks.stream().anyMatch(this::isSGSBlock);
    }
    private boolean isSGSBlock(Block block) {
        return bm.getReinforcedBlocks().containsKey(block) || bm.getLockedBlocks().containsKey(block);
    }

}
