package app;

import app.http.ConnectionHandler;
import app.http.RequestReader;
import app.http.ResponseProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final Integer port;
    private final Integer bufferSize;
    private final String pathToResources;
    private final RequestReader requestReader;
    private final ResponseProvider responseProvider;

    public Server(Integer port, String pathToResources, Integer bufferSize, RequestReader readRequest, ResponseProvider responseProvider) {
        this.port = port;
        this.pathToResources = pathToResources;
        this.requestReader = readRequest;
        this.responseProvider = responseProvider;
        this.bufferSize = bufferSize;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        LOGGER.log(Level.INFO, "Server started.");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            LOGGER.log(Level.INFO, "Client accepted.");
            ConnectionHandler connectionHandler = new ConnectionHandler(requestReader, clientSocket, responseProvider, pathToResources, bufferSize);
            new Thread(connectionHandler).start();
        }
    }
}

