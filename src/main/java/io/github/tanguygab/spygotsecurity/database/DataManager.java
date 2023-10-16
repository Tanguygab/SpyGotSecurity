package io.github.tanguygab.spygotsecurity.database;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.KeyPad;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.blocks.LockedContainer;
import io.github.tanguygab.spygotsecurity.database.serializers.*;
import io.github.tanguygab.spygotsecurity.database.serializers.lockedblocks.KeyPadSerializer;
import io.github.tanguygab.spygotsecurity.database.serializers.lockedblocks.LockedContainerSerializer;
import io.github.tanguygab.spygotsecurity.modules.ListModule;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import lombok.Getter;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private final SpyGotSecurity plugin;
    private final YamlDocument modules;
    private final YamlDocument lockedBlocks;
    @Getter private final static StandardSerializer serializer = StandardSerializer.getDefault();//new StandardSerializer(StandardSerializer.DEFAULT_SERIALIZED_TYPE_KEY);

    static {
        serializer.register(Location.class, new LocationSerializer());
        serializer.register(KeyPad.class, new KeyPadSerializer());
        serializer.register(LockedContainer.class, new LockedContainerSerializer());
        serializer.register(ListModule.class, new ListModuleSerializer());
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unload() {
        lockedBlocks.set("locked-blocks",new ArrayList<>(plugin.getBlockManager().getLockedBlocks().values()));
        modules.set("modules",new ArrayList<>(plugin.getItemManager().getModules().values()));
        try {
            lockedBlocks.save();
            modules.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
