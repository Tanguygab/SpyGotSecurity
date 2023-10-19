package io.github.tanguygab.spygotsecurity.database.serializers.modules;

import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import io.github.tanguygab.spygotsecurity.modules.DisguiseModule;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisguiseModuleSerializer implements TypeAdapter<DisguiseModule> {

    @NotNull
    @Override
    public Map<Object, Object> serialize(@NotNull DisguiseModule module) {
        Map<Object, Object> map = new HashMap<>();
        map.put("uuid",module.getUuid());
        map.put("material",module.getMaterial() == null ? null : module.getMaterial());
        return map;
    }

    @NotNull
    @Override
    public DisguiseModule deserialize(@NotNull Map<Object, Object> map) {
        UUID uuid = (UUID) map.get("uuid");
        String mat = (String) map.get("material");
        Material material = mat == null ? null : Material.getMaterial(mat);
        return new DisguiseModule(uuid,material);
    }
}