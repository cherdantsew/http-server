package app;

import app.http.*;
import app.http.request.RequestReader;
import app.http.response.ResponseProvider;
import app.http.response.ResponseWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final ServerProperties serverProperties;
    private final RequestReader requestReader;
    private final ResponseWriter responseWriter;
    private final ResponseProvider responseProvider;

    public Server(ServerProperties serverProperties, RequestReader requestReader, ResponseWriter responseWriter, ResponseProvider responseProvider) {
        this.serverProperties = serverProperties;
        this.requestReader = requestReader;
        this.responseProvider = responseProvider;
        this.responseWriter = responseWriter;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(serverProperties.getPort());
        LOGGER.log(Level.INFO, "Server started.");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            LOGGER.log(Level.INFO, "Client accepted.");
            ConnectionHandler connectionHandler = new ConnectionHandler(requestReader, responseWriter, clientSocket, responseProvider, serverProperties);
            new Thread(connectionHandler).start();
        }
    }
}

