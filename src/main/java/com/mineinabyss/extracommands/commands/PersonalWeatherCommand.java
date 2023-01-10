package com.mineinabyss.extracommands.commands;

import com.mineinabyss.extracommands.ExtraCommands;
import net.kyori.adventure.text.Component;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PersonalWeatherCommand implements CommandExecutor, TabCompleter {
    public static final Map<String, WeatherType> weatherAliases = new LinkedHashMap<>();

    static {
        weatherAliases.put("sun", WeatherType.CLEAR);
        weatherAliases.put("clear", WeatherType.CLEAR);
        weatherAliases.put("storm", WeatherType.DOWNFALL);
        weatherAliases.put("thunder", WeatherType.DOWNFALL);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        label = command.getLabel().replace("extracommands:", "");
        String firstArg = args.length >= 1 ? args[0] : "";
        String secondArg = args.length >= 2 ? args[1] : "";
        Player player = sender instanceof Player ? (Player) sender : ExtraCommands.validatePlayer(secondArg, sender);

        if (!ExtraCommands.checkPermission(sender, "extracommands.pweather")) return false;

        if (label.equals("pweather")) {
            if (player == null) return false;
            if (sender instanceof Player p && p.getUniqueId().equals(player.getUniqueId()) && !ExtraCommands.checkPermission(sender, "extracommands.pweather.others"))
                return false;

            WeatherType weather = weatherAliases.getOrDefault(firstArg, null);
            if (firstArg.equals("reset")) {
                player.resetPlayerWeather();
                sender.sendMessage(Component.text("Reset weather of " + ExtraCommands.miniMessage.serialize(player.displayName()) + "").color(ExtraCommands.INFO));
                return false;
            }

            if (weather == null) {
                sender.sendMessage(Component.text("Please give a valid weather-type").color(ExtraCommands.ERROR));
                return false;
            }

            player.setPlayerWeather(weather);
            sender.sendMessage(Component.text("Set weather of " + ExtraCommands.miniMessage.serialize(player.displayName()) + " to ").color(ExtraCommands.INFO).append(Component.text(weather.name()).color(ExtraCommands.ERROR)));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equals("pweather")) {
            return Stream.of("storm", "sun", "clear", "thunder", "reset").filter(s -> s.startsWith(args[0])).toList();
        }
        return new ArrayList<>();
    }
}
