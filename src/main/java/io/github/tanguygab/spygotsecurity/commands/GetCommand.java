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
        String item = args.length > 0 ? args[0] : "";
        switch (item) {
            case "keypad" -> {
                if (!(sender instanceof Player player)) {
                    send(sender,"&cYou must be a player!");
                    return;
                }

                ItemStack keypad = new ItemStack(Material.IRON_BLOCK);
                ItemMeta meta = keypad.getItemMeta();
                assert meta != null;
                meta.setDisplayName(color("&8&lKeypad"));
                meta.setLore(color("","&8Place and right-click me","&8to set a password!"));
                meta.getPersistentDataContainer().set(BlockManager.LOCKED_BLOCK, PersistentDataType.STRING,"keypad");
                keypad.setItemMeta(meta);

                player.getInventory().addItem(keypad);
                send(sender,"&aWoaw, a keypad!");
            }
            case "idk" -> {}
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
