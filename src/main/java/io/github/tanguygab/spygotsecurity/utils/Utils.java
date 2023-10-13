package io.github.tanguygab.spygotsecurity.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&',string);
    }
    public static void send(Player player, String message) {
        player.sendMessage(color(message));
    }

}
