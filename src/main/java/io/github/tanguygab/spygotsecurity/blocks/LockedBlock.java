package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.menus.locked.CheckPasscodeMenu;
import io.github.tanguygab.spygotsecurity.menus.locked.PasscodeMenu;
import io.github.tanguygab.spygotsecurity.menus.locked.SetPasscodeMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;
import static io.github.tanguygab.spygotsecurity.utils.Utils.*;

@Setter
@Getter
public abstract class LockedBlock extends SGSBlock {

    private byte[] password;
    private byte[] salt;

    public LockedBlock(Block block, UUID owner, byte[] password, byte[] salt) {
        super(block, owner);
        this.password = password;
        this.salt = salt;
    }

    public abstract void onSuccess(Player player);

    public void onClick(Player player) {
        if (!isOwner(player) && password == null) {
            send(player,"&cThis keypad hasn't been configured yet!");
            return;
        }
        openMenu(player, password == null ? new SetPasscodeMenu(this,player) : new CheckPasscodeMenu(this,player));
    }

    public void openMenu(Player player, SGSMenu menu) {
        plugin().getInventoryListener().open(player, menu);
    }

    protected SpyGotSecurity plugin() {
        return SpyGotSecurity.getInstance();
    }
    protected void runSync(Runnable run) {
        plugin().getServer().getScheduler().runTask(plugin(),run);
    }

}
