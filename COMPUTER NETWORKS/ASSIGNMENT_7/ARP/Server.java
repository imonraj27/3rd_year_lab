import java.io.*;
import java.net.*;

class Server {
    public static void main(String args[]) {
        try {
            ServerSocket obj = new ServerSocket(50604);
            Socket obj1 = obj.accept();
            DataInputStream din = new DataInputStream(obj1.getInputStream());
            DataOutputStream dout = new DataOutputStream(obj1.getOutputStream());

            String ip[] = { "165.165.80.80", "165.165.79.1" };
            String mac[] = { "6A:08:AA:C2:A3:23", "8A:BC:E3:FA:AE:79" };
            while (true) {
                String str = din.readUTF();
                System.out.println(str);
                boolean found = false;
                for (int i = 0; i < ip.length; i++) {
                    if (str.equals(ip[i])) {
                        dout.writeUTF(mac[i] + '\n');
                        dout.flush();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    dout.writeUTF("No such Ip");
                    dout.flush();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}