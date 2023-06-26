import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
    public static void main(String args[]) {
        try {
            Scanner sc = new Scanner(System.in);
            Socket clsct = new Socket("127.0.0.1", 50604);
            DataInputStream din = new DataInputStream(clsct.getInputStream());
            DataOutputStream dout = new DataOutputStream(clsct.getOutputStream());
            while (true) {
                System.out.println("Enter the Logical address(IP):");
                String str1 = sc.next();
                dout.writeUTF(str1);
                dout.flush();
                String str = din.readUTF();
                System.out.println("The Physical Address is: " + str);
            }
        } catch (Exception e) {
            // clsct.close();
            System.out.println(e);
        }
    }
}