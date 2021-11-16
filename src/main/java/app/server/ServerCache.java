package app.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerCache {
    private final long maxCacheSize;
    private long currentCacheSize = 0;
    private final ConcurrentMap<String, byte[]> cacheMap = new ConcurrentHashMap<>();

    public ServerCache(long maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public byte[] getResource(String fileName) {
        return cacheMap.get(fileName);
    }

    public boolean contains(String fileName) {
        return cacheMap.containsKey(fileName);
    }

    public void put(String filename) throws IOException {
        File file = new File(filename);
        if (!file.isFile() || !enoughSpace(filename) || cacheMap.containsKey(filename))
            return;
        byte[] bytes = Files.readAllBytes(Path.of(new File(filename).getAbsolutePath()));
        cacheMap.putIfAbsent(filename, bytes);
        currentCacheSize += new File(filename).length();
    }

    private boolean enoughSpace(String fileName) {
        long fileSize = new File(fileName).length();
        return (maxCacheSize - currentCacheSize) >= fileSize;
    }
}