package io.github.tanguygab.spygotsecurity.menus.modules;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import org.bukkit.entity.Player;

public abstract class ModuleMenu<T> extends SGSMenu {

    protected T module;

    public ModuleMenu(Player player, T module) {
        super(player);
        this.module = module;
    }

}
