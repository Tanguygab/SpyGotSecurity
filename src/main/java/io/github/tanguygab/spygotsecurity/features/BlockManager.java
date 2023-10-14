package io.github.tanguygab.spygotsecurity.features;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.KeyPad;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.blocks.LockedContainer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BlockManager {

    public static final NamespacedKey LOCKED_BLOCK = new NamespacedKey(SpyGotSecurity.getInstance(),"locked-block");

    @Accessors(fluent = true)
    private final boolean usePasswords;
    private final Map<Block, LockedBlock> lockedBlocks = new HashMap<>();

    public BlockManager(SpyGotSecurity plugin) {
        usePasswords = plugin.getConfiguration().getString("locked-blocks.method","PASSCODE").equalsIgnoreCase("password");
    }

    public void addLockedBlock(LockedBlock block) {
        lockedBlocks.put(block.getBlock(),block);
    }

    public LockedBlock getBlockFromItem(Block block, Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(BlockManager.LOCKED_BLOCK, PersistentDataType.STRING)) return null;
        String type = meta.getPersistentDataContainer().get(BlockManager.LOCKED_BLOCK, PersistentDataType.STRING);
        if (type == null) return null;
        return switch (type) {
            case "keypad" -> new KeyPad(block,player);
            case "container" -> new LockedContainer(block,player);
            //case "keycard-scanner" -> null;
            default -> null;
        };
    }

}
