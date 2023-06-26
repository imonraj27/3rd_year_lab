import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

class Sender {
    Socket socket;

    Sender() throws UnknownHostException, IOException {
        socket = new Socket("localhost", 2200);
    }

    static void div() {
        System.out.println("===============================================");
    }

    public void run() throws IOException {
        Scanner sc = new Scanner(System.in);
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
        while (true) {
            Sender.div();
            System.out.println("Enter the message you want to send: ");
            String msg = sc.nextLine();

            dout.writeUTF(msg);
            dout.flush();

            System.out.println("Do you want to send more messages? Y/N");
            String c = sc.nextLine();
            if (!c.equals("Y") && !c.equals("y"))
                break;

        }
        sc.close();
        dout.close();
    }
}