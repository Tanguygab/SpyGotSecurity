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
        map.put("players",module.getPlayers().stream().map(UUID::toString).toList());
        return map;
    }

    @NotNull
    @Override
    public ListModule deserialize(@NotNull Map<Object, Object> map) {
        UUID uuid = (UUID) map.get("uuid");
        boolean inverted = (boolean) map.get("inverted");
        @SuppressWarnings("unchecked")
        List<UUID> players = (List<UUID>) map.get("players");
        return new ListModule(uuid,inverted,players);
}
}