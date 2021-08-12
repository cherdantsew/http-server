import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            ClientRequestHandler clientRequestHandler = new ClientRequestHandler(s);
            new Thread(clientRequestHandler).start();
        }
    }
}