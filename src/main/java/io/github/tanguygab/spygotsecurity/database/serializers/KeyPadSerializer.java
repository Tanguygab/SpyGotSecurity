package io.github.tanguygab.spygotsecurity.database.serializers;

import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import dev.dejvokep.boostedyaml.serialization.standard.TypeAdapter;
import io.github.tanguygab.spygotsecurity.blocks.KeyPad;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeyPadSerializer implements TypeAdapter<KeyPad> {

    @NotNull
    @Override
    public Map<Object, Object> serialize(@NotNull KeyPad keypad) {
        Map<Object,Object> map = new HashMap<>();
        map.put("location",keypad.getBlock().getLocation());
        map.put("uuid",keypad.getOwner().toString());
        map.put("password",keypad.getPassword());
        map.put("salt",keypad.getSalt());
        return map;
    }

    @NotNull
    @Override
    public KeyPad deserialize(@NotNull Map<Object, Object> map) {
        Location loc = (Location) StandardSerializer.getDefault().deserialize((Map<Object, Object>) map.get("location"));
        UUID uuid = UUID.fromString((String) map.get("uuid"));
        byte[] password = (byte[]) map.get("password");
        byte[] salt = (byte[]) map.get("salt");
        return new KeyPad(loc.getBlock(),uuid,password,salt);
    }
}
