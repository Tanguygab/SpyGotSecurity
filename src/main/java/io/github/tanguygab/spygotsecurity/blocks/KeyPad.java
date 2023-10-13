package io.github.tanguygab.spygotsecurity.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.UUID;

public class KeyPad extends LockedBlock {

    public KeyPad(Block block, Player owner) {
        this(block, owner.getUniqueId(), null, null);
    }

    public KeyPad(Block block, UUID owner, byte[] password, byte[] salt) {
        super(block, owner, password, salt);
    }

    @Override
    public void onSuccess(Player player) {
        runSync(()->{
            block.setType(Material.REDSTONE_BLOCK);
            plugin().getServer().getScheduler().runTaskLater(plugin(),()->block.setType(Material.IRON_BLOCK),20);
        });
    }

}
