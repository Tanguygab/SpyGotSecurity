package io.github.tanguygab.spygotsecurity.modules;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum ModuleType {

    WHITELIST("Whitelist",Material.PAPER),
    BLACKLIST("Blacklist",Material.PAPER),
    DISGUISE("Disguise",Material.PAINTING),
    HARMING("Harming",Material.ITEM_FRAME),
    MORE_PINS("More Pins", Material.IRON_BARS);

    private final String name;
    private final Material material;

    public static ModuleType get(String string) {
        string = string.toLowerCase();
        for (ModuleType type : values())
            if (type.toString().toLowerCase().equals(string))
                return type;
        return null;
    }
}
