package io.github.tanguygab.spygotsecurity.features;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.KeyPad;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BlockManager {

    @Getter private final Map<Block,KeyPad> keypads = new HashMap<>();

    public BlockManager(SpyGotSecurity plugin) {

    }

    public void addKeyPad(Block block, Player player) {
        keypads.put(block,new KeyPad(block,player.getUniqueId()));
    }
    public KeyPad getKeyPad(Block block) {
        return keypads.get(block);
    }

    public boolean isKeyPad(Block block) {
        return keypads.containsKey(block);
    }

    public void removeKeyPad(Block block) {
        keypads.remove(block);
    }

}
