package io.github.tanguygab.spygotsecurity.menus;

import io.github.tanguygab.spygotsecurity.modules.ModuleType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
        inv = plugin.getServer().createInventory(null,36);
        fillBorders();

        allowedModules.forEach(module->{
            int slot = 10+allowedModules.indexOf(module);
            inv.setItem(slot,block().getModules().containsKey(module)
                    ? plugin.getItemManager().getItemFromType(module)
                    : getItem(Material.WHITE_STAINED_GLASS_PANE,"No Module"));
            inv.setItem(slot+9,getItem(Material.PAPER,module.getName()+" Module"));
        });

    }
}
