package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.modules.ListModule;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class ConfigurableBlock extends SGSBlock {

    private final List<SGSModule> modules;

    public ConfigurableBlock(Block block, UUID owner, List<SGSModule> modules) {
        super(block, owner);
        this.modules = modules == null ? new ArrayList<>() : modules;
    }

    public boolean isBlacklisted(Player player) {
        for (SGSModule module : modules) {
            if (module instanceof ListModule m && m.isInverted())
                return m.contains(player);
        }
        return false;
    }

    public boolean isWhitelisted(Player player) {
        for (SGSModule module : modules) {
            if (module instanceof ListModule m && !m.isInverted())
                return m.contains(player);
        }
        return false;
    }

}
