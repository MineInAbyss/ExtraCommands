package com.mineinabyss.extracommands.commands;

import com.mineinabyss.extracommands.ExtraCommands;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
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

public class PersonalTimeCommand implements CommandExecutor, TabCompleter {
    public static final Map<String, Integer> nameToTicks = new LinkedHashMap<>();

    static {
        nameToTicks.put("sunrise", 23000);
        nameToTicks.put("dawn", 23000);

        nameToTicks.put("daystart", 0);
        nameToTicks.put("day", 0);

        nameToTicks.put("morning", 1000);

        nameToTicks.put("midday", 6000);
        nameToTicks.put("noon", 6000);

        nameToTicks.put("afternoon", 9000);

        nameToTicks.put("sunset", 12000);
        nameToTicks.put("dusk", 12000);
        nameToTicks.put("sundown", 12000);
        nameToTicks.put("nightfall", 12000);

        nameToTicks.put("nightstart", 14000);
        nameToTicks.put("night", 14000);

        nameToTicks.put("midnight", 18000);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        label = command.getLabel().replace("extracommands:", "");
        String firstArg = args.length >= 1 ? args[0] : "";
        String secondArg = args.length >= 2 ? args[1] : "";
        Player player = sender instanceof Player ? (Player) sender : ExtraCommands.validatePlayer(secondArg, sender);

        if (!ExtraCommands.checkPermission(sender, "extracommands.ptime")) return false;

        if (label.equals("ptime")) {

            if (player == null) return false;
            if (sender instanceof Player p && p.getUniqueId().equals(player.getUniqueId()) && !ExtraCommands.checkPermission(sender, "extracommands.ptime.others")) return false;

            final String timeArg = firstArg.replace("@", "");
            Long time;
            try {
                time = Long.parseLong(timeArg);
            } catch (NumberFormatException e) {
                if (timeArg.equals("reset")) time = null;
                else if (nameToTicks.containsKey(timeArg)) {
                    try {
                        time = Long.parseLong(String.valueOf(nameToTicks.getOrDefault(timeArg, 0)));
                    } catch (NumberFormatException e2) {
                        sender.sendMessage(Component.text("Please give a valid time").color(ExtraCommands.ERROR));
                        return false;
                    }
                }
                sender.sendMessage(Component.text("Please give a valid time").color(ExtraCommands.ERROR));
                return false;
            }

            setPlayerTime(player, time, !firstArg.startsWith("@"));

            sender.sendMessage(Component.text("Set time of " + ExtraCommands.miniMessage.serialize(player.displayName()) + " to ").color(ExtraCommands.INFO).append(Component.text(time + " ticks").color(ExtraCommands.ERROR)));
        }

        return false;
    }

    private static void setPlayerTime(final Player player, final Long ticks, final Boolean relative) {
        if (ticks == null) {
            player.resetPlayerTime();
        } else {
            final World world = player.getWorld();
            long time = player.getPlayerTime();
            time -= time % 24000;
            time += 24000 + ticks;
            if (relative) {
                time -= world.getTime();
            }
            player.setPlayerTime(time, relative);
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equals("ptime")) {
            switch (args.length) {
                case 1 -> {
                    return Stream.of("sunrise", "day", "morning", "midday", "afternoon", "sunset", "night", "midnight", "reset").filter(s -> s.startsWith(args[0])).toList();
                }
                case 2 -> {
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(s -> s.startsWith(args[1])).toList();
                }
            }
        }
        return new ArrayList<>();
    }
}
