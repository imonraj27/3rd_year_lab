import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Receiver {
    ServerSocket server;
    Socket socket;

    static void div() {
        System.out.println("===============================================");
    }

    Receiver() throws IOException {
        server = new ServerSocket(2200);
    }

    public void run() throws IOException {
        socket = server.accept();
        DataInputStream din = new DataInputStream(socket.getInputStream());

        while (true) {
            Receiver.div();
            String msg = din.readUTF();

            System.out.println("MESSAGE GOT: ");
            System.out.println(msg);
        }
    }
}
