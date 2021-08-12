import java.io.*;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println("Client processing finished");
    }

    private static File getFile(Request request) {
        String requestedResourcePath = request.getResource().replace('/', '\\');
        File requestedResource;
        if ("\\".equals(requestedResourcePath)) {
            requestedResource = new File(ResourcesRepository.C_RESOURCES + ResourcesRepository.HOME_PAGE_ADDRESS);
        } else requestedResource = new File(ResourcesRepository.C_RESOURCES + requestedResourcePath);
        return requestedResource;
    }
}
