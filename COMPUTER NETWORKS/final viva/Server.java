import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
    ServerSocket ss;
    Socket socket;

    HashMap<String, HashMap<String, String>> global = new HashMap<String, HashMap<String, String>>();

    Server() throws Exception {
        ss = new ServerSocket(4444);
    }

    void fillMap() {
        global.put("com", new HashMap<>());
        global.get("com").put("facebook", "123.123.45.56");
        global.get("com").put("google", "127.123.95.56");
        global.put("in", new HashMap<>());
        global.get("in").put("jadavpur", "187.3.9.5");

    }

    void run_process() throws Exception {
        fillMap();
        socket = ss.accept();
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
        DataInputStream din = new DataInputStream(socket.getInputStream());

        String domain = din.readUTF();
        String[] clips = domain.split("[.]");

        HashMap map = global;
        int n = clips.length;
        String ans = "";

        for (int i = n - 1; i >= 0; i--) {
            if (i == 0) {
                ans = (String) map.get((String) clips[i]);
                break;
            } else {
                map = (HashMap) map.get(clips[i]);
            }
        }

        dout.writeUTF(ans);
        dout.flush();

    }
}