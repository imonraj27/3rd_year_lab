import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

public class Server {
    Timestamp[] ip_times = new Timestamp[5];
    boolean[] ip_takens = new boolean[5];
    int[] who_gets = new int[5];

    int MAXTIME = 15000;

    void exec() throws IOException {
        ServerSocket ss = new ServerSocket(40000);
        Socket socket = ss.accept();

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

        while (true) {
            String str = dis.readUTF();
            String[] arr = str.split(" ", 3);

            if (arr[0].equals("NEWIP")) {
                boolean found = false;
                for (int i = 0; i < 5; i++) {
                    if (ip_takens[i] == false) {
                        ip_times[i] = new Timestamp(new java.util.Date().getTime());
                        ip_takens[i] = true;
                        who_gets[i] = Integer.parseInt(arr[1]);
                        dout.writeUTF("SUCCESS 120.120.30." + i);
                        dout.flush();
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    dout.writeUTF("FAILURE");
                    dout.flush();
                }
            } else {
                System.out.println(arr[2]);
                String[] arr2 = arr[2].split("[.]");
                int id = Integer.parseInt(arr2[3]);
                if (ip_takens[id] == true && who_gets[id] == Integer.parseInt(arr[1])) {
                    ip_times[id] = new Timestamp(new java.util.Date().getTime());
                    dout.writeUTF("SUCCESS");
                    dout.flush();
                } else {
                    dout.writeUTF("FAILURE");
                    dout.flush();
                }

            }

        }
    }

    void updateTable() {
        Thread th = new Thread() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < 5; i++) {
                        if (ip_takens[i] == false)
                            continue;

                        Timestamp ts = new Timestamp(new java.util.Date().getTime());
                        if (ts.getTime() - ip_times[i].getTime() >= MAXTIME) {
                            System.out.println("IP 120.120.30." + i + " EXPIRED");
                            ip_takens[i] = false;
                        }
                    }
                }
            }
        };
        th.start();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.updateTable();
        try {
            server.exec();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}