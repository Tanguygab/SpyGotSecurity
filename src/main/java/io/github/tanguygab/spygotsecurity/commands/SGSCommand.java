package io.github.tanguygab.spygotsecurity.commands;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public abstract class SGSCommand {

    protected final SpyGotSecurity plugin;

    protected String color(String string) {
        return ChatColor.translateAlternateColorCodes('&',string);
    }
    protected List<String> color(String... array) {
        List<String> lore = new ArrayList<>();
        for (String line : array) lore.add(color(line));
        return lore;
    }

    protected void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public abstract void onCommand(CommandSender sender, String[] args);
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

}
