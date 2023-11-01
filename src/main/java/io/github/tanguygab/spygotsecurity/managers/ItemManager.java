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

    public final boolean CODEBREAKER_ENABLED;
    public final double CODEBREAKER_CHANCE;

    private final List<Integer> morePinsModuleLevels;
    public final boolean LOCKPICK_ENABLED;
    public final int LOCKPICK_DEFAULT_PINS;
    public final double LOCKPICK_BREAK_CHANCE;

    public ItemManager(YamlDocument config) {
        for (ModuleType type : ModuleType.values())
            if (config.getBoolean("modules."+type.toString().toLowerCase(),true))
                allowedModules.add(type);

        HARMING_DAMAGE = config.getDouble("modules.harming.damage",2.);
        CODEBREAKER_ENABLED = config.getBoolean("codebreaker.enabled",true);
        CODEBREAKER_CHANCE = config.getDouble("codebreaker.success-chance",0.33);

        if (config.contains("modules.disguise.disabled-blocks"))
            config.getStringList("modules.disguise.disabled-blocks").forEach(block->{
                Material material = Material.getMaterial(block);
                if (material != null) disabledDisguises.add(material);
            });

        if (config.getBoolean("modules.whitelist",true))
            allowedModules.add(ModuleType.WHITELIST);

        morePinsModuleLevels = config.getIntList("modules.more-pins", List.of(3,5,7));
        LOCKPICK_ENABLED = config.getBoolean("lockpick.enabled",true);
        LOCKPICK_DEFAULT_PINS = validPins(config.getInt("lockpick.default-pins",1));
        LOCKPICK_BREAK_CHANCE = config.getDouble("lockpick.break-chance",.7);
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
            case WHITELIST -> getItem(type.getMaterial(),"&a&lWhiteList","m:whitelist","","&8Right-Click to add players");
            case BLACKLIST -> getItem(type.getMaterial(),"&a&lBlackList","m:blacklist","","&8Right-Click to add players");
            case DISGUISE -> getItem(type.getMaterial(),"&6&lDisguise Module","m:disguise","","&8Right-Click to set a disguise");
            case HARMING -> getItem(type.getMaterial(),"&6&lHarming Module","m:harming");
            case MORE_PINS -> getItem(type.getMaterial(),"&8&lMore Pins Module","m:more_pins","","&7Level: &f1");
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

    public int getMorePins(SGSModule module) {
        if (module == null || module.getType() != ModuleType.MORE_PINS) return 0;
        return validPins(morePinsModuleLevels.get(((MorePinsModule)module).getLevel()));
    }

    public int validPins(int pin) {
        return Math.min(7,Math.max(pin, 1));
    }
}
