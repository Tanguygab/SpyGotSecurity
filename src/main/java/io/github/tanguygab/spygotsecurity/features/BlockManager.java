package io.github.tanguygab.spygotsecurity.features;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockManager {

    private final Map<Block, LockedBlock> lockedBlocks = new HashMap<>();

    public BlockManager(SpyGotSecurity plugin) {

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
