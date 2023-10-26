package io.github.tanguygab.spygotsecurity.database.serializers;

import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LocationSerializer implements TypeAdapter<Location> {
    @NotNull
    @Override
    public Map<Object, Object> serialize(@NotNull Location location) {
        Map<Object,Object> map = new HashMap<>();
        map.put("x",location.getBlockX());
        map.put("y",location.getBlockY());
        map.put("z",location.getBlockZ());
        map.put("world", Objects.requireNonNull(location.getWorld()).getName());
        return map;
    }

    @NotNull
    @Override
    public Location deserialize(@NotNull Map<Object, Object> map) {
        int x = (int) map.get("x");
        int y = (int) map.get("y");
        int z = (int) map.get("z");
        String world = (String) map.get("world");
        return new Location(Bukkit.getServer().getWorld(world),x,y,z);
    }
}
