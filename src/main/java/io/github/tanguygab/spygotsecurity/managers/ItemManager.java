package io.github.tanguygab.spygotsecurity.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.modules.*;
import io.github.tanguygab.spygotsecurity.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemManager {

    public static final NamespacedKey ITEM = new NamespacedKey(SpyGotSecurity.getInstance(),"item");
    public static final NamespacedKey CODEBREAKER = new NamespacedKey(SpyGotSecurity.getInstance(),"codebreaker");
    private static final NamespacedKey MODULE = new NamespacedKey(SpyGotSecurity.getInstance(),"module");

    @Getter private final Map<UUID, SGSModule> modules = new HashMap<>();
    @Getter private final List<ModuleType> allowedModules = new ArrayList<>();
    @Getter private final List<Material> disabledDisguises = new ArrayList<>();
    public final double HARMING_DAMAGE;
    public final double CODEBREAKER_CHANCE;

    public ItemManager(YamlDocument config) {
        for (ModuleType type : ModuleType.values())
            if (config.getBoolean("modules."+type.toString().toLowerCase(),true))
                allowedModules.add(type);

        HARMING_DAMAGE = config.getDouble("modules.harming.damage",2.);
        CODEBREAKER_CHANCE = config.getDouble("modules.codebreaker.success-chance",0.33);

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

    public boolean canDisguise(Material material) {
        return material.isBlock() && !disabledDisguises.contains(material);
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
                case "harming" -> new HarmingModule(uuid);
                case "disguise" -> new DisguiseModule(uuid);
                default -> null;
            };
            if (module == null) return null;
            item.setItemMeta(ItemUtils.setData(item.getItemMeta(),MODULE,uuid.toString()));
            addModule(module);
            return module;
        }
    }

    public ItemStack getItemFromModule(SGSModule module) {
        ItemStack item = getItemFromModuleType(module.getType());
        item.setItemMeta(ItemUtils.setData(item.getItemMeta(),MODULE,module.getUuid().toString()));
        return item;
    }

    public ItemStack getItemFromType(String type) {
        if (type == null) return null;
        return switch (type) {
            case "codebreaker" -> getItem(Material.IRON_DOOR,"&d&lCodeBreaker","c:3","","&8Right-Click a locked block to break-in");
            case "lockpick" -> getItem(Material.IRON_HOE,"&d&lLockPick",type,"","&8Right-Click a locked block to lockpick");
            case "reinforcer" -> getItem(Material.IRON_SHOVEL,"&d&lBlock Reinforcer",type,"","&8Left-Click a block to reinforce","&7Right-Click to open menu");
            case "remover" -> getItem(Material.IRON_PICKAXE,"&d&lBlock Remover",type,"","&8Right-Click a reinforced block to remove");
            default -> getItemFromModuleType(ModuleType.get(type));
        };
    }
    public ItemStack getItemFromModuleType(ModuleType type) {
        if (type == null) return null;
        return switch (type) {
            case WHITELIST -> getItem(Material.PAPER,"&a&lWhiteList","m:whitelist","","&8Right-Click to add players");
            case BLACKLIST -> getItem(Material.PAPER,"&a&lBlackList","m:blacklist","","&8Right-Click to add players");
            case DISGUISE -> getItem(Material.PAINTING,"&6&lDisguise Module","m:disguise","","&8Right-Click to set a disguise");
            case HARMING -> getItem(Material.ITEM_FRAME,"&6&lHarming Module","m:harming");
        };
    }

    private ItemStack getItem(Material material, String name, String data, String... lore) {
        NamespacedKey key = ITEM;
        if (data.startsWith("m:")) {
            key = MODULE;
            data = data.substring(2);
        }
        if (data.startsWith("c:")) {
            key = CODEBREAKER;
            data = data.substring(2);
        }
        return ItemUtils.getItem(material,name,key,data,lore);
    }

    public int getCodeBreakerUses(ItemStack item) {
        String data = ItemUtils.getTypeFromItem(CODEBREAKER,item);
        if (data == null) return -1;
        try {return Integer.parseInt(data);}
        catch (Exception e) {return -1;}
    }

    public void setCodebreakerUses(ItemStack item, int uses) {
        item.setItemMeta(ItemUtils.setData(item.getItemMeta(),CODEBREAKER,uses+""));
    }

}
