package app;

import app.http.ConnectionHandler;
import app.http.ServerProperties;
import app.http.request.RequestReader;
import app.http.response.ResponseProvider;
import app.http.response.ResponseWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final int BACKLOG = 500;
    private final ServerProperties serverProperties;
    private final RequestReader requestReader;
    private final ResponseWriter responseWriter;
    private final ResponseProvider responseProvider;
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    public Server(ServerProperties serverProperties, RequestReader requestReader, ResponseWriter responseWriter, ResponseProvider responseProvider) {
        this.serverProperties = serverProperties;
        this.requestReader = requestReader;
        this.responseProvider = responseProvider;
        this.responseWriter = responseWriter;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(serverProperties.getPort(), BACKLOG);
        LOGGER.log(Level.INFO, "Server started.");
        Executor executorService = Executors.newFixedThreadPool(20);
        while (true) {
            ConnectionHandler connectionHandler = new ConnectionHandler(requestReader, responseWriter, serverSocket.accept(), responseProvider, serverProperties);
            LOGGER.log(Level.INFO, "Client accepted.");
            System.out.println("Total accepted clients " + atomicInteger.incrementAndGet());
            executorService.execute(connectionHandler);
        }
    }
}