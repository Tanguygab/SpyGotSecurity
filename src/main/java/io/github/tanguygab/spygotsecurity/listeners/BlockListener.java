package io.github.tanguygab.spygotsecurity.listeners;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.features.BlockManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
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
        LockedBlock block = bm.getBlockFromItem(e.getBlock(),player,e.getItemInHand());
        if (block == null) return;
        bm.addLockedBlock(block);
        send(player,"&aKeypad created! Right-Click to add a password");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (!bm.getLockedBlocks().containsKey(block)) return;
        Player player = e.getPlayer();
        if (!bm.getLockedBlocks().get(block).isOwner(player)) {
            e.setCancelled(true);
            send(player,"&cYou are not the owner of this keypad!");
            return;
        }
        plugin.getBlockManager().getLockedBlocks().remove(e.getBlock());
        send(player,"&cKeypad deleted!");
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
