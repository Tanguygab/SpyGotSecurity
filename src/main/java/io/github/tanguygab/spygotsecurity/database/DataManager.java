package io.github.tanguygab.spygotsecurity.database;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.KeyPad;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.blocks.LockedContainer;
import io.github.tanguygab.spygotsecurity.database.serializers.*;
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
            StandardSerializer.getDefault().register(KeyPad.class, new KeyPadSerializer());
            StandardSerializer.getDefault().register(LockedContainer.class, new LockedContainerSerializer());
            StandardSerializer.getDefault().register(Location.class, new LocationSerializer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        List<Map<Object, Object>> list = (List<Map<Object, Object>>) data.getList("keypads");
        list.forEach(keypad->plugin.getBlockManager().addLockedBlock((LockedBlock) StandardSerializer.getDefault().deserialize(keypad)));
    }

    public void unload() {
        data.set("keypads",new ArrayList<>(plugin.getBlockManager().getLockedBlocks().values()));
        try {
            data.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
