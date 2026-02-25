package top.itsglobally.ban.data;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class Config implements Global{
    private String method;
    private String address;
    private String database;
    private String username;
    private String password;


    public Config() {
        FileConfiguration config = plugin.getConfig();

        this.method = config.getString("storage.method");
        this.address = config.getString("storage.address");
        this.database = config.getString("storage.database");
        this.username = config.getString("storage.username");
        this.password = config.getString("storage.password");

        boolean changed = false;

        if (method == null || method.isEmpty() ||
                (!method.equalsIgnoreCase("sqlite") &&
                        !method.equalsIgnoreCase("mysql"))) {

            config.set("storage.method", "sqlite");
            this.method = "sqlite";
            changed = true;
        }

        if (this.method.equalsIgnoreCase("mysql")) {

            if (address == null || address.isEmpty()) {
                config.set("storage.address", "localhost");
                this.address = "localhost";
                changed = true;
            }

            if (database == null || database.isEmpty()) {
                config.set("storage.database", "database");
                this.database = "kanade";
                changed = true;
            }

            if (username == null || username.isEmpty()) {
                config.set("storage.username", "root");
                this.username = "root";
                changed = true;
            }

            if (password == null) {
                config.set("storage.password", "password");
                this.password = "KanadeSoCute";
                changed = true;
            }
        }

        if (changed) {
            plugin.saveConfig();
            plugin.getLogger().warning("Config had invalid or missing values. I've reset the invalid or missing values to default.");
        }
    }
}
