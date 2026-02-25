package top.itsglobally.ban.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import top.itsglobally.ban.data.BanData;
import top.itsglobally.ban.data.Global;
import top.itsglobally.ban.util.Async;
import top.itsglobally.ban.util.TimeParser;

public class Listener implements Global, org.bukkit.event.Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        BanData banData = plugin.getStorage().getBan(event.getUniqueId());
        if (banData == null) return;

        String kickMessage;
        if (banData.expiresAt() != -1L) {
            kickMessage = plugin.getMessage("ban")
                    .replace("{reason}", banData.reason())
                    .replace("{time}",
                            TimeParser.formatTimestamp(banData.expiresAt()));
        } else {
            kickMessage = plugin.getMessage("ban_perm")
                    .replace("{reason}", banData.reason());
        }

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMessage);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {

    }
}
