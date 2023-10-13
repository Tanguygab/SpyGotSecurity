package io.github.tanguygab.spygotsecurity.menus.locked;

import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.utils.PasswordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class PasscodeMenu extends SGSMenu {

    protected final LockedBlock block;
    private final List<String> passcode = new ArrayList<>();

    public PasscodeMenu(LockedBlock block, Player player, String title) {
        super(player);
        this.block = block;
        inv = plugin.getServer().createInventory(null, 54, title);
    }

    @Override
    public void onOpen() {
        inv.addItem(getItem(Material.PLAYER_HEAD,"Hello"));
        inv.setItem(4,getItem(Material.LIME_WOOL,"Confirm"));
    }

    @Override
    public void onClick(ItemStack item, int slot, ClickType click) {
        switch (slot) {
            case 0 -> player.sendMessage("Hey");
            case 4 -> {
                passcode.add("a");
                player.closeInventory();
                byte[] salt = getSalt();
                PasswordUtils.asyncHash(String.join("a",passcode),salt,passcode->onClick(passcode,salt));
            }
        }
    }

    protected abstract byte[] getSalt();
    protected abstract void onClick(byte[] password, byte[] salt);
}
