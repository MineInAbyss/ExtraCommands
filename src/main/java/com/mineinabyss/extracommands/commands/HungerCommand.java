package com.mineinabyss.extracommands.commands;

import com.mineinabyss.extracommands.ExtraCommands;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class HungerCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        label = command.getLabel().replace("extracommands:", "");
        String firstArg = args.length >= 1 ? args[0] : "";
        String secondArg = args.length >= 2 ? args[1] : "";
        Player player = sender instanceof Player ? (Player) sender : ExtraCommands.validatePlayer(secondArg, sender);

        if (!ExtraCommands.checkPermission(sender, "extracommands.hunger")) return false;

        if (label.equals("hunger")) {
            if (player == null) return false;

            int food;
            try {
                food = Integer.parseInt(firstArg);
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("Please specify a number").color(ExtraCommands.ERROR));
                return false;
            }
            player.setFoodLevel(food);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equals("hunger")) {
            return IntStream.range(1, 20).boxed().map(Object::toString).toList();
        }
        return new ArrayList<>();
    }
}
