package io.github.tanguygab.spygotsecurity.database.serializers.modules;

import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import io.github.tanguygab.spygotsecurity.modules.HarmingModule;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HarmingModuleSerializer implements TypeAdapter<HarmingModule> {

    @NotNull
    @Override
    public Map<Object, Object> serialize(@NotNull HarmingModule module) {
        Map<Object, Object> map = new HashMap<>();
        map.put("uuid",module.getUuid());
        return map;
    }

    @NotNull
    @Override
    public HarmingModule deserialize(@NotNull Map<Object, Object> map) {
        UUID uuid = (UUID) map.get("uuid");
        return new HarmingModule(uuid);
    }
}