package io.github.tanguygab.spygotsecurity.menus.modules;

import io.github.tanguygab.spygotsecurity.modules.ListModule;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ListModuleMenu extends ModuleMenu{

    public ListModuleMenu(Player player, ListModule module) {
        super(player, module);
        inv = plugin.getServer().createInventory(null, 54, "");
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClick(ItemStack item, int slot, ClickType click) {

    }
}
