package app.http;

import java.util.Optional;
import java.util.Properties;

public class ServerProperties {
    private static final int DEFAULT_PORT_NUMBER = 8080;
    private static final String DEFAULT_RESOURCES_PATH = System.getProperty("user.dir");
    private static final Integer DEFAULT_BUFFER_SIZE = 1024;
    private static final long DEFAULT_CACHE_SIZE = 5242880;
    private static final String PORT = "port";
    private static final String RESOURCES_PATH = "resources_path";
    private static final String BUFFER_SIZE = "buffer_size";
    private static final String CACHE_SIZE = "cache_size";

    private final String pathToResources;
    private final int port;
    private final int bufferSize;
    private final long cacheSize;

    public ServerProperties(Properties props) {
        this.port = Optional.ofNullable(props.getProperty(PORT)).map(Integer::parseInt).orElse(DEFAULT_PORT_NUMBER);
        this.pathToResources = Optional.ofNullable(props.getProperty(RESOURCES_PATH)).orElse(DEFAULT_RESOURCES_PATH);
        this.bufferSize = Optional.ofNullable(props.getProperty(BUFFER_SIZE)).map(Integer::parseInt).orElse(DEFAULT_BUFFER_SIZE);
        this.cacheSize = Optional.ofNullable(props.getProperty(CACHE_SIZE)).map(Long::parseLong).orElse(DEFAULT_CACHE_SIZE);
    }

    public String getPathToResources() {
        return pathToResources;
    }

    public int getPort() {
        return port;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public long getCacheSize() {
        return cacheSize;
    }
}
