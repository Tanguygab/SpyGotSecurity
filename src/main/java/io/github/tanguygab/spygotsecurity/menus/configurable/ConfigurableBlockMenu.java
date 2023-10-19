package io.github.tanguygab.spygotsecurity.menus.configurable;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.modules.ModuleType;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfigurableBlockMenu<ConfigurableBlock> extends SGSMenu {

    protected final ConfigurableBlock block;
    private final List<ModuleType> allowedModules = new ArrayList<>();

    public ConfigurableBlockMenu(Player player, ConfigurableBlock block) {
        super(player);
        this.block = block;
    }

    private io.github.tanguygab.spygotsecurity.blocks.ConfigurableBlock block() {
        return (io.github.tanguygab.spygotsecurity.blocks.ConfigurableBlock) block;
    }

    public void allowModules(ModuleType... types) {
        if (types.length == 0) types = ModuleType.values();
        allowedModules.addAll(List.of(types));
    }

    @Override
    public void onOpen() {
        inv = plugin.getServer().createInventory(null,36, "Configuration");
        fillBorders();

        allowedModules.forEach(module->{
            int slot = 10+allowedModules.indexOf(module);
            ItemStack moduleItem;
            if (block().getModules().containsKey(module)) {
                moduleItem = plugin.getItemManager().getItemFromModuleType(module);
                ItemMeta meta = moduleItem.getItemMeta();
                assert meta != null;
                meta.addEnchant(Enchantment.DURABILITY,1,true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                moduleItem.setItemMeta(meta);
            } else moduleItem = getItem(Material.WHITE_STAINED_GLASS_PANE,"No Module");
            inv.setItem(slot,moduleItem);
            inv.setItem(slot+9,getItem(module.getMaterial(),module.getName()+" Module"));
        });

        onMenuOpen();
    }

    @Override
    public void onClick(ItemStack item, int slot, ClickType click) {
        for (ModuleType moduleType : allowedModules) {
            int moduleSlot = 10+allowedModules.indexOf(moduleType);
            if (moduleSlot != slot) continue;
            if (block().getModules().containsKey(moduleType)) {
                SGSModule module = block().getModules().get(moduleType);
                block().removeModule(player,module);
                open();
            }
            return;
        }
        SGSModule module = plugin.getItemManager().getModuleFromItem(item);
        if (module != null) {
            block().addModule(player, module, item);
            open();
            return;
        }
        onItemClick(item,slot,click);
    }

    public abstract void onMenuOpen();
    public abstract void onItemClick(ItemStack item, int slot, ClickType click);
}
