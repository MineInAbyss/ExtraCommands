package com.mineinabyss.extracommands.commands;

import com.mineinabyss.extracommands.ExtraCommands;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SeenCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        label = command.getLabel().replace("extracommands:", "");
        String firstArg = args.length >= 1 ? args[0] : "";

        if (!ExtraCommands.checkPermission(sender, "extracommands.seen")) return false;

        if (label.equals("seen")) {
            if (firstArg.isEmpty()) {
                sender.sendMessage(Component.text("Please specify a player to look up").color(ExtraCommands.ERROR));
                return false;
            }

            OfflinePlayer player2 = Bukkit.getOfflinePlayer(firstArg);
            Date lastSeen = new Date(player2.getLastSeen());
            TimeSince timeSince = calculateTime(getDateDiff(lastSeen, TimeUnit.MILLISECONDS));
            String lastSeenTime = timeSince.days + " days " + timeSince.hours + " hours " + timeSince.minutes + " minutes " + timeSince.seconds + " seconds";

            if (timeSince.days == 0) {
                lastSeenTime = lastSeenTime.replace(timeSince.days + " days ", "");
                if (timeSince.hours == 0) {
                    lastSeenTime = lastSeenTime.replace(timeSince.hours + " hours ", "");
                    if (timeSince.minutes == 0) {
                        lastSeenTime = lastSeenTime.replace(timeSince.minutes + " minutes ", "");
                    }
                }
            }

            if (player2.isOnline()) sender.sendMessage(Component.text(firstArg + " is currently online").color(ExtraCommands.INFO));
            else if (player2.getLastSeen() == 0)
                sender.sendMessage(Component.text("A player with the name " + firstArg + " has never joined this server.").color(ExtraCommands.INFO));
            else
                sender.sendMessage(Component.text(firstArg + " was last seen ").color(ExtraCommands.INFO).append(Component.text(lastSeenTime).color(ExtraCommands.ERROR)).append(Component.text(" ago").color(ExtraCommands.INFO)));
        }
        return true;
    }

    private TimeSince calculateTime(long s) {
        int days = (int) TimeUnit.MILLISECONDS.toDays(s);
        long hours = TimeUnit.MILLISECONDS.toHours(s) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(s) - TimeUnit.HOURS.toMinutes(days);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(s) - TimeUnit.MINUTES.toSeconds(minutes);

        return new TimeSince(days, hours, minutes, seconds);

    }

    private static class TimeSince {
        public int days;
        public long hours;
        public long minutes;
        public long seconds;

        public TimeSince(int days, long hours, long minutes, long seconds) {
            this.days = days;
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }
    }

    private static long getDateDiff(Date seen, TimeUnit timeUnit) {
        long diffInMillies = Date.from(Instant.now()).getTime() - seen.getTime();
        return timeUnit.convert(diffInMillies, timeUnit);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equals("seen")) {
            return Arrays.stream(Bukkit.getOfflinePlayers()).limit(20).map(OfflinePlayer::getName).filter(Objects::nonNull).filter(s -> s.startsWith(args[0])).toList();
        }
        return new ArrayList<>();
    }
}
