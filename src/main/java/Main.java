import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.err.println("Client accepted");
            ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientSocket);
            new Thread(clientRequestHandler).start();
        }
    }
}