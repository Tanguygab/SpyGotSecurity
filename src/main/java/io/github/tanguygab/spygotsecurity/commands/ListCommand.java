package io.github.tanguygab.spygotsecurity.commands;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.utils.NamespacedKeys;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ListCommand extends SGSCommand {

    public ListCommand(SpyGotSecurity plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        String item = args.length > 0 ? args[0] : "";
        switch (item) {
            case "keypad" -> {
                send(sender,"&8Keypads&7: &f"+plugin.getBlockManager().getKeypads().size());
            }
            case "idk" -> {}
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
