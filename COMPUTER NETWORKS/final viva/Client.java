import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;

    Client() throws Exception {
        socket = new Socket("localhost", 4444);
    }

    void run_process() throws Exception {
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
        DataInputStream din = new DataInputStream(socket.getInputStream());

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Domain: ");
        String domain = sc.next();

        dout.writeUTF(domain);
        dout.flush();

        String ip = din.readUTF();

        System.out.println("Ip is: " + ip);

        sc.close();
    }
}