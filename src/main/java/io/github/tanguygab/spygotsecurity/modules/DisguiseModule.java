package io.github.tanguygab.spygotsecurity.modules;

import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.menus.modules.DisguiseModuleMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class DisguiseModule extends SGSModule {

    @Setter private Material material;

    public DisguiseModule(UUID uuid) {
        this(uuid,null);
    }

    public DisguiseModule(UUID uuid, Material material) {
        super(uuid, ModuleType.DISGUISE);
        this.material = material;
    }

    @Override
    public SGSMenu getMenu(Player player) {
        return new DisguiseModuleMenu(player,this);
    }

}
