package com.mineinabyss.extracommands.commands;

import com.mineinabyss.extracommands.ExtraCommands;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GameModeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String commandLabel = command.getLabel().replace("extracommands:", "");
        String firstArg = args.length >= 1 ? args[0] : "";
        String secondArg = args.length >= 2 ? args[1] : "";
        Player player = sender instanceof Player ? (Player) sender : ExtraCommands.validatePlayer(secondArg, sender);

        if (!ExtraCommands.checkPermission(sender, "extracommands.gamemode")) return false;

        if (commandLabel.startsWith("gamemode")) {
            if (player == null) return false;
            if (sender instanceof Player p && p.getUniqueId().equals(player.getUniqueId()) && !ExtraCommands.checkPermission(sender, "extracommands.gamemode.others")) return false;

            String gamemode = label.replace("gm", "").replace(commandLabel, "");
            if (gamemode.isEmpty()) gamemode = firstArg;

            switch (gamemode.toLowerCase()) {
                case "survival", "s" -> player.setGameMode(GameMode.SURVIVAL);
                case "creative", "c" -> player.setGameMode(GameMode.CREATIVE);
                case "spectator", "sp" -> player.setGameMode(GameMode.SPECTATOR);
                case "adventure", "a" -> player.setGameMode(GameMode.ADVENTURE);
                default -> sender.sendMessage(Component.text("Not a valid gamemode").color(ExtraCommands.ERROR));
            }
            if (!gamemode.isEmpty())
                sender.sendMessage(Component.text("Gamemode of " + ExtraCommands.miniMessage.serialize(player.displayName()) + " set to ").color(ExtraCommands.INFO).append(Component.text(player.getGameMode().name()).color(ExtraCommands.ERROR)));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equals("gamemode")) {
            switch (args.length) {
                case 1 -> {
                    return Stream.of("survival", "spectator", "creative", "adventure").filter(s -> s.startsWith(args[0])).toList();
                }
                case 2 -> {
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(s -> s.startsWith(args[1])).toList();
                }
            }
        }
        return new ArrayList<>();
    }
}
