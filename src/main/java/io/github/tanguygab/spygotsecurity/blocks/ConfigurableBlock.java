package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.modules.DisguiseModule;
import io.github.tanguygab.spygotsecurity.modules.ListModule;
import io.github.tanguygab.spygotsecurity.modules.ModuleType;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public abstract class ConfigurableBlock extends SGSBlock {

    private final Material originalBlock = block.getType();
    private final Map<ModuleType,SGSModule> modules = new HashMap<>();

    public ConfigurableBlock(Block block, UUID owner, List<SGSModule> modules) {
        super(block, owner);
        if (modules == null) return;
        modules.forEach(module->this.modules.put(module.getType(),module));
    }

    public void addModule(Player player, SGSModule module, ItemStack moduleItem) {
        SGSModule previous = modules.put(module.getType(),module);
        player.getInventory().remove(moduleItem);
        if (previous != null) removeModule(player,previous);
        disguiseBlock();
    }

    public void removeModule(Player player, SGSModule module) {
        ItemStack item = plugin().getItemManager().getItemFromModule(module);
        if (modules.get(module.getType()) == module) modules.remove(module.getType());
        resetBlock();
        if (player.getInventory().addItem(item).isEmpty()) return;
        Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(),item);
    }

    public boolean isBlacklisted(Player player) {
        if (!modules.containsKey(ModuleType.BLACKLIST)) return false;
        return ((ListModule)modules.get(ModuleType.BLACKLIST)).contains(player);
    }

    public boolean isWhitelisted(Player player) {
        if (!modules.containsKey(ModuleType.WHITELIST)) return false;
        return ((ListModule)modules.get(ModuleType.WHITELIST)).contains(player);
    }

    public void harmPlayer(Player player) {
        if (modules.containsKey(ModuleType.HARMING))
            plugin().getServer().getScheduler().runTask(plugin(),()->player.damage(plugin().getItemManager().HARMING_DAMAGE));
    }

    protected SpyGotSecurity plugin() {
        return SpyGotSecurity.getInstance();
    }

    public void disguiseBlock() {
        if (!modules.containsKey(ModuleType.DISGUISE)) return;
        DisguiseModule module = (DisguiseModule) modules.get(ModuleType.DISGUISE);
        if (module.getType() != null) block.setType(module.getMaterial());
    }
    public void resetBlock() {
        if (modules.containsKey(ModuleType.DISGUISE))
            block.setType(originalBlock);
    }
}
