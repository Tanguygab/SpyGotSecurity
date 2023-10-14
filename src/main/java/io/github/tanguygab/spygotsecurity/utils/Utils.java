package io.github.tanguygab.spygotsecurity.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&',string);
    }
    public static void send(Player player, String message) {
        player.sendMessage(color(message));
    }
    public static List<String> color(String... array) {
        List<String> lore = new ArrayList<>();
        for (String line : array) lore.add(color(line));
        return lore;
    }

}
