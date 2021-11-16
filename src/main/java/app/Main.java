package app;

import app.http.request.RequestReader;
import app.http.response.ResponseProvider;
import app.http.response.ResponseWriter;
import app.server.ServerProperties;
import app.server.Server;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            initServer(args).start();
            LOGGER.log(Level.INFO, "Server stopped");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "error while server init", e);
        }
    }

    private static Server initServer(String[] args) throws IOException {
        Properties props = PropertyFileReader.getProps(args);
        ServerProperties serverProperties = new ServerProperties(props);
        return new Server(serverProperties, new RequestReader(), new ResponseWriter(), new ResponseProvider());
    }
}