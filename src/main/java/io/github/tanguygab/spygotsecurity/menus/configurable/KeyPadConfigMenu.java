package io.github.tanguygab.spygotsecurity.menus.configurable;

import io.github.tanguygab.spygotsecurity.blocks.KeyPad;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KeyPadConfigMenu extends ConfigurableBlockMenu<KeyPad> {

    public KeyPadConfigMenu(Player player, KeyPad keypad) {
        super(player, keypad);
        allowModules();
    }

    @Override
    public void onMenuOpen() {
        ItemStack tickDelay = getItem(Material.CLOCK,"&eTick Delay");
        ItemMeta meta = tickDelay.getItemMeta();
        assert meta != null;
        meta.setLore(Utils.color("",
                "&fTicks: &e"+block.getTickDelay(),
                "",
                "&7(Shift-)Left-Click to add 1(5) tick",
                "&7(Shift-)Right-Click to remove 1(5) tick",
                "&7Drop to reset to 20 ticks"
        ));
        tickDelay.setItemMeta(meta);
        inv.setItem(16,tickDelay);
    }

    @Override
    public void onItemClick(ItemStack item, int slot, ClickType click) {
        if (slot != 16) return;
        int ticks = click.isShiftClick() ? 5 : 1;
        ticks = switch (click) {
            case DROP -> 20;
            case RIGHT, SHIFT_RIGHT -> block.getTickDelay()-ticks;
            case LEFT, SHIFT_LEFT -> block.getTickDelay()+ticks;
            default -> block.getTickDelay();
        };
        if (ticks < 0) ticks = 0;
        block.setTickDelay(ticks);
        onMenuOpen();
    }

}
