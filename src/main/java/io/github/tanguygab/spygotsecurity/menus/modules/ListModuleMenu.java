package io.github.tanguygab.spygotsecurity.menus.modules;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.modules.ListModule;
import io.github.tanguygab.spygotsecurity.modules.ModuleType;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.ResponseAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ListModuleMenu extends ModuleMenu<ListModule> {

    protected static final NamespacedKey itemKey = new NamespacedKey(SpyGotSecurity.getInstance(),"item");
    private int page = 1;

    public ListModuleMenu(Player player, ListModule module) {
        super(player, module);
    }

    @Override
    public void onOpen() {
        inv = plugin.getServer().createInventory(null, 54, module.getType().getName() + "ed players ("+page+"/"+getMaxPage()+")");
        fillBorders();
        fillSlots(15,24,33,42);
        inv.setItem(34,getItem(Material.BARRIER,"Clear List"));
        updatePublic(module.isPublik());

        inv.setItem(16,getItem(Material.ARROW,"Next Page"));
        inv.setItem(43,getItem(Material.ARROW,"Previous Page"));

        list(module.getPlayers(),uuid->{
            OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);
            ItemStack item = getItem(Material.PLAYER_HEAD, player.getName());
            if (item.getItemMeta() instanceof SkullMeta skull) {
                skull.setOwnerProfile(player.getPlayerProfile());
                setMeta(skull,player.getUniqueId().toString());
                item.setItemMeta(skull);
            }
            inv.addItem(item);
        });
        inv.addItem(getItem(Material.OAK_BUTTON,"&aAdd a Player"));
    }

    private  <T> void list(List<T> list, Consumer<T> run) {
        if (list.isEmpty()) return;
        for (int i = (page - 1) * 20; i < 20 * page && i < list.size(); i++) {
            run.accept(list.get(i));
        }
    }

    private int getMaxPage() {
        int pages = module.getPlayers().size();
        int max = pages/20+(pages%20==0?0:1);
        return max == 0 ? 1 : max;
    }

    private void setMeta(ItemMeta meta, String data) {
        meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, data);
    }

    private String getKey(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(itemKey, PersistentDataType.STRING)) return null;
        return meta.getPersistentDataContainer().get(itemKey,PersistentDataType.STRING);
    }

    private void updatePublic(boolean publik) {
        module.setPublik(publik);
        ItemStack item = publik ? getItem(Material.ENDER_EYE,"&aPublic")
                : getItem(Material.ENDER_PEARL,"&cPrivate");
        inv.setItem(25,item);
    }

    @Override
    public void onClick(ItemStack item, int slot, ClickType click) {
        switch (slot) {
            case 16,43 -> {
                int page = this.page + (slot == 16 ? 1 : -1);
                if (page < 1 || page > getMaxPage()) return;
                this.page = page;
                open();
            }
            case 25 -> updatePublic(!module.isPublik());
            case 34 -> {
                module.getPlayers().clear();
                open();
            }
            default -> {
                if (click == ClickType.DROP) {
                    String uuid = getKey(item);
                    if (uuid == null) return;
                    module.getPlayers().remove(UUID.fromString(uuid));
                    open();
                    return;
                }
                if (item == null || item.getType() != Material.OAK_BUTTON) return;
                close();
                new AnvilGUI.Builder()
                        .plugin(plugin)
                        .title("Choose a Player")
                        .text("Player Name")
                        .itemLeft(new ItemStack(Material.PLAYER_HEAD))
                        .onClickAsync((slot0, gui) -> CompletableFuture.supplyAsync(() -> {
                            String text = gui.getText();
                            if (text.equals("Not found!") || text.equals("Already added!")) return List.of();
                            OfflinePlayer player = getOfflinePlayer(text);
                            if (player == null) return List.of(ResponseAction.replaceInputText("Not found!"));
                            if (module.getPlayers().contains(player.getUniqueId()))
                                return List.of(ResponseAction.replaceInputText("Already added!"));
                            module.getPlayers().add(player.getUniqueId());
                            return List.of(ResponseAction.close());
                        }))
                        .onClose(gui->plugin.getServer().getScheduler().runTask(plugin, this::open))
                        .open(player);
            }
        }
    }

    private OfflinePlayer getOfflinePlayer(String name) {
        Player online = Bukkit.getServer().getPlayerExact(name);
        if (online != null) return online;
        for (OfflinePlayer offline : Bukkit.getServer().getOfflinePlayers())
            if (name.equals(offline.getName()))
                return offline;
        return null;
    }
}
