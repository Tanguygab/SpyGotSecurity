package io.github.tanguygab.spygotsecurity.menus;

import io.github.tanguygab.spygotsecurity.blocks.PasswordProtectedBlock;
import io.github.tanguygab.spygotsecurity.utils.PasswordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PPBMenu extends SGSMenu {

    private final PasswordProtectedBlock block;

    public PPBMenu(PasswordProtectedBlock block, Player player) {
        super(player);
        this.block = block;
        inv = plugin.getServer().createInventory(null, 54, "Select your Password");
    }

    @Override
    public void open() {
        inv.addItem(getItem(Material.PLAYER_HEAD,"Hello"));
        inv.setItem(4,getItem(Material.LIME_WOOL,"Confirm"));
        player.openInventory(inv);
    }

    @Override
    public void onClick(ItemStack item, int slot, ClickType click) {
        switch (slot) {
            case 0 -> player.sendMessage("Hey");
            case 4 -> {
                byte[] salt = PasswordUtils.newSalt();
                PasswordUtils.asyncHash("012345",salt,password->{
                    block.setPassword(password);
                    block.setSalt(salt);
                    player.sendMessage("New password set!");
                    PasswordUtils.asyncHash("012345",block.getSalt(),password1-> player.sendMessage(""+Arrays.equals(password,password1)));
                });
            }
        }
    }
}
