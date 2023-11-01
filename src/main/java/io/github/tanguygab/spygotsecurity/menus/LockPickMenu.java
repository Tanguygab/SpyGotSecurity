package io.github.tanguygab.spygotsecurity.menus;

import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LockPickMenu extends SGSMenu {

    private int pos = 0;
    protected final LockedBlock block;
    private boolean moving = false;
    private List<Integer> pins;

    public LockPickMenu(Player player, LockedBlock block) {
        super(player);
        this.block = block;
        inv = plugin.getServer().createInventory(null,54,"Lock picking");
    }

    @Override
    public void onOpen() {
        fillBorders();
        inv.setItem(48,getHead("Left","a185c97dbb8353de652698d24b64327b793a3f32a98be67b719fbedab35e"));
        inv.setItem(49,getHead("Up","105a2cab8b68ea57e3af992a36e47c8ff9aa87cc8776281966f8c3cf31a38"));
        inv.setItem(50,getHead("Right","31c0ededd7115fc1b23d51ce966358b27195daf26ebb6e45a66c34c69c34091"));
        move(false);

        pins = new ArrayList<>(switch (block.getPins()) {
            case 7 -> List.of(0,1,2,3,4,5,6);
            case 6 -> List.of(0,1,2,4,5,6);
            case 5 -> List.of(0,2,3,4,6);
            case 4 -> List.of(0,2,4,6);
            case 3 -> List.of(1,3,5);
            case 2 -> List.of(2,4);
            default -> List.of(3);
        });
        Collections.shuffle(pins);
        pins.forEach(this::loadPin);
    }

    private void loadPin(int index) {
        int slot = index+10;
        inv.setItem(slot,getItem(Material.CHAIN));
        inv.setItem(slot+9,getItem(Material.CHAIN));
        inv.setItem(slot+18,getItem(Material.DARK_OAK_FENCE_GATE));
    }

    @Override
    public void onClick(ItemStack item, int slot, ClickType click) {
        switch (slot) {
            case 48,50 -> move(slot == 50);
            case 49 -> moveUp();
        }
    }

    private void move(boolean right) {
        if (moving) return;
        moving = true;
        pos+=right ? 1 : -1;
        if (pos < 0) pos = 0;
        if (pos > 7) pos = 7;

        for (int i = 1; i <= pos; i++) inv.setItem(36+pos-i,getItem(Material.OAK_TRAPDOOR));
        inv.setItem(36+pos,getItem(Material.LIGHTNING_ROD));
        for (int i = pos+1; i < 8; i++)
            inv.setItem(36+i,getItem(Material.WHITE_STAINED_GLASS_PANE));
        moving = false;
    }

    private void moveUp() {
        if (pos == 0 || moving || pins.isEmpty()) return;
        int slot = 36+pos;
        ItemStack above = inv.getItem(slot-9);
        if (above == null || above.getType() != Material.DARK_OAK_FENCE_GATE) return;

        if (pins.get(0) != pos-1) {
            if (player.getGameMode() != GameMode.CREATIVE && Utils.RANDOM.nextDouble() < plugin.getItemManager().LOCKPICK_BREAK_CHANCE) {
                player.getInventory().setItemInMainHand(null);
                player.closeInventory();
                Utils.send(player,"&cYour Lockpick broke!");
            } else onOpen();
            return;
        }
        pins.remove(0);

        moving = true;
        inv.setItem(slot,getItem(Material.OAK_TRAPDOOR));
        inv.setItem(slot-9,getItem(Material.LIGHTNING_ROD));
        inv.setItem(slot-18,getItem(Material.DARK_OAK_FENCE_GATE));

        plugin.getServer().getScheduler().runTaskLater(plugin,()->{
            inv.setItem(slot-18,getItem(Material.BLACK_STAINED_GLASS_PANE));
            inv.setItem(slot-27,getItem(Material.DARK_OAK_FENCE_GATE));
            plugin.getServer().getScheduler().runTaskLater(plugin,()->{
                inv.setItem(slot-9,getItem(Material.BLACK_STAINED_GLASS_PANE));
                inv.setItem(slot,getItem(Material.LIGHTNING_ROD));
                if (pins.isEmpty()) {
                    close();
                    block.onSuccess(player);
                }
                moving = false;
            },15);
        },5);
    }

}
