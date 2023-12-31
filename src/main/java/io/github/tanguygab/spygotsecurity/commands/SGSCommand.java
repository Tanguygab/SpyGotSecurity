package io.github.tanguygab.spygotsecurity.commands;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import io.github.tanguygab.spygotsecurity.utils.Utils;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor
public abstract class SGSCommand {

    protected final SpyGotSecurity plugin;

    protected String color(String string) {
        return Utils.color(string);
    }

    protected void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public abstract void onCommand(CommandSender sender, String[] args);
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

}
