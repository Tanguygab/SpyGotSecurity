package io.github.tanguygab.spygotsecurity.modules;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.menus.modules.ListModuleMenu;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class ListModule extends SGSModule {

    private final boolean inverted;
    private final List<UUID> players;

    public ListModule(UUID uuid, boolean inverted) {
        this(uuid,inverted,new ArrayList<>());
    }

    public ListModule(UUID uuid, boolean inverted, List<UUID> players) {
        super(uuid);
        this.inverted = inverted;
        this.players = players;
    }

    public boolean contains(Player player) {
        return players.contains(player.getUniqueId());
    }

    @Override
    public SGSMenu getMenu(Player player) {
        return new ListModuleMenu(player,this);
    }
}
