package io.github.tanguygab.spygotsecurity;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import io.github.tanguygab.spygotsecurity.commands.*;
import io.github.tanguygab.spygotsecurity.database.DataManager;
import io.github.tanguygab.spygotsecurity.features.BlockManager;
import io.github.tanguygab.spygotsecurity.listeners.*;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SpyGotSecurity extends JavaPlugin {

    @Getter private static SpyGotSecurity instance;
    private final Map<String,SGSCommand> commands = new HashMap<>();

    @Getter private YamlDocument configuration;
    @Getter private BlockManager blockManager;
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
        blockManager = new BlockManager(this);
        (dataManager = new DataManager(this)).load();

        PluginManager plm = getServer().getPluginManager();
        plm.registerEvents(inventoryListener = new InventoryListener(),this);
        plm.registerEvents(new BlockListener(this),this);

        commands.put("get",new GetCommand(this));
        commands.put("list",new ListCommand(this));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        dataManager.unload();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
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
