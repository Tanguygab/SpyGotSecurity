package io.github.tanguygab.spygotsecurity.listeners;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.KeyPad;
import io.github.tanguygab.spygotsecurity.features.BlockManager;
import io.github.tanguygab.spygotsecurity.utils.NamespacedKeys;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BlockListener extends SGSListener {

    private final BlockManager bm = plugin.getBlockManager();

    public BlockListener(SpyGotSecurity plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        ItemMeta meta = e.getItemInHand().getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(NamespacedKeys.KEYPAD, PersistentDataType.BOOLEAN)) return;
        Player player = e.getPlayer();
        bm.addKeyPad(e.getBlock(),player);
        send(player,"&aKeypad created! Right-Click to add a password");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (!bm.isKeyPad(block)) return;
        Player player = e.getPlayer();
        if (!bm.getKeyPad(block).isOwner(player)) {
            e.setCancelled(true);
            send(player,"&cYou are not the owner of this keypad!");
            return;
        }
        plugin.getBlockManager().removeKeyPad(e.getBlock());
        send(player,"&cKeypad deleted!");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (!bm.isKeyPad(block) || e.getHand() == EquipmentSlot.OFF_HAND) return;
        KeyPad keyPad = bm.getKeyPad(block);
        Player player = e.getPlayer();
        if (!keyPad.isOwner(player) && keyPad.getPassword() == null) {
            send(player,"&cThis keypad hasn't been configured yet!");
            return;
        }

        keyPad.openMenu(player);
        send(player,"opening menu");
    }

}
