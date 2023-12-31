package io.github.tanguygab.spygotsecurity.blocks;

import io.github.tanguygab.spygotsecurity.managers.ItemManager;
import io.github.tanguygab.spygotsecurity.menus.LockPickMenu;
import io.github.tanguygab.spygotsecurity.menus.configurable.LockedBlockConfigMenu;
import io.github.tanguygab.spygotsecurity.menus.locked.CheckPasscodeMenu;
import io.github.tanguygab.spygotsecurity.menus.locked.SetPasscodeMenu;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import io.github.tanguygab.spygotsecurity.utils.ItemUtils;
import io.github.tanguygab.spygotsecurity.utils.PasswordUtils;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import lombok.Getter;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.GameMode;
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
public abstract class LockedBlock extends ConfigurableBlock {

    private byte[] password;
    private byte[] salt;

    public LockedBlock(Block block, UUID owner, List<SGSModule> modules, byte[] password, byte[] salt) {
        super(block, owner, modules);
        this.password = password;
        this.salt = salt;
    }

    public abstract void onSuccess(Player player);
    public void openConfigMenu(Player player) {
        new LockedBlockConfigMenu(player,this).open();
    }

    public void onClick(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        String itemType = ItemUtils.getTypeFromItem(ItemManager.ITEM,item);
        if (itemType != null && password != null) switch (itemType) {
            case "codebreaker" -> {
                ItemManager im = plugin().getItemManager();
                int uses = im.getCodeBreakerUses(item);
                if (uses != -1 && player.getGameMode() != GameMode.CREATIVE) {
                    if (--uses < 1) player.getInventory().remove(item);
                    else im.setCodebreakerUses(item, uses);
                }
                if (player.getGameMode() == GameMode.CREATIVE || Utils.RANDOM.nextDouble() < im.CODEBREAKER_CHANCE) {
                    onSuccess(player);
                    return;
                }
            }
            case "lockpick" -> {
                new LockPickMenu(player,this).open();
                return;
            }
        }
        if (!isOwner(player)) {
            if (password == null) {
                Utils.send(player,"&cThis block hasn't been configured yet!");
                return;
            }
            if (isBlacklisted(player)) {
                Utils.send(player,"&4You're blacklisted from using this!");
                return;
            }
        } else if (player.isSneaking()) {
            openConfigMenu(player);
            return;
        }

        if (password != null && isWhitelisted(player)) {
            Utils.send(player,"&aYou're whitelisted!");
            onSuccess(player);
            return;
        }

        if (plugin().getBlockManager().USE_PASSWORD) {
            if (password == null) {
                byte[] salt = PasswordUtils.newSalt();
                openAnvilGUI(player, "Set your password", salt, password -> onPasswordSet(player, password, salt));
                return;
            }
            openAnvilGUI(player,"Enter your password",salt, password -> onPasswordCheck(player,password));
            return;
        }
        (password == null ? new SetPasscodeMenu(player,this) : new CheckPasscodeMenu(player,this)).open();
    }

    public void onPasswordSet(Player player, byte[] password, byte[] salt) {
        this.password = password;
        this.salt = salt;
        Utils.send(player,"New passcode set!");
    }
    public void onPasswordCheck(Player player, byte[] password) {
        if (Arrays.equals(password,this.password)) {
            plugin().getServer().getScheduler().runTask(plugin(),()->onSuccess(player));
            Utils.send(player,"&aCorrect passcode!");
            return;
        }
        Utils.send(player,"&cWrong passcode!");
        harmPlayer(player);
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

}
