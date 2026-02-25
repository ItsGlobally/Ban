package top.itsglobally.ban.storage;

import top.itsglobally.ban.data.BanData;

import java.util.UUID;

public abstract class BanStorage {
    public abstract void connect();

    public abstract void addBan(UUID uuid, String reason, long expireTime);

    public abstract boolean isBanned(UUID uuid);

    public abstract void removeBan(UUID uuid);

    public abstract BanData getBan(UUID uuid);

    public abstract void disconnect();

}