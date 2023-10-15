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
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataManager {

    private final SpyGotSecurity plugin;
    private final YamlDocument data;

    public DataManager(SpyGotSecurity plugin) {
        this.plugin = plugin;
        try {
            data = YamlDocument.create(new File(plugin.getDataFolder(),"data.yml"));
            StandardSerializer serializer = StandardSerializer.getDefault();
            serializer.register(KeyPad.class, new KeyPadSerializer());
            serializer.register(LockedContainer.class, new LockedContainerSerializer());
            serializer.register(Location.class, new LocationSerializer());
            serializer.register(ListModule.class, new ListModuleSerializer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        List<Map<Object, Object>> modules = (List<Map<Object, Object>>) data.getList("modules");
        modules.forEach(module->plugin.getItemManager().addModule((SGSModule) StandardSerializer.getDefault().deserialize(module)));

        List<Map<Object, Object>> lockedBlocks = (List<Map<Object, Object>>) data.getList("locked-blocks");
        lockedBlocks.forEach(locked->plugin.getBlockManager().addLockedBlock((LockedBlock) StandardSerializer.getDefault().deserialize(locked)));
    }

    public void unload() {
        data.set("locked-blocks",new ArrayList<>(plugin.getBlockManager().getLockedBlocks().values()));
        data.set("modules",new ArrayList<>(plugin.getItemManager().getModules().values()));
        try {
            data.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
