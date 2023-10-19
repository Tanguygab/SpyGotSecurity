package io.github.tanguygab.spygotsecurity.modules;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HarmingModule extends SGSModule {

    public HarmingModule(UUID uuid) {
        super(uuid, ModuleType.HARMING);
    }

    @Override
    public SGSMenu getMenu(Player player) {
        return null;
    }
}
