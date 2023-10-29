package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class KeyPad extends LockedBlock {

    public KeyPad(Block block, Player owner) {
        this(block, owner.getUniqueId(), null, null, null);
    }

    public KeyPad(Block block, UUID owner, List<SGSModule> modules, byte[] password, byte[] salt) {
        super(block, owner, modules, password, salt);
    }

    @Override
    public void onSuccess(Player player) {
        block.setType(Material.REDSTONE_BLOCK);
        plugin().getServer().getScheduler().runTaskLater(plugin(),this::setBlock,20);
    }

}
