package io.github.tanguygab.spygotsecurity.modules;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.menus.modules.ListModuleMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class ListModule extends SGSModule {

    private final boolean inverted;
    @Setter private boolean publik;
    private final List<UUID> players;

    public ListModule(UUID uuid, boolean inverted) {
        this(uuid,inverted,false,new ArrayList<>());
    }

    public ListModule(UUID uuid, boolean inverted, boolean publik, List<UUID> players) {
        super(uuid);
        this.inverted = inverted;
        this.publik = publik;
        this.players = players;
    }

    public boolean contains(Player player) {
        return publik || players.contains(player.getUniqueId());
    }

    @Override
    public SGSMenu getMenu(Player player) {
        return new ListModuleMenu(player,this);
    }
}
