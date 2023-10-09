package io.github.tanguygab.spygotsecurity.listeners;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

@AllArgsConstructor
public abstract class SGSListener implements Listener {

    protected final SpyGotSecurity plugin;

    protected String color(String string) {
        return ChatColor.translateAlternateColorCodes('&',string);
    }
    protected void send(Player player, String message) {
        player.sendMessage(color(message));
    }

}
