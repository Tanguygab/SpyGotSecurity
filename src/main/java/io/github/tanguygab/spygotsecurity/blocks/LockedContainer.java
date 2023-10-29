package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.UUID;

public class LockedContainer extends LockedBlock {

    private final Inventory inventory;

    public LockedContainer(Block block, Player owner) {
        this(block, owner.getUniqueId(), null, null, null);
    }

    public LockedContainer(Block block, UUID owner, List<SGSModule> modules, byte[] password, byte[] salt) {
        super(block, owner, modules, password, salt);
        inventory = block.getState() instanceof Container container ? container.getInventory() : null;
    }

    @Override
    public void onSuccess(Player player) {
        if (inventory != null) player.openInventory(inventory);
    }

}
