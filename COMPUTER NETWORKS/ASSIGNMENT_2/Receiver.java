import org.w3c.dom.xpath.XPathResult;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Receiver {
    String data_file;
    ArrayList<StringBuilder> packets = new ArrayList<StringBuilder>();
    int packet_size;
    ServerSocket ss;
    Socket s;
    DataInputStream dis;
    DataOutputStream dout;

    public Receiver(int packet_size, String data_file) throws IOException {
        this.data_file = data_file;
        this.packet_size = packet_size;
        ss=new ServerSocket(6666);
        s=ss.accept();//establishes connection
        dis=new DataInputStream(s.getInputStream());
        dout=new DataOutputStream(s.getOutputStream());
    }





    public boolean isSame(char c1, char c2){
        return c1==c2;
    }

    public void stopAndWait() throws InterruptedException, IOException {
        String noofpack = dis.readUTF();
        FlowControl.no_of_packets = Integer.valueOf(noofpack);
        int idendifier = 1;
        int i = 0;
//        FileReader fr = new FileReader(new File(this.data_file));
        StringBuilder str = new StringBuilder("");


        System.out.println("RECEIVER IS READY##");
        while(true){
            str = new StringBuilder("");
            while(str.length()==0){
                String  tempstr=(String)dis.readUTF();
                str = new StringBuilder(tempstr);
            }

            if(!isSame((char) ('0'+idendifier),str.charAt(this.packet_size))){
                idendifier = (idendifier+1)%2;
                i++;

            }



            if(Math.random()<0.4){
                dout.writeUTF("false "+"$$");
            }else{
                this.packets.add(new StringBuilder(str.substring(0,this.packet_size)));
                System.out.println(i + " - message= "+str);
                dout.writeUTF("true "+i);
                if(i==FlowControl.no_of_packets) break;
            }


        }
        dis.readUTF();
        ss.close();

        System.out.print("COMPLETE RECEIVED PACKETS: ");
        System.out.println(this.packets);
    }

    public void goBackNArq() throws IOException, InterruptedException {
        System.out.println("===========GO BACK N ARQ===========");
        String noofpack = dis.readUTF();
        FlowControl.no_of_packets = Integer.valueOf(noofpack);

        Socket ack_socket = new Socket("localhost",4500);
        dout = new DataOutputStream(ack_socket.getOutputStream());



        int expected = 0;
        while(expected<FlowControl.no_of_packets){
            String pack = dis.readUTF();
            String[] args = pack.split(" ");
            if(Integer.parseInt(args[1])==expected){
                if(Math.random()>0.4){
                    System.out.println("Frame "+ args[1]+": "+args[0]);
                    System.out.println("ack sent for frame: "+args[1]);
                    dout.writeUTF(expected+"");
                    dout.flush();
                    expected++;
                }else{

                }
            }else if(Integer.parseInt(args[1])<expected){
                dout.writeUTF(args[1]+"");
                dout.flush();
            }
        }
        dout.writeUTF("finish");
        dout.flush();
        dis.readUTF();
    }

    public void selectiveRepeatArq() throws IOException {
        System.out.println("===========SELECTIVE REPEAT===========");
        String noofpack = dis.readUTF();
        FlowControl.no_of_packets = Integer.valueOf(noofpack);

        Socket ack_socket = new Socket("localhost",4500);
        dout = new DataOutputStream(ack_socket.getOutputStream());



        int count = 0;
        while(count<FlowControl.no_of_packets){
            String pack = dis.readUTF();
            String[] args = pack.split(" ");

            if(Math.random()>0.5){
                System.out.println("Frame "+ args[1]+": "+args[0]);
                System.out.println("ack sent for frame: "+args[1]);
                 dout.writeUTF(args[1]+"");
                 dout.flush();
                 count++;
            }else{

            }

        }
        dout.writeUTF("finish");
        dout.flush();
        dis.readUTF();
    }
}
