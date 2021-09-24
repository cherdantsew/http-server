import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int DEFAULT_PORT_NUMBER = 8080;
    private static final String PORT = "port";

    public static void main(String[] args) throws IOException {
        PropertyFileReader propertyFileReader = PropertyFileReader.getInstance(args);
        int port = Integer.parseInt(propertyFileReader.getProperty(PORT));
        ServerSocket serverSocket = new ServerSocket(port == 0 ? DEFAULT_PORT_NUMBER : port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            logger.log(Level.INFO, "Client accepted.");
            ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientSocket);
            new Thread(clientRequestHandler).start();
        }
    }
}