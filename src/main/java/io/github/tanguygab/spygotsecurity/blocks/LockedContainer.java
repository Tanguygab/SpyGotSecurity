package io.github.tanguygab.spygotsecurity.blocks;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LockedContainer extends LockedBlock {

    public LockedContainer(Block block, Player owner) {
        this(block, owner.getUniqueId(), null, null);
    }

    public LockedContainer(Block block, UUID owner, byte[] password, byte[] salt) {
        super(block, owner, password, salt);
    }

    @Override
    public void onSuccess(Player player) {
        if (block.getState() instanceof Container container) {
            player.openInventory(container.getInventory());
        }
    }

}
