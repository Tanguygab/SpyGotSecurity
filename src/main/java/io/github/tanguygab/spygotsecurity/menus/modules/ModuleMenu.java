package io.github.tanguygab.spygotsecurity.menus.modules;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import org.bukkit.entity.Player;

public abstract class ModuleMenu extends SGSMenu {

    protected SGSModule module;

    public ModuleMenu(Player player, SGSModule module) {
        super(player);
        this.module = module;
    }

}
