package io.github.tanguygab.spygotsecurity.blocks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class SGSBlock {

    protected final Block block;
    @Setter private UUID owner;

    public boolean isOwner(Player player) {
        return player != null && player.getUniqueId().equals(owner);
    }




}
