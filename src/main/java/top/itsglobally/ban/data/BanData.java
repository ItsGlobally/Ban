package top.itsglobally.ban.data;

public class Ban {
    private String playerUuid;
    private String playerName;
    private String reason;
    private String bannedBy;
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

    public String getBannedBy() {
        return bannedBy;
    }

    public void setBannedBy(String bannedBy) {
        this.bannedBy = bannedBy;
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