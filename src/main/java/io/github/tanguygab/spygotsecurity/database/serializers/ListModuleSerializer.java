package io.github.tanguygab.spygotsecurity.database.serializers;

import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import io.github.tanguygab.spygotsecurity.modules.ListModule;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ListModuleSerializer implements TypeAdapter<ListModule> {

    @NotNull
    @Override
    public Map<Object, Object> serialize(@NotNull ListModule module) {
        Map<Object, Object> map = new HashMap<>();
        map.put("uuid",module.getUuid());
        map.put("inverted",module.isInverted());
        map.put("public",module.isPublik());
        map.put("players",module.getPlayers());
        return map;
    }

    @NotNull
    @Override
    public ListModule deserialize(@NotNull Map<Object, Object> map) {
        UUID uuid = (UUID) map.get("uuid");
        boolean inverted = (boolean) map.get("inverted");
        boolean publik = (boolean) map.get("public");
        @SuppressWarnings("unchecked")
        List<UUID> players = (List<UUID>) map.get("players");
        return new ListModule(uuid,inverted,publik,players);
    }
}