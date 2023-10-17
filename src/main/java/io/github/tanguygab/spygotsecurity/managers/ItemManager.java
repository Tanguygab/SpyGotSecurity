package io.github.tanguygab.spygotsecurity.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.modules.ListModule;
import io.github.tanguygab.spygotsecurity.modules.ModuleType;
import io.github.tanguygab.spygotsecurity.modules.SGSModule;
import io.github.tanguygab.spygotsecurity.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class ItemManager {

    private static final NamespacedKey MODULE = new NamespacedKey(SpyGotSecurity.getInstance(),"module");

    private final Map<UUID, SGSModule> modules = new HashMap<>();
    private final List<ModuleType> allowedModules = new ArrayList<>();
    private final double HARMING_DAMAGE;
    private final List<Material> disabledDisguises = new ArrayList<>();

    public ItemManager(YamlDocument config) {
        for (ModuleType type : ModuleType.values())
            if (config.getBoolean("modules."+type.toString().toLowerCase(),true))
                allowedModules.add(type);

        HARMING_DAMAGE = config.getDouble("modules.harming.damage",2.);

        if (config.contains("modules.disguise.disabled-blocks"))
            config.getStringList("modules.disguise.disabled-blocks").forEach(block->{
                Material material = Material.getMaterial(block);
                if (material != null) disabledDisguises.add(material);
            });

        if (config.getBoolean("modules.whitelist",true))
            allowedModules.add(ModuleType.WHITELIST);
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

    public ItemStack getItemFromModule(SGSModule module) {
        ItemStack item = getItemFromType(module.getType());
        item.setItemMeta(ItemUtils.setData(item.getItemMeta(),MODULE,module.getUuid().toString()));
        return item;
    }

    public ItemStack getItemFromType(ModuleType type) {
        if (type == null) return null;
        return switch (type) {
            case WHITELIST -> getItem(Material.PAPER,"&a&lWhiteList","whitelist");
            case BLACKLIST -> getItem(Material.PAPER,"&a&lBlackList","blacklist");
            case DISGUISE -> getItem(Material.PAINTING,"&6&lDisguise Module","disguise");
            case HARMING -> getItem(Material.ITEM_FRAME,"&6&lHarming Module","harming");
        };
    }

    private ItemStack getItem(Material material, String name, String data) {
        return ItemUtils.getItem(material,name,MODULE,data,"","&8Place and right-click me","&8to set a password!");
    }

}
