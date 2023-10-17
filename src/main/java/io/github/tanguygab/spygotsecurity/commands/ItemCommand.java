package io.github.tanguygab.spygotsecurity.commands;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.modules.ModuleType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemCommand extends SGSCommand {

    public ItemCommand(SpyGotSecurity plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            send(sender,"&cYou must be a player!");
            return;
        }
        String type = args.length > 0 ? args[0] : "";
        ItemStack item = plugin.getItemManager().getItemFromType(ModuleType.get(type));
        if (item == null) {
            send(sender,"&cInvalid item!");
            return;
        }
        player.getInventory().addItem(item);
        send(sender,"&aWoaw, a "+type+"!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return args.length <= 1 ? List.of("keypad","chest","shulker_box","barrel","hopper","furnace","blast_furnace", "smoker") : null;
    }
}
