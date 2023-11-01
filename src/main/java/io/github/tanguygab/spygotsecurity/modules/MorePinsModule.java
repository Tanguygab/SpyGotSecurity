package io.github.tanguygab.spygotsecurity.modules;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class MorePinsModule extends SGSModule {

    private final int level;

    public MorePinsModule(UUID uuid, int level) {
        super(uuid, ModuleType.MORE_PINS);
        this.level = level;
    }

    @Override
    public SGSMenu getMenu(Player player) {
        return null;
    }

}
