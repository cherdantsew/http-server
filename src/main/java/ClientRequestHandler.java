import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRequestHandler implements Runnable {
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private static final Logger logger = Logger.getLogger(ClientRequestHandler.class.getName());

    public ClientRequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            Request request = HttpUtils.readRequestFromInputStream(inputStream);
            File requestedFile = getFile(request);
            Response response = HttpUtils.getResponse(request, requestedFile);
            HttpUtils.writeResponse(response, outputStream);
            socket.close();
            logger.log(Level.INFO, "Client processing finished.");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error occurred while trying to write response/getting response object.", e);
        }
    }

    private static File getFile(Request request) {
        String requestedResourcePath = request.getResource().replace('/', '\\');
        if ("\\".equals(requestedResourcePath)) {
             return new File(ResourcesRepository.C_RESOURCES + ResourcesRepository.HOME_PAGE_ADDRESS);
        }
        return new File(ResourcesRepository.C_RESOURCES + requestedResourcePath);
    }
}
