package io.github.tanguygab.spygotsecurity.menus.configurable;

import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class LockedBlockConfigMenu extends ConfigurableBlockMenu<LockedBlock> {

    public LockedBlockConfigMenu(Player player, LockedBlock block) {
        super(player, block);
        allowModules();
    }

    @Override
    public void onMenuOpen() {}

    @Override
    public void onItemClick(ItemStack item, int slot, ClickType click) {}


}
