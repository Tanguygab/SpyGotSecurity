package io.github.tanguygab.spygotsecurity;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.tanguygab.spygotsecurity.commands.*;
import io.github.tanguygab.spygotsecurity.database.DataManager;
import io.github.tanguygab.spygotsecurity.managers.*;
import io.github.tanguygab.spygotsecurity.listeners.*;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class SpyGotSecurity extends JavaPlugin {

    @Getter private static SpyGotSecurity instance;
    private final Map<String,SGSCommand> commands = new HashMap<>();

    @Getter private YamlDocument configuration;
    @Getter private BlockManager blockManager;
    @Getter private ItemManager itemManager;
    @Getter private DataManager dataManager;
    @Getter private InventoryListener inventoryListener;

    @Override
    public void onEnable() {
        instance = this;
        try {
            configuration = YamlDocument.create(new File(getDataFolder(),"config.yml"), Objects.requireNonNull(getResource("config.yml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        blockManager = new BlockManager(configuration);
        itemManager = new ItemManager(configuration);
        dataManager = new DataManager(this);

        PluginManager plm = getServer().getPluginManager();
        plm.registerEvents(inventoryListener = new InventoryListener(),this);
        plm.registerEvents(new BlockListener(this),this);
        plm.registerEvents(new ItemListener(this),this);

        commands.put("block",new BlockCommand(this));
        commands.put("item",new ItemCommand(this));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        dataManager.unload();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            Utils.send(sender,getHelpMessage());
            return true;
        }
        String arg = args[0];
        if (arg.equalsIgnoreCase("reload") && sender.isOp()) {
            onDisable();
            onEnable();
            Utils.send(sender,"&aReloaded!");
            return true;
        }
        if (!commands.containsKey(arg)) {
            Utils.send(sender,getHelpMessage());
            return true;
        }
        commands.get(arg).onCommand(sender,Arrays.copyOfRange(args,1,args.length));
        return true;
    }

    private String getHelpMessage() {
        return "&6&m                                        \n"
                + "&8[&7SpyGotSecurity&8] &7" + getDescription().getVersion() + "\n"
                + " &8- &f/sgs\n"
                + "   &8| &7Default help page\n"
                + " &8- &f/sgs item\n"
                + "   &8| &7Gives items\n"
                + " &8- &f/sgs block\n"
                + "   &8| &7Gives blocks\n"
                + " &8- &f/sgs reload\n"
                + "   &8| &7Reload the plugin\n"
                + "&6&m                                        ";
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        String arg = args.length > 0 ? args[0] : "";
        if (!commands.containsKey(arg)) return List.of("item","block","reload");
        args = Arrays.copyOfRange(args,1, args.length);
        return commands.get(arg).onTabComplete(sender,args);
    }
}
