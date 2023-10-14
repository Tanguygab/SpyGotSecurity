package io.github.tanguygab.spygotsecurity.listeners;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.features.BlockManager;
import io.github.tanguygab.spygotsecurity.utils.MultiBlockUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.Objects;

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
        LockedBlock locked = bm.getBlockFromItem(block,player,e.getItemInHand());
        plugin.getServer().getScheduler().runTaskLater(plugin,()-> {
            if (locked == null) {
                Block other = MultiBlockUtils.getSide(e.getBlock());
                if (bm.getLockedBlocks().get(other) != null)
                    MultiBlockUtils.setSingle(other,block);
                return;
            }
            Block other = MultiBlockUtils.getSide(block);
            LockedBlock lockedOther = bm.getLockedBlocks().get(other);
            if (!MultiBlockUtils.isMultiBlock(locked,lockedOther)) {
                MultiBlockUtils.setSingle(block,other);
                bm.addLockedBlock(locked);
                send(player, "&aLocked Block placed! Right-Click to add a password");
                return;
            }
            lockedOther.syncPasswordTo(locked);
            bm.addLockedBlock(locked);
            send(player, "&aDouble Locked Chest placed! Password synchronized with its other half.");
        },1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (!bm.getLockedBlocks().containsKey(block)) return;
        Player player = e.getPlayer();
        LockedBlock locked = bm.getLockedBlocks().get(block);
        if (!locked.isOwner(player)) {
            e.setCancelled(true);
            send(player,"&cYou are not the owner of this block!");
            return;
        }
        plugin.getBlockManager().getLockedBlocks().remove(e.getBlock());
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
                drop(item, block);
            }
        }

        send(player,"&cLocked Block deleted!");
        if (box != null) {
            if (box.getInventory().isEmpty() && player.getGameMode() == GameMode.CREATIVE)
                return;
            meta.setBlockState(box);
            drop.setItemMeta(meta);
        }
        drop(drop,block);
    }

    private void drop(ItemStack item, Block block) {
        Objects.requireNonNull(block.getLocation().getWorld()).dropItemNaturally(block.getLocation(),item);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK  || !bm.getLockedBlocks().containsKey(block) || e.getHand() == EquipmentSlot.OFF_HAND) return;
        e.setCancelled(true);
        LockedBlock locked = bm.getLockedBlocks().get(block);
        locked.onClick(e.getPlayer());
    }

}
