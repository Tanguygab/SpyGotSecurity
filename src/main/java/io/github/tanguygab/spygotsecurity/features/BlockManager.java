package io.github.tanguygab.spygotsecurity.features;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockManager {

    @Getter @Accessors(fluent = true)
    private final boolean usePasswords;
    private final Map<Block, LockedBlock> lockedBlocks = new HashMap<>();

    public BlockManager(SpyGotSecurity plugin) {
        usePasswords = plugin.getConfiguration().getString("locked-blocks.method","PASSCODE").equalsIgnoreCase("password");
    }

    public LockedBlock getLockedBlock(Block block) {
        return lockedBlocks.get(block);
    }

    public boolean isLockedBlock(Block block) {
        return lockedBlocks.containsKey(block);
    }

    public void removedLockedBlock(Block block) {
        lockedBlocks.remove(block);
    }

    public void addLockedBlock(LockedBlock block) {
        lockedBlocks.put(block.getBlock(),block);
    }
}
