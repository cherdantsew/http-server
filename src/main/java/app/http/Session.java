package app.http;

import java.util.TimerTask;
import java.util.UUID;

public class Session extends TimerTask {
    private final UUID uuid;
    private final long sessionCreationTime;
    private long lastRequestTime;
    private boolean isValid;

    public Session() {
        this.uuid = UUID.randomUUID();
        this.sessionCreationTime = this.lastRequestTime = System.currentTimeMillis();
        this.isValid = true;
    }

    @Override
    public void run() {
        if ((System.currentTimeMillis() - lastRequestTime) > 10000 && isValid) {
            isValid = false;
            System.out.println("Made session UUID = " + uuid + " invalid.");
        }
    }

    public void update() {
        this.lastRequestTime = System.currentTimeMillis();
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isNew() {
        return sessionCreationTime == lastRequestTime;
    }
}