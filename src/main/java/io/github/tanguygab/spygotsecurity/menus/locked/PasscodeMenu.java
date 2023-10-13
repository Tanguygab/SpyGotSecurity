package io.github.tanguygab.spygotsecurity.menus.locked;

import io.github.tanguygab.spygotsecurity.blocks.LockedBlock;
import io.github.tanguygab.spygotsecurity.menus.SGSMenu;
import io.github.tanguygab.spygotsecurity.utils.PasswordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public abstract class PasscodeMenu extends SGSMenu {

    protected final LockedBlock block;
    private final List<String> passcode = new ArrayList<>();
    private final Map<Integer,Integer> numbers = new HashMap<>();
    private boolean showPasscode = false;

    public PasscodeMenu(LockedBlock block, Player player, String title) {
        super(player);
        this.block = block;
        inv = plugin.getServer().createInventory(null, 54, title);
    }

    @Override
    public void onOpen() {
        inv.setItem(42,getItem(Material.LIME_WOOL,"Confirm"));
        updateNametag();
        inv.setItem(15,getHead("a185c97dbb8353de652698d24b64327b793a3f32a98be67b719fbedab35e","Backspace"));

        setCodeItem(38,0);
        setCodeItem(28,1);
        setCodeItem(29,2);
        setCodeItem(30,3);
        setCodeItem(19,4);
        setCodeItem(20,5);
        setCodeItem(21,6);
        setCodeItem(10,7);
        setCodeItem(11,8);
        setCodeItem(12,9);
        fillMenu();
    }

    private void updateNametag() {
        String name = showPasscode ? String.join("",passcode) : "*".repeat(passcode.size());
        inv.setItem(24,getItem(Material.NAME_TAG,name.isEmpty() ? "Enter your Password!" : name));
    }

    @Override
    public void onClick(ItemStack item, int slot, ClickType click) {
        switch (slot) {
            case 15 -> {
                if (passcode.isEmpty()) return;
                passcode.remove(passcode.size()-1);
                updateNametag();
            }
            case 24 -> {
                showPasscode = !showPasscode;
                updateNametag();
            }
            case 42 -> {
                passcode.add("a");
                player.closeInventory();
                byte[] salt = getSalt();
                PasswordUtils.asyncHash(String.join("",passcode),salt,passcode->onClick(passcode,salt));
            }
            default -> {
                if (!numbers.containsKey(slot)) return;
                passcode.add(String.valueOf(numbers.get(slot)));
                updateNametag();
            }
        }
    }

    protected abstract byte[] getSalt();
    protected abstract void onClick(byte[] password, byte[] salt);

    private void setCodeItem(int slot, int num) {
        ItemStack item = getHead(getTexture(num),String.valueOf(num));
        inv.setItem(slot,item);
        numbers.put(slot,num);
    }
    private String getTexture(int num) {
        return switch (num) {
            case 0 -> "0ebe7e5215169a699acc6cefa7b73fdb108db87bb6dae2849fbe24714b27";
            case 1 -> "71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530";
            case 2 -> "4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847";
            case 3 -> "1d4eae13933860a6df5e8e955693b95a8c3b15c36b8b587532ac0996bc37e5";
            case 4 -> "d2e78fb22424232dc27b81fbcb47fd24c1acf76098753f2d9c28598287db5";
            case 5 -> "6d57e3bc88a65730e31a14e3f41e038a5ecf0891a6c243643b8e5476ae2";
            case 6 -> "334b36de7d679b8bbc725499adaef24dc518f5ae23e716981e1dcc6b2720ab";
            case 7 -> "6db6eb25d1faabe30cf444dc633b5832475e38096b7e2402a3ec476dd7b9";
            case 8 -> "59194973a3f17bda9978ed6273383997222774b454386c8319c04f1f4f74c2b5";
            case 9 -> "e67caf7591b38e125a8017d58cfc6433bfaf84cd499d794f41d10bff2e5b840";
            default -> null;
        };
    }
}
