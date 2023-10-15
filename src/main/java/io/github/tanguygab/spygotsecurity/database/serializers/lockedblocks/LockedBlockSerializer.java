package io.github.tanguygab.spygotsecurity.database.serializers.lockedblocks;

import dev.dejvokep.boostedyaml.serialization.standard.StandardSerializer;
import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import org.bukkit.Location;

import java.util.*;

public abstract class LockedBlockSerializer {

    protected Map<Object, Object> serialize(LockedBlock block) {
        Map<Object,Object> map = new HashMap<>();
        map.put("location",block.getBlock().getLocation());
        map.put("uuid",block.getOwner());
        map.put("modules",block.getModules().stream().map(SGSModule::getUuid).toList());
        map.put("password",block.getPassword());
        map.put("salt",block.getSalt());
        return map;
    }

    protected Location getLocation(Map<Object, Object> map) {
        @SuppressWarnings("unchecked")
        Map<Object, Object> obj = (Map<Object, Object>) map.get("location");
        return (Location) StandardSerializer.getDefault().deserialize(obj);
    }

    protected UUID getUUID(Map<Object, Object> map) {
        return (UUID) map.get("uuid");
    }

    protected List<SGSModule> getModules(Map<Object, Object> map) {
        List<SGSModule> modules = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<UUID> uuids = (List<UUID>) map.get("modules");
        uuids.forEach(uuid -> modules.add(SpyGotSecurity.getInstance().getItemManager().getModules().get(uuid)));
        return modules;
    }

    protected byte[] getPassword(Map<Object, Object> map, boolean salt) {
        return (byte[]) map.get(salt ? "salt" : "password");
    }
}
