package app.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    private final RequestReader requestReader;
    private final ResponseProvider responseProvider;
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final String pathToResources;
    private static final Logger logger = Logger.getLogger(ConnectionHandler.class.getName());
    private final Integer bufferSize;

    public ConnectionHandler(RequestReader requestReader, Socket socket, ResponseProvider responseProvider, String pathToResources, Integer bufferSize) throws IOException {
        this.requestReader = requestReader;
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.responseProvider = responseProvider;
        this.pathToResources = pathToResources;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        try (socket) {
            Request request = requestReader.readRequest(inputStream, pathToResources);
            if (request == null) {
                return;
            }
            Response response = responseProvider.getResponse(new File(request.getResource()), bufferSize);
            ResponseWriter.writeResponse(response, outputStream);
            logger.log(Level.INFO, "Client processing finished.");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error occurred while trying to write response/getting response object.", e);
        }
    }
}
