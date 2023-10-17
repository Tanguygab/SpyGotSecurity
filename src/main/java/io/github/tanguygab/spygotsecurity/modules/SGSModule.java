package io.github.tanguygab.spygotsecurity.modules;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class SGSModule {

    private final UUID uuid;
    private final ModuleType type;

    public abstract SGSMenu getMenu(Player player);

}
