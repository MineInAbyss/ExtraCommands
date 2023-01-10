package com.mineinabyss.extracommands;

import com.mineinabyss.extracommands.commands.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExtraCommands extends JavaPlugin {

    public static final MiniMessage miniMessage = MiniMessage.miniMessage();
    public static final NamedTextColor ERROR = NamedTextColor.RED;
    public static final NamedTextColor INFO = NamedTextColor.GOLD;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("ptime").setExecutor(new PersonalTimeCommand());
        getCommand("pweather").setExecutor(new PersonalWeatherCommand());
        getCommand("gamemode").setExecutor(new GameModeCommand());
        getCommand("seen").setExecutor(new SeenCommand());
        getCommand("hunger").setExecutor(new HungerCommand());

    }

    public static boolean checkPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Component.text("You do not have permission to run this command ").color(ERROR));
            return false;
        }
        return true;
    }

    public static Player validatePlayer(String arg, CommandSender sender) {
        Player player = null;
        if (!arg.isEmpty())
            player = Bukkit.getPlayer(arg);
        if (player == null) {
            if (sender instanceof ConsoleCommandSender)
                sender.sendMessage(Component.text("This command can only be run on players").color(ERROR));
            else sender.sendMessage(Component.text(arg + " is not a valid player").color(ERROR));
        }
        return player;
    }
}
