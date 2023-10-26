package io.github.tanguygab.spygotsecurity.menus;

import io.github.tanguygab.spygotsecurity.managers.BlockManager;
import io.github.tanguygab.spygotsecurity.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class BlockReinforcerMenu extends SGSMenu {

    private final BlockManager bm = plugin.getBlockManager();

    public BlockReinforcerMenu(Player player) {
        super(player);
        inv = plugin.getServer().createInventory(null,InventoryType.ANVIL,"Reinforce Blocks");
    }

    @Override
    public void onOpen() {}

    @Override
    public boolean clickCancelled(ItemStack item, int slot, ClickType click) {
        if (slot == 1 || click.isShiftClick()) return true;
        if (slot == 2) {
            inv.setItem(0,null);
            return false;
        }
        ItemStack cursor = player.getItemOnCursor();
        if (slot == 0 && !bm.canReinforce(cursor.getType())) return true;

        ItemStack input = slot == 0 ? cursor : inv.getItem(1);
        inv.setItem(2, ItemUtils.getReinforcedCopy(input,!bm.isReinforcedBlockItem(input)));
        return false;
    }

    @Override
    public void onClose() {
        ItemStack item = inv.getItem(0);
        if (item == null || item.getType().isAir()) return;
        if (!player.getInventory().addItem(item).isEmpty())
            player.getWorld().dropItem(player.getLocation(),item);
    }

}
