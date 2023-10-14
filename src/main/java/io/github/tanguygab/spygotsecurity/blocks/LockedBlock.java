package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.menus.locked.CheckPasscodeMenu;
import io.github.tanguygab.spygotsecurity.menus.locked.SetPasscodeMenu;
import io.github.tanguygab.spygotsecurity.utils.MultiBlockUtils;
import io.github.tanguygab.spygotsecurity.utils.PasswordUtils;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import lombok.Getter;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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
            Utils.send(player,"&cThis keypad hasn't been configured yet!");
            return;
        }
        if (plugin().getBlockManager().usePasswords()) {
            if (password == null) {
                byte[] salt = PasswordUtils.newSalt();
                openAnvilGUI(player, "Set your password", salt, password -> onPasswordSet(player, password, salt));
                return;
            }
            openAnvilGUI(player,"Enter your password",salt, password -> onPasswordCheck(player,password));
            return;
        }
        openMenu(player, password == null ? new SetPasscodeMenu(this,player) : new CheckPasscodeMenu(this,player));
    }

    public void onPasswordSet(Player player, byte[] password, byte[] salt) {
        this.password = password;
        this.salt = salt;
        Utils.send(player,"New passcode set!");
        plugin().getServer().getScheduler().runTask(plugin(),()->{
            LockedBlock other = plugin().getBlockManager().getLockedBlocks().get(MultiBlockUtils.getSide(block));
            if (MultiBlockUtils.isMultiBlock(this, other))
                syncPasswordTo(other);
        });
    }
    public void onPasswordCheck(Player player, byte[] password) {
        if (Arrays.equals(password,this.password)) {
            plugin().getServer().getScheduler().runTask(plugin(),()->onSuccess(player));
            Utils.send(player,"&aCorrect passcode!");
            return;
        }
        Utils.send(player,"&cWrong passcode!");
    }

    public void openMenu(Player player, SGSMenu menu) {
        plugin().getInventoryListener().open(player, menu);
    }

    private void openAnvilGUI(Player player, String title, byte[] salt, Consumer<byte[]> onClick) {
        new AnvilGUI.Builder()
                .plugin(plugin())
                .title(title)
                .text("Your Password")
                .itemLeft(new ItemStack(Material.PAPER))
                .onClickAsync((slot, gui) -> CompletableFuture.supplyAsync(() -> {
                    PasswordUtils.asyncHash(gui.getText(), salt, onClick);
                    return List.of(AnvilGUI.ResponseAction.close());
                }))
                .open(player);
    }

    protected SpyGotSecurity plugin() {
        return SpyGotSecurity.getInstance();
    }

    public void syncPasswordTo(LockedBlock side) {
        side.password = password;
        side.salt = salt;
    }

}
