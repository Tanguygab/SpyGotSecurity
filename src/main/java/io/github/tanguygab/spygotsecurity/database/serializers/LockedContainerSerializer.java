package io.github.tanguygab.spygotsecurity.database.serializers;

import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import io.github.tanguygab.spygotsecurity.blocks.LockedContainer;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LockedContainerSerializer implements TypeAdapter<LockedContainer> {

    @NotNull
    @Override
    public Map<Object, Object> serialize(@NotNull LockedContainer container) {
        Map<Object,Object> map = new HashMap<>();
        map.put("location",container.getBlock().getLocation());
        map.put("uuid",container.getOwner().toString());
        map.put("password",container.getPassword());
        map.put("salt",container.getSalt());
        return map;
    }

    @NotNull
    @Override
    public LockedContainer deserialize(@NotNull Map<Object, Object> map) {
        @SuppressWarnings("unchecked")
        Location loc = (Location) StandardSerializer.getDefault().deserialize((Map<Object, Object>) map.get("location"));
        assert loc != null;
        UUID uuid = UUID.fromString((String) map.get("uuid"));
        byte[] password = (byte[]) map.get("password");
        byte[] salt = (byte[]) map.get("salt");
        return new LockedContainer(loc.getBlock(),uuid,password,salt);
    }
}
