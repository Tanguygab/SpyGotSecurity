package io.github.tanguygab.spygotsecurity.menus.modules;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import org.bukkit.entity.Player;

public abstract class ModuleMenu<SGSModule> extends SGSMenu {

    protected SGSModule module;

    public ModuleMenu(Player player, SGSModule module) {
        super(player);
        this.module = module;
    }

}
