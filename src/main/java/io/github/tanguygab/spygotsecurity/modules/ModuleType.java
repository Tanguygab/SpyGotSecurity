package io.github.tanguygab.spygotsecurity.modules;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModuleType {

    WHITELIST("Whitelist"),
    BLACKLIST("Blacklist"),
    DISGUISE("Disguise"),
    HARMING("Harming");

    private final String name;

    public static ModuleType get(String string) {
        string = string.toLowerCase();
        for (ModuleType type : values())
            if (type.toString().toLowerCase().equals(string))
                return type;
        return null;
    }
}
