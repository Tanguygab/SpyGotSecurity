package io.github.tanguygab.spygotsecurity.menus.modules;

import io.github.tanguygab.spygotsecurity.managers.ItemManager;
import io.github.tanguygab.spygotsecurity.modules.DisguiseModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class DisguiseModuleMenu extends ModuleMenu<DisguiseModule> {

    private final ItemManager im = plugin.getItemManager();

    public DisguiseModuleMenu(Player player, DisguiseModule module) {
        super(player,module);
        inv = plugin.getServer().createInventory(null,InventoryType.DISPENSER,"Disguise Module");
    }

    @Override
    public void onOpen() {
        fillSlots(0,1,2,3,5,6,7,8);
        if (module.getMaterial() == null) return;
        ItemStack item = new ItemStack(module.getMaterial());
        inv.setItem(4,item);
    }

    @Override
    public boolean clickCancelled(ItemStack item, int slot, ClickType click) {
        if ((slot != 4 && slot < 10) || click.isShiftClick()) return true;
        ItemStack cursor = player.getItemOnCursor();
        if (slot == 4 && !im.canDisguise(cursor.getType())) return true;
        ItemStack input = slot == 4 ? cursor : inv.getItem(4);
        module.setMaterial(input == null || input.getType() == Material.AIR ? null : input.getType());
        return false;
    }

}
