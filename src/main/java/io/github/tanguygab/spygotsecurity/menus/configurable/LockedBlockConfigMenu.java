package io.github.tanguygab.spygotsecurity.menus.configurable;

import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.menus.ConfigurableBlockMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class LockedBlockConfigMenu extends ConfigurableBlockMenu<LockedBlock> {

    public LockedBlockConfigMenu(Player player, LockedBlock block) {
        super(player, block);
        allowModules();
    }

    @Override
    public void onClick(ItemStack item, int slot, ClickType click) {

    }
}
