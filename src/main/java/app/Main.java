package app;

import java.io.*;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final int DEFAULT_PORT_NUMBER = 8080;
    private static final String DEFAULT_RESOURCES_PATH = System.getProperty("user.dir");
    private static final String PORT = "port";
    private static final String RESOURCES_PATH = "resources_path";

    public static void main(String[] args) {
        try {
            initServer(args).start();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "error while server init", e);
        }
    }

    private static Server initServer(String[] args) throws IOException {
        Properties props = PropertyFileReader.getProps(args);
        int port = Optional.ofNullable(props.getProperty(PORT)).map(Integer::parseInt).orElse(DEFAULT_PORT_NUMBER);
        String pathToResource = Optional.ofNullable(props.getProperty(RESOURCES_PATH)).orElse(DEFAULT_RESOURCES_PATH);
        return new Server(port, pathToResource);
    }
}