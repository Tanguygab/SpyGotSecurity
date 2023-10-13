package io.github.tanguygab.spygotsecurity.commands;

import io.github.tanguygab.spygotsecurity.SpyGotSecurity;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand extends SGSCommand {

    public ListCommand(SpyGotSecurity plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        String item = args.length > 0 ? args[0] : "";
        switch (item) {
            case "keypad" -> {
            }
            case "idk" -> {}
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
