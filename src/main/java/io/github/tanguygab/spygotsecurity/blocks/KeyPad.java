package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.menus.configurable.KeyPadConfigMenu;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Setter @Getter
public class KeyPad extends LockedBlock {

    private int tickDelay;

    public KeyPad(Block block, Player owner) {
        this(block, owner.getUniqueId(), null, null, null,20);
    }

    public KeyPad(Block block, UUID owner, List<SGSModule> modules, byte[] password, byte[] salt, int tickDelay) {
        super(block, owner, modules, password, salt);
        this.tickDelay = tickDelay;
    }

    @Override
    public void onSuccess(Player player) {
        block.setType(Material.REDSTONE_BLOCK);
        plugin().getServer().getScheduler().runTaskLater(plugin(),this::setBlock,tickDelay);
    }

    @Override
    public void openConfigMenu(Player player) {
        new KeyPadConfigMenu(player,this).open();
    }
}
