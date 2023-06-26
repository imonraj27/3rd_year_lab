
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        try {
            Socket s = new Socket("localhost", 40000);
            Scanner sc = new Scanner(System.in);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            while (true) {
                System.out.println("\n----------------\n1. DISCOVER NEW IP");
                System.out.println("2. RENEW IP");
                System.out.println("0. EXIT");

                System.out.print("ENTER CHOICE: ");
                int choice = sc.nextInt();
                System.out.print("YOUR CLIENT_NO(AS IF PORT): ");
                int port = sc.nextInt();

                if (choice == 1) {
                    dout.writeUTF("NEWIP " + port);
                    dout.flush();

                    String[] arr = dis.readUTF().split(" ", 2);

                    if (arr[0].equals("SUCCESS")) {
                        System.out.println("CLIENT " + port + " IS ASSIGNED " + arr[1]);
                        map.put(port, arr[1]);
                    } else {
                        System.out.println("NO IP IS AVAILABLE..");
                    }
                } else if (choice == 2) {
                    dout.writeUTF("RENEWIP " + port + " " + map.get(port));
                    dout.flush();

                    String str = dis.readUTF();

                    if (str.equals("SUCCESS")) {
                        System.out.println("CLIENT " + port + " IS RENEWED " + map.get(port));
                    } else {
                        System.out.println("SORRY, IP EXPIRED..");
                        map.remove(port);
                    }
                } else
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
