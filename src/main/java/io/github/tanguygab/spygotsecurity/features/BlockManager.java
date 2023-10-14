package io.github.tanguygab.spygotsecurity.features;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.KeyPad;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.blocks.LockedContainer;
import io.github.tanguygab.spygotsecurity.blocks.SGSBlock;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
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

    public ItemStack getItem(SGSBlock block) {
        String type = block instanceof KeyPad ? "keypad" : block.getBlock().getType().toString().toLowerCase();
        if (type.contains("shulker_box")) type = "shulker_box";
        return getItemFromType(type);
    }

    public ItemStack getItemFromType(String type) {
        return switch (type) {
            case "keypad" -> getItem(Material.IRON_BLOCK,"&8&lKeypad","keypad");
            case "chest" -> getItem(Material.CHEST,"&6&lChest","container");
            case "shulker_box" -> getItem(Material.SHULKER_BOX,"&d&lShulker Box","container");
            case "barrel" -> getItem(Material.BARREL,"&6&lBarrel","container");
            case "hopper" -> getItem(Material.HOPPER,"&7&lHopper","container");
            case "furnace" -> getItem(Material.FURNACE,"&7&lFurnace","container");
            case "blast_furnace" -> getItem(Material.BLAST_FURNACE,"&8&lBlast Furnace","container");
            case "smoker" -> getItem(Material.SMOKER,"&6&lSmoker","container");
            default -> null;
        };
    }

    private ItemStack getItem(Material material, String name, String data) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Utils.color(name));
        meta.setLore(Utils.color("","&8Place and right-click me","&8to set a password!"));
        meta.getPersistentDataContainer().set(LOCKED_BLOCK, PersistentDataType.STRING,data);
        item.setItemMeta(meta);
        return item;
    }

}
