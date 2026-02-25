package top.itsglobally.ban.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.itsglobally.ban.data.Global;
import top.itsglobally.ban.util.Async;

import java.util.List;

public class UnbanCommand implements Global, CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /unban <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);
        Async.supply(() -> plugin.getStorage().getBan(target.getUniqueId()),
                banData -> {
                    if (banData == null) {
                        commandSender.sendMessage(ChatColor.RED + "That player is not banned!");
                        return;
                    }

                    Async.run(() -> plugin.getStorage().removeBan(target.getUniqueId()));
                    commandSender.sendMessage(ChatColor.GREEN + "You have unbanned " + target.getName() + "!");
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}
