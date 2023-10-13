package io.github.tanguygab.spygotsecurity.menus.locked;

import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CheckPasscodeMenu extends PasscodeMenu {

    public CheckPasscodeMenu(LockedBlock block, Player player) {
        super(block, player, "Enter your Passcode");
    }

    @Override
    protected byte[] getSalt() {
        return block.getSalt();
    }

    @Override
    protected void onClick(byte[] password, byte[] salt) {
        if (Arrays.equals(password,block.getPassword())) {
            block.onSuccess(player);
            Utils.send(player,"&aCorrect passcode!");
            return;
        }
        Utils.send(player,"&cWrong passcode!");
    }
}
