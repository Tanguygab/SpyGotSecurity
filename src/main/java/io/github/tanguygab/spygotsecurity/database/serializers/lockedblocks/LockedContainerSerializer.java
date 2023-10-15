package io.github.tanguygab.spygotsecurity.database.serializers.lockedblocks;

import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import io.github.tanguygab.spygotsecurity.blocks.LockedContainer;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LockedContainerSerializer extends LockedBlockSerializer implements TypeAdapter<LockedContainer> {

    @NotNull
    @Override
    public Map<Object, Object> serialize(@NotNull LockedContainer container) {
        return super.serialize(container);
    }

    @NotNull
    @Override
    public LockedContainer deserialize(@NotNull Map<Object, Object> map) {
        Location loc = getLocation(map);
        UUID uuid = getUUID(map);
        List<SGSModule> modules = getModules(map);
        byte[] password = getPassword(map,false);
        byte[] salt = getPassword(map,true);
        return new LockedContainer(loc.getBlock(),uuid,modules,password,salt);
    }
}
