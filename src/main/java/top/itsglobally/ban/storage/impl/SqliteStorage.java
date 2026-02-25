package top.itsglobally.ban.storage.impl;

import top.itsglobally.ban.data.BanData;
import top.itsglobally.ban.data.Global;
import top.itsglobally.ban.storage.BanStorage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class SqliteStorage extends BanStorage implements Global {

    private final File dbFile;
    private Connection dbConnection;

    public SqliteStorage() {
        dbFile = new File(plugin.getDataFolder(), "database.db");
        if (!dbFile.exists()) {
            try {
                dbFile.getParentFile().mkdirs();
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            String sql = """
                CREATE TABLE IF NOT EXISTS bans (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    uuid TEXT NOT NULL,
                    reason TEXT,
                    start BIGINT,
                    end BIGINT,
                    active BOOLEAN
                )
                """;
            try (Statement stmt = dbConnection.createStatement()) {
                stmt.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBan(UUID uuid, String reason, long expireTime) {
        String sql = "INSERT INTO bans (uuid, reason, start, end, active) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, reason);
            stmt.setLong(3, System.currentTimeMillis());
            stmt.setLong(4, expireTime);
            stmt.setBoolean(5, true);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BanData getBan(UUID uuid) {
        String sql = """
            SELECT id, reason, start, end
            FROM bans
            WHERE uuid = ? AND active = 1
            ORDER BY start DESC
            LIMIT 1
            """;

        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    int id = rs.getInt("id");
                    String reason = rs.getString("reason");
                    long end = rs.getLong("end");


                    if (end <= System.currentTimeMillis()) {
                        removeBanById(id);
                        return null;
                    }



                    return new BanData(id, uuid, reason, end);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isBanned(UUID uuid) {
        String sql = """
            SELECT id, end 
            FROM bans
            WHERE uuid = ? AND active = 1
            ORDER BY start DESC
            LIMIT 1
            """;
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long endTime = rs.getLong("end");
                    int id = rs.getInt("id");

                    if (endTime == -1) return true;

                    if (endTime <= System.currentTimeMillis()) return true;

                    removeBanById(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeBan(UUID uuid) {
        String sql = """
            UPDATE bans
            SET active = 0
            WHERE id = (
                SELECT id FROM bans WHERE uuid = ? AND active = 1
                ORDER BY start DESC
                LIMIT 1
            )
            """;
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeBanById(int id) {
        String sql = "UPDATE bans SET active = 0 WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}