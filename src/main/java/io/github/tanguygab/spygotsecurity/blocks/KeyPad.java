package io.github.tanguygab.spygotsecurity.blocks;

import org.bukkit.block.Block;

import java.util.UUID;

public class KeyPad extends PasswordProtectedBlock {

    public KeyPad(Block block, UUID owner, byte[] password, byte[] salt) {
        super(block, owner, password, salt);
    }

    public KeyPad(Block block, UUID owner) {
        this(block, owner, null, null);
    }


}
