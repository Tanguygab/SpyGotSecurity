package io.github.tanguygab.spygotsecurity.menus.locked;

import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import org.bukkit.entity.Player;

public class CheckPasscodeMenu extends PasscodeMenu {

    public CheckPasscodeMenu(LockedBlock block, Player player) {
        super(block, player, "Enter your Passcode");
    }

    @Override
    protected byte[] getSalt() {
        return block.getSalt();
    }

    @Override
    protected void onClick(byte[] passcode, byte[] salt) {
        block.onPasswordCheck(player, passcode);
    }
}
