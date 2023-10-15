package io.github.tanguygab.spygotsecurity.managers;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.modules.ListModule;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import io.github.tanguygab.spygotsecurity.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ItemManager {

    private static final NamespacedKey MODULE = new NamespacedKey(SpyGotSecurity.getInstance(),"module");

    private final Map<UUID, SGSModule> modules = new HashMap<>();

    public ItemManager(SpyGotSecurity plugin) {

    }

    public void addModule(SGSModule module) {
        modules.put(module.getUuid(),module);
    }

    public SGSModule getModuleFromItem(ItemStack item) {
        String type = ItemUtils.getTypeFromItem(MODULE,item);
        if (type == null) return null;
        UUID uuid;
        try {
            uuid = UUID.fromString(type);
            return modules.get(uuid);
        } catch (Exception e) {
            uuid = UUID.randomUUID();
            SGSModule module = switch (type) {
                case "whitelist","blacklist" -> new ListModule(uuid,type.equals("blacklist"));
                //case "disguise" -> null;
                default -> null;
            };
            item.setItemMeta(ItemUtils.setData(item.getItemMeta(),MODULE,uuid.toString()));
            addModule(module);
            return module;
        }
    }

    public ItemStack getItemFromType(String type) {
        return switch (type) {
            case "whitelist" -> getItem(Material.PAPER,"&a&lWhiteList","whitelist");
            case "blacklist" -> getItem(Material.PAPER,"&a&lBlackList","blacklist");
            case "disguise" -> getItem(Material.PAINTING,"&6&lDisguise Module","disguise");
            default -> null;
        };
    }

    private ItemStack getItem(Material material, String name, String data) {
        return ItemUtils.getItem(material,name,MODULE,data,"","&8Place and right-click me","&8to set a password!");
    }

}
