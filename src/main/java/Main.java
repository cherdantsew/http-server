import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static String pathToPropertyFile = null;

    public static void main(String[] args) throws IOException {
        if (args.length > 0)
            pathToPropertyFile = args[0];
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            logger.log(Level.INFO, "Client accepted.");
            ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientSocket);
            new Thread(clientRequestHandler).start();
        }
    }
}