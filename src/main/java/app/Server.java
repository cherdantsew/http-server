package app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final Integer port;
    private final String pathToResources;

    public Server(Integer port, String pathToResources) {
        this.port = port;
        this.pathToResources = pathToResources;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            LOGGER.log(Level.INFO, "Client accepted.");
            ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientSocket, pathToResources);
            new Thread(clientRequestHandler).start();
        }
    }
}
