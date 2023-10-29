package io.github.tanguygab.spygotsecurity.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.*;
import io.github.tanguygab.spygotsecurity.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@Getter
public class BlockManager {

    private static final NamespacedKey LOCKED_BLOCK = new NamespacedKey(SpyGotSecurity.getInstance(),"locked-block");
    public static final NamespacedKey REINFORCED = new NamespacedKey(SpyGotSecurity.getInstance(),"reinforced");

    private final Map<Block, LockedBlock> lockedBlocks = new HashMap<>();
    private final Map<Block, UUID> reinforcedBlocks = new HashMap<>();

    public final boolean USE_PASSWORD;
    public final boolean KEYPAD_ENABLED;
    private final List<Material> allowedContainers = new ArrayList<>();
    public final boolean REINFORCED_BLOCKS_ENABLED;
    private final List<Material> disabledReinforcedBlocks = new ArrayList<>();

    public BlockManager(YamlDocument config) {
        USE_PASSWORD = config.getString("locked-blocks.method","PASSCODE").equalsIgnoreCase("password");
        KEYPAD_ENABLED = config.getBoolean("locked-blocks.keypad",true);
        allowContainer(config,"chest",Material.CHEST);
        allowContainer(config,"shulker-box",Material.SHULKER_BOX);
        allowContainer(config,"barrel",Material.BARREL);
        allowContainer(config,"hopper",Material.HOPPER);
        allowContainer(config,"furnaces",Material.FURNACE,Material.SMOKER,Material.BLAST_FURNACE);

        REINFORCED_BLOCKS_ENABLED = config.getBoolean("reinforced-blocks.enabled",true);
        config.getStringList("reinforced-blocks.disabled-blocks").forEach(mat->{
            Material material = Material.getMaterial(mat);
            if (material != null) disabledReinforcedBlocks.add(material);
        });
    }

    public void unload() {
        lockedBlocks.values().forEach(ConfigurableBlock::resetBlock);
    }

    private void allowContainer(YamlDocument config, String name, Material... materials) {
        if (config.getBoolean("locked-blocks.containers"+name,true))
            allowedContainers.addAll(List.of(materials));
    }

    public void addLockedBlock(LockedBlock block) {
        lockedBlocks.put(block.getBlock(),block);
        block.setBlock();
    }

    public LockedBlock getBlockFromItem(Block block, Player player, ItemStack item) {
        String type = ItemUtils.getTypeFromItem(LOCKED_BLOCK,item);
        if (type == null) return null;
        return switch (type) {
            case "keypad" -> KEYPAD_ENABLED ? new KeyPad(block,player) : null;
            case "container" -> allowedContainers.contains(block.getType().toString().contains("SHULKER_BOX")
                    ? Material.SHULKER_BOX : block.getType()) ? new LockedContainer(block,player) : null;
            //case "keycard-scanner" -> null;
            default -> null;
        };
    }

    public boolean canReinforce(Material material) {
        return material.isBlock() && !material.isInteractable() && !disabledReinforcedBlocks.contains(material);
    }
    public boolean isReinforcedBlockItem(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && Boolean.TRUE.equals(meta.getPersistentDataContainer().get(BlockManager.REINFORCED, PersistentDataType.BOOLEAN));
    }

    public ItemStack getItem(SGSBlock block) {
        String type = block instanceof KeyPad ? "keypad" : block.getBlock().getType().toString().toLowerCase();
        if (type.contains("shulker_box")) type = "shulker_box";
        return getItemFromType(type);
    }

    public ItemStack getItemFromType(String type) {
        return switch (type) {
            case "keypad" -> KEYPAD_ENABLED ? getItem(Material.IRON_BLOCK,"&8&lKeypad","keypad") : null;
            case "chest" -> getItem(Material.CHEST,"&6&lLocked Chest","container");
            case "shulker_box" -> getItem(Material.SHULKER_BOX,"&d&lLocked Shulker Box","container");
            case "barrel" -> getItem(Material.BARREL,"&6&lLocked Barrel","container");
            case "hopper" -> getItem(Material.HOPPER,"&7&lLocked Hopper","container");
            case "furnace" -> getItem(Material.FURNACE,"&7&lLocked Furnace","container");
            case "blast_furnace" -> getItem(Material.BLAST_FURNACE,"&8&lLocked Blast Furnace","container");
            case "smoker" -> getItem(Material.SMOKER,"&6&lLocked Smoker","container");
            default -> null;
        };
    }

    private ItemStack getItem(Material material, String name, String data) {
        if (material != Material.IRON_BLOCK && !allowedContainers.contains(material)) return null;
        return ItemUtils.getItem(material,name,LOCKED_BLOCK,data,"","&8Place and right-click me","&8to set a password!");
    }

}
