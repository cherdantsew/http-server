package app;
import app.httpUtils.*;
import app.responses.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRequestHandler implements Runnable {
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final String pathToResources;

    private static final Logger logger = Logger.getLogger(ClientRequestHandler.class.getName());

    public ClientRequestHandler(Socket socket, String pathToResources) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.pathToResources = pathToResources;
    }

    @Override
    public void run() {
        try {
            Request request = HttpUtils.readRequestFromInputStream(inputStream, pathToResources);
            if (request == null) {
                return;
            }
            Response response = HttpUtils.buildResponse(request);
            response.write(outputStream);
            socket.close();
            logger.log(Level.INFO, "Client processing finished.");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error occurred while trying to write response/getting response object.", e);
        }
    }

    private File getFile(Request request) {
        return new File(pathToResources + request.getResource());
    }
}
