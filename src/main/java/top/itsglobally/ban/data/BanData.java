package top.itsglobally.ban.data;


import java.util.UUID;

public record BanData(
        long id,
        UUID plyerUUID,
        String reason,
        Long expiresAt) {
    public boolean isPermanent() {
        return expiresAt == null;
    }

    public boolean isExpired() {
        return !isPermanent() && expiresAt < System.currentTimeMillis();
    }
}