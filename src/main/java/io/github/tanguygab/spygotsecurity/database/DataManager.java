package io.github.tanguygab.spygotsecurity.database;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.KeyPad;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.blocks.LockedContainer;
import io.github.tanguygab.spygotsecurity.database.serializers.LocationSerializer;
import io.github.tanguygab.spygotsecurity.database.serializers.lockedblocks.KeyPadSerializer;
import io.github.tanguygab.spygotsecurity.database.serializers.lockedblocks.LockedContainerSerializer;
import io.github.tanguygab.spygotsecurity.database.serializers.modules.*;
import io.github.tanguygab.spygotsecurity.modules.*;
import lombok.Getter;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataManager {

    private final SpyGotSecurity plugin;
    private final YamlDocument modules;
    private final YamlDocument lockedBlocks;
    private final YamlDocument reinforcedBlocks;
    @Getter private final static StandardSerializer serializer = StandardSerializer.getDefault();

    static {
        serializer.register(Location.class, new LocationSerializer());
        serializer.register(KeyPad.class, new KeyPadSerializer());
        serializer.register(LockedContainer.class, new LockedContainerSerializer());
        serializer.register(ListModule.class, new ListModuleSerializer());
        serializer.register(HarmingModule.class, new HarmingModuleSerializer());
        serializer.register(DisguiseModule.class, new DisguiseModuleSerializer());
    }

    @SuppressWarnings("unchecked")
    public DataManager(SpyGotSecurity plugin) {
        this.plugin = plugin;
        try {
            modules = YamlDocument.create(new File(plugin.getDataFolder(),"modules.yml"));
            List<SGSModule> modules = (List<SGSModule>) this.modules.getList("modules");
            modules.forEach(module->plugin.getItemManager().addModule(module));

            lockedBlocks = YamlDocument.create(new File(plugin.getDataFolder(),"locked-blocks.yml"));
            List<LockedBlock> lockedBlocks = (List<LockedBlock>) this.lockedBlocks.getList("locked-blocks");
            lockedBlocks.forEach(locked->plugin.getBlockManager().addLockedBlock(locked));

            reinforcedBlocks = YamlDocument.create(new File(plugin.getDataFolder(),"reinforced-blocks.yml"));
            Section reinforcedBlocks = this.reinforcedBlocks.getSection("reinforced-blocks");
            if (reinforcedBlocks != null)
                reinforcedBlocks.getKeys().forEach(owner->{
                    List<Location> locations = (List<Location>) reinforcedBlocks.getList(Route.from(owner));
                    locations.forEach(loc->plugin.getBlockManager().getReinforcedBlocks().put(loc.getBlock(), UUID.fromString((String) owner)));
                });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unload() {
        Map<UUID,List<Location>> blocks = new HashMap<>();
        plugin.getBlockManager().getReinforcedBlocks().forEach((block,owner)->blocks.computeIfAbsent(owner,k->new ArrayList<>()).add(block.getLocation()));
        reinforcedBlocks.set("reinforced-blocks",blocks);
        lockedBlocks.set("locked-blocks",new ArrayList<>(plugin.getBlockManager().getLockedBlocks().values()));
        modules.set("modules",new ArrayList<>(plugin.getItemManager().getModules().values()));
        try {
            reinforcedBlocks.save();
            lockedBlocks.save();
            modules.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
