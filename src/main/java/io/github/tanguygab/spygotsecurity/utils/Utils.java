package io.github.tanguygab.spygotsecurity.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public final static Random RANDOM = new Random();

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&',string);
    }
    public static void send(CommandSender player, String message) {
        player.sendMessage(color(message));
    }
    public static void actionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color(message)));
    }
    public static List<String> color(String... array) {
        List<String> lore = new ArrayList<>();
        for (String line : array) lore.add(color(line));
        return lore;
    }

}
