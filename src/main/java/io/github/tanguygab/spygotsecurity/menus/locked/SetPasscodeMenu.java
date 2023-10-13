package io.github.tanguygab.spygotsecurity.menus.locked;

import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.utils.PasswordUtils;
import org.bukkit.entity.Player;

public class SetPasscodeMenu extends PasscodeMenu {

    public SetPasscodeMenu(LockedBlock block, Player player) {
        super(block, player, "Set your Passcode");
    }

    @Override
    protected byte[] getSalt() {
        return PasswordUtils.newSalt();
    }

    @Override
    protected void onClick(byte[] password, byte[] salt) {
        block.onPasswordSet(player,password,salt);
    }
}
