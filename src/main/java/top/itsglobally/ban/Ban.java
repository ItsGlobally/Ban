package top.itsglobally.ban;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import top.itsglobally.ban.command.BanCommand;
import top.itsglobally.ban.command.UnbanCommand;
import top.itsglobally.ban.data.Config;
import top.itsglobally.ban.storage.BanStorage;
import top.itsglobally.ban.storage.impl.SqliteStorage;

public final class Ban extends JavaPlugin {

    @Getter
    private static Ban plugin;
    @Getter
    private BanStorage storage;
    @Getter
    private Config configClass;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        configClass = new Config();
        if (configClass.getMethod().equals("mysql")) {
            getLogger().warning("MySQL method is not fully done yet. Fallback to sqlite.");
            storage = new SqliteStorage();
        }
        else storage = new SqliteStorage();
        storage.connect();
        setupCommand();
    }

    public void setupCommand() {
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("unban").setExecutor(new UnbanCommand());

    }

    @Override
    public void onDisable() {
        storage.disconnect();
    }

    public String getMessage(String path) {
        return colorize(getConfig().getString("messages." + path, "&cMessage not found: " + path));
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
