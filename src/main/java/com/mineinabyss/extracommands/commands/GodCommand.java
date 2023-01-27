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

import java.util.List;

public class GodCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String commandLabel = command.getLabel().replace("extracommands:", "");
        String firstArg = args.length >= 1 ? args[0] : "";
        String secondArg = args.length >= 2 ? args[1] : "";
        Player player = sender instanceof Player ? (Player) sender : ExtraCommands.validatePlayer(secondArg, sender);

        if (!ExtraCommands.checkPermission(sender, "extracommands.god")) return false;

        if (commandLabel.startsWith("god")) {
            player.setInvulnerable(!player.isInvulnerable());
            String gm = player.isInvulnerable() ? "now in God Mode" : "is no longer in God Mode";
            sender.sendMessage(Component.text("Set " + ExtraCommands.miniMessage.serialize(player.displayName()) + " is " + gm).color(ExtraCommands.INFO));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
