import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileWriter;

class Server {
    ServerSocket ss;
    Socket soc;

    Server() throws IOException {
        ss = new ServerSocket(2000);
        soc = ss.accept();

        while (true) {
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            String[] received = ((String) dis.readUTF()).split(" ", 3);
            String op = received[0];
            String filename = received[1];

            System.out.println(filename);
            String str;

            if (op.equals("READ")) {
                try {
                    Path fileName = Path.of(".\\public\\" + filename);
                    str = Files.readString(fileName);
                } catch (Exception e) {
                    str = "FILE NOT FOUND!!";
                }

                DataOutputStream dout = new DataOutputStream(soc.getOutputStream());

                dout.writeUTF(str);
                dout.flush();
            } else if (op.equals("UPDATE")) {
                try {
                    FileWriter fWriter = new FileWriter("./public/" + filename);
                    fWriter.write(received[2]);
                    System.out.println(received[2]);
                    fWriter.close();

                    DataOutputStream dout = new DataOutputStream(soc.getOutputStream());

                    dout.writeUTF("SUCCESS");
                    dout.flush();
                } catch (Exception e) {
                    DataOutputStream dout = new DataOutputStream(soc.getOutputStream());

                    dout.writeUTF("ERROR");
                    dout.flush();
                }
            }
        }

        // ss.close();
    }

    public static void main(String[] args) {
        try {
            new Server();
        } catch (IOException e) {
        }
    }
}