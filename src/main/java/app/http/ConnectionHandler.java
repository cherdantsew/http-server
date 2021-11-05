package app.http;

import app.ServerCache;
import app.http.request.Request;
import app.http.request.RequestReader;
import app.http.response.Response;
import app.http.response.ResponseProvider;
import app.http.response.ResponseWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
    private static final Logger LOGGER = Logger.getLogger(ConnectionHandler.class.getName());

    public ConnectionHandler(RequestReader requestReader, ResponseWriter responseWriter, Socket socket, ResponseProvider responseProvider, ServerProperties serverProperties, ServerCache serverCache) throws IOException {
        this.requestReader = requestReader;
        this.responseWriter = responseWriter;
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.responseProvider = responseProvider;
        this.serverProperties = serverProperties;
        this.serverCache = serverCache;
    }

    @Override
    public void run() {
        try (socket) {
            Request request = requestReader.readRequest(inputStream, serverProperties.getPathToResources());
            if (request == null) {
                return;
            }
            serverCache.put(request.getResource());
            Response response = responseProvider.getResponse(new File(request.getResource()));
            responseWriter.writeResponse(response, outputStream, serverProperties.getBufferSize(), serverCache);
            LOGGER.log(Level.INFO, "Client processing finished.");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error occurred while trying to write response/getting response object.", e);
        }
    }
}