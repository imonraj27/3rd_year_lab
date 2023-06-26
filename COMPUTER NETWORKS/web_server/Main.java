import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int i = 0;

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {

            while (true) {
                clientSocket = serverSocket.accept();

                if (clientSocket != null) {
                    System.out.println("Connected");
                } else {
                    System.out.println("not conected");
                }
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println("\r\n");
                out.println("<html><body><h1 color='red'>Hello </h1></body></html>");
                out.flush();
                i++;
                out.close();

                clientSocket.close();
            }

            // serverSocket.close();
        } catch (Exception e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

    }

}