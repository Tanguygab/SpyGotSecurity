package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.menus.PPBMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;

@Setter
@Getter
public abstract class PasswordProtectedBlock extends SGSBlock {

    private byte[] password;
    private byte[] salt;

    public PasswordProtectedBlock(Block block, UUID owner, byte[] password, byte[] salt) {
        super(block, owner);
        this.password = password;
        this.salt = salt;
    }

    public void openMenu(Player player) {
        SpyGotSecurity.getInstance().getInventoryListener().open(player, new PPBMenu(this,player));
    }

}
