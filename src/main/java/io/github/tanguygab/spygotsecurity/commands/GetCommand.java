package io.github.tanguygab.spygotsecurity.commands;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.features.BlockManager;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GetCommand extends SGSCommand {

    public GetCommand(SpyGotSecurity plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            send(sender,"&cYou must be a player!");
            return;
        }
        String type = args.length > 0 ? args[0] : "";
        ItemStack item = switch (type) {
            case "keypad" -> getItem(Material.IRON_BLOCK,"&8&lKeypad","keypad");
            case "chest" -> getItem(Material.CHEST,"&6&lChest","container");
            case "shulker" -> getItem(Material.SHULKER_BOX,"&d&lShulker Box","container");
            case "barrel" -> getItem(Material.BARREL,"&6&lBarrel","container");
            case "hopper" -> getItem(Material.HOPPER,"&7&lHopper","container");
            case "furnace" -> getItem(Material.FURNACE,"&7&lFurnace","container");
            case "blast_furnace" -> getItem(Material.BLAST_FURNACE,"&8&lBlast Furnace","container");
            case "smoker" -> getItem(Material.SMOKER,"&6&lSmoker","container");
            default -> null;
        };
        if (item == null) {
            send(sender,"&cInvalid item!");
            return;
        }
        player.getInventory().addItem(item);
        send(sender,"&aWoaw, a "+type+"!");
    }

    private ItemStack getItem(Material material, String name, String data) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(color(name));
        meta.setLore(color("","&8Place and right-click me","&8to set a password!"));
        meta.getPersistentDataContainer().set(BlockManager.LOCKED_BLOCK, PersistentDataType.STRING,data);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return args.length <= 1 ? List.of("keypad","chest","shulker","barrel","hopper","furnace","blast_furnace","smoker") : null;
    }
}
