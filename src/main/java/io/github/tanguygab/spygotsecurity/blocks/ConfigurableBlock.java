package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.modules.ListModule;
import io.github.tanguygab.spygotsecurity.modules.ModuleType;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public abstract class ConfigurableBlock extends SGSBlock {

    private final Map<ModuleType,SGSModule> modules = new HashMap<>();

    public ConfigurableBlock(Block block, UUID owner, List<SGSModule> modules) {
        super(block, owner);
        if (modules == null) return;
        modules.forEach(module->this.modules.put(module.getType(),module));
    }

    public boolean hasModule(SGSModule module) {
        return modules.containsKey(module.getType());
    }

    public boolean isBlacklisted(Player player) {
        if (!modules.containsKey(ModuleType.BLACKLIST)) return false;
        return ((ListModule)modules.get(ModuleType.BLACKLIST)).contains(player);
    }

    public boolean isWhitelisted(Player player) {
        if (!modules.containsKey(ModuleType.WHITELIST)) return false;
        return ((ListModule)modules.get(ModuleType.WHITELIST)).contains(player);
    }
}
