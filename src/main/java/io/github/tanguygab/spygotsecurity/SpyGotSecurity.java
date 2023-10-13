package io.github.tanguygab.spygotsecurity;

import io.github.tanguygab.spygotsecurity.commands.*;
import io.github.tanguygab.spygotsecurity.features.BlockManager;
import io.github.tanguygab.spygotsecurity.listeners.*;
import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class SpyGotSecurity extends JavaPlugin {

    @Getter private static SpyGotSecurity instance;
    private final Map<String,SGSCommand> commands = new HashMap<>();

    @Getter private BlockManager blockManager;

    @Getter private InventoryListener inventoryListener;

    @Override
    public void onEnable() {
        instance = this;

        blockManager = new BlockManager(this);

        PluginManager plm = getServer().getPluginManager();
        plm.registerEvents(inventoryListener = new InventoryListener(),this);
        plm.registerEvents(new BlockListener(this),this);

        commands.put("get",new GetCommand(this));
        commands.put("list",new ListCommand(this));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("WIP");
            return true;
        }
        String arg = args[0];
        if (!commands.containsKey(arg)) {
            sender.sendMessage("WIP");
            return true;
        }
        commands.get(arg).onCommand(sender,Arrays.copyOfRange(args,1,args.length));
        return true;
    }
}
