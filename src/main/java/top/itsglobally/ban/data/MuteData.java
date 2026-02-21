package top.itsglobally.ban.data;

public class Mute {
    private String playerUuid;
    private String playerName;
    private String reason;
    private String mutedBy;
    private Long expiresAt;

    public String getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMutedBy() {
        return mutedBy;
    }

    public void setMutedBy(String mutedBy) {
        this.mutedBy = mutedBy;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isPermanent() {
        return expiresAt == null;
    }

    public boolean isExpired() {
        return !isPermanent() && expiresAt < System.currentTimeMillis();
    }
}