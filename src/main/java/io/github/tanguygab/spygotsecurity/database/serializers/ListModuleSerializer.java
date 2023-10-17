package io.github.tanguygab.spygotsecurity.database.serializers;

import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import io.github.tanguygab.spygotsecurity.modules.ListModule;
import io.github.tanguygab.spygotsecurity.modules.ModuleType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ListModuleSerializer implements TypeAdapter<ListModule> {

    @NotNull
    @Override
    public Map<Object, Object> serialize(@NotNull ListModule module) {
        Map<Object, Object> map = new HashMap<>();
        map.put("blacklist",module.getType() == ModuleType.BLACKLIST);
        map.put("uuid",module.getUuid());
        map.put("public",module.isPublik());
        map.put("players",module.getPlayers());
        return map;
    }

    @NotNull
    @Override
    public ListModule deserialize(@NotNull Map<Object, Object> map) {
        UUID uuid = (UUID) map.get("uuid");
        boolean blacklist = (boolean) map.get("blacklist");
        boolean publik = (boolean) map.get("public");
        @SuppressWarnings("unchecked")
        List<UUID> players = (List<UUID>) map.get("players");
        return new ListModule(uuid,blacklist,publik,players);
    }
}