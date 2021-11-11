package app.http;

import app.ServerCache;
import app.http.request.Request;
import app.http.request.RequestReader;
import app.http.response.Response;
import app.http.response.ResponseProvider;
import app.http.response.ResponseWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    private final RequestReader requestReader;
    private final ResponseWriter responseWriter;
    private final ResponseProvider responseProvider;
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final ServerProperties serverProperties;
    private final ServerCache serverCache;
    private final ConcurrentMap<String, Session> sessions;
    private static final Logger LOGGER = Logger.getLogger(ConnectionHandler.class.getName());
    private final Timer timer;

    public ConnectionHandler(RequestReader requestReader, ResponseWriter responseWriter, Socket socket, ResponseProvider responseProvider, ServerProperties serverProperties, ServerCache serverCache, ConcurrentMap<String, Session> sessions, Timer timer) throws IOException {
        this.requestReader = requestReader;
        this.responseWriter = responseWriter;
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.responseProvider = responseProvider;
        this.serverProperties = serverProperties;
        this.serverCache = serverCache;
        this.sessions = sessions;
        this.timer = timer;
    }

    @Override
    public void run() {
        try (socket) {
            Request request = requestReader.readRequest(inputStream, serverProperties.getPathToResources(), sessions, timer);
            if (request == null) {
                return;
            }
            serverCache.put(request.getResource());
            Response response = responseProvider.getResponse(request);
            responseWriter.writeResponse(response, outputStream, serverProperties.getBufferSize(), serverCache);
            LOGGER.log(Level.INFO, "Client processing finished.");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error occurred while trying to write response/getting response object.", e);
        }
    }
}