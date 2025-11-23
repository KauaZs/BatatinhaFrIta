package org.kauazs.batatinhafrita.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Colorize {
    public static String format(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String stripColor(String str) {
        return ChatColor.stripColor(str);
    }

    public static void sendMessage(CommandSender sender, String str) {
        sender.sendMessage(format(str));
    }
}