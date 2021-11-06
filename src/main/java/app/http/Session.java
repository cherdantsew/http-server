package app.http;

import java.util.UUID;

public class Session {
    private final UUID uuid;
    private final long sessionCreationTime;
    private long lastRequestTime;

    public Session() {
        this.uuid = UUID.randomUUID();
        this.sessionCreationTime = System.currentTimeMillis();
        this.lastRequestTime = sessionCreationTime;
    }

    public void update() {
        this.lastRequestTime = System.currentTimeMillis();
    }

    public UUID getUuid() {
        return uuid;
    }
}