package top.itsglobally.ban.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.itsglobally.ban.data.Global;
import top.itsglobally.ban.util.Async;
import top.itsglobally.ban.util.TimeParser;

import java.util.List;

public class BanCommand implements Global, CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /ban <player> [time] [reason]");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        Async.supply(
                () -> plugin.getStorage().getBan(target.getUniqueId()),
                isBanned -> {

                    if (isBanned != null) {
                        sender.sendMessage(ChatColor.RED + "That player is already banned!");
                        return;
                    }

                    String reason = "No reason specified";
                    Long duration = null;
                    long expiresAt = -1L;

                    if (args.length > 1) {
                        duration = TimeParser.parseTime(args[1]);

                        if (duration != null) {
                            expiresAt = System.currentTimeMillis() + duration;
                            if (args.length > 2) {
                                reason = String.join(" ",
                                        java.util.Arrays.copyOfRange(args, 2, args.length));
                            }
                        } else {
                            reason = String.join(" ",
                                    java.util.Arrays.copyOfRange(args, 1, args.length));
                        }
                    }

                    long finalExpiresAt = expiresAt;
                    String finalReason = reason;

                    Async.run(() ->
                            plugin.getStorage().addBan(
                                    target.getUniqueId(),
                                    finalReason,
                                    finalExpiresAt
                            )
                    );

                    Player online = Bukkit.getPlayer(target.getUniqueId());
                    if (online != null && online.isOnline()) {

                        String kickMessage;

                        if (finalExpiresAt != -1L) {
                            kickMessage = plugin.getMessage("ban")
                                    .replace("{reason}", finalReason)
                                    .replace("{time}",
                                            TimeParser.formatTimestamp(finalExpiresAt));
                        } else {
                            kickMessage = plugin.getMessage("ban_perm")
                                    .replace("{reason}", finalReason);
                        }

                        online.kickPlayer(kickMessage);
                    }

                    sender.sendMessage(ChatColor.GREEN +
                            "Player " + target.getName() + " has been banned.");
                }
        );

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}
