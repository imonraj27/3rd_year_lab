import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;

class PacketSenderThread extends Thread {
    Socket sending_socket;
    ArrayList<StringBuilder> packets;
    Integer window_start;
    Integer window_end;
    int window_size;
    DataOutputStream dout;

    PacketSenderThread(ArrayList<StringBuilder> packets, Integer ws, Integer we) throws IOException {
        this.packets = new ArrayList<>(packets);
        this.sending_socket = new Socket("localhost",6666);
        this.window_start = ws;
        this.window_end = we;
        this.window_size = 4;

    }

    void beginProcess() throws IOException {
        dout=new DataOutputStream(sending_socket.getOutputStream());
        dout.writeUTF(packets.size()+"");
        dout.flush();
    }

    synchronized void sendPack() throws IOException, InterruptedException {
        if(window_end<packets.size()){
            System.out.println("Sent Frame- "+ window_end);
            FlowControl.updateAcks(window_end,false);
            dout.writeUTF(String.valueOf(packets.get(window_end))+" "+window_end.toString());
            dout.flush();
        }
        window_end++;
    }

    @Override
    public void run() {
        try{
            while(true){
                if(window_start==packets.size()) {
                    while(!FlowControl.isFinish);
                    dout.writeUTF("finish");
                    dout.flush();
                    break;
                }
                while(window_end.intValue()-window_start.intValue()<window_size){
                    if(FlowControl.isFinish) break;
                   sendPack();
                    Thread.sleep(1000);
                }

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                long start_time = timestamp.getTime();
                boolean isR = false;
                while(window_start<packets.size()){
                    timestamp = new Timestamp(System.currentTimeMillis());
                    if(timestamp.getTime()>start_time+2000){
                        System.out.println("Timeout Resending Frame#");
                        window_end = Integer.valueOf(window_start);
                        break;
                    }
                    if(FlowControl.acks.get(window_start)){
                        System.out.println("Ack Get - "+window_start++);
//                        window_start++;
                        break;
//                        isR = true;
                    }
                }
            }
        }catch (Exception e){

        }

    }
}
class PacketSenderThreadSelectiveRep extends Thread {
    Socket sending_socket;
    ArrayList<StringBuilder> packets;
    Integer window_start;
    Integer window_end;
    int window_size;
    DataOutputStream dout;
    int count=0;

    PacketSenderThreadSelectiveRep(ArrayList<StringBuilder> packets, Integer ws, Integer we) throws IOException {
        this.packets = new ArrayList<>(packets);
        this.sending_socket = new Socket("localhost",6666);
        this.window_start = ws;
        this.window_end = we;
        this.window_size = 4;

    }

    void beginProcess() throws IOException {
        dout=new DataOutputStream(sending_socket.getOutputStream());
        dout.writeUTF(packets.size()+"");
        dout.flush();
    }

    synchronized void sendPack() throws IOException, InterruptedException {
        if(window_end<packets.size() && FlowControl.acks.get(window_end) == false) {
            System.out.println("Sent Frame- " + window_end);
            dout.writeUTF(String.valueOf(packets.get(window_end)) + " " + window_end.toString());
            dout.flush();
        }
        window_end++;
    }

    @Override
    public void run() {
        try{
            while(true){
                if(count==packets.size() ) {
                    while(!FlowControl.isFinish);
                    dout.writeUTF("finish");
                    dout.flush();
                    break;
                }
                while(window_end.intValue()-window_start.intValue()<window_size){
                    if(FlowControl.isFinish) break;
                    sendPack();
                    Thread.sleep(1000);
                }

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                long start_time = timestamp.getTime();
                boolean isR = false;
                while(window_start<packets.size() && window_start<window_end){
                    timestamp = new Timestamp(System.currentTimeMillis());
                    if(timestamp.getTime()>start_time+2000){
                        System.out.println("Timeout Resending Frame#");
                        window_end = Integer.valueOf(window_start);
                        break;
                    }
                    if(FlowControl.acks.get(window_start)){
                        window_start++;
                        count++;
                    }
                }
            }
        }catch (Exception e){

        }

    }
}

class AckTakingThread extends Thread {
    ServerSocket ss;
    Socket ack_socket;
    DataInputStream dis;
    AckTakingThread() throws IOException {
        this.ss = new ServerSocket(4500);
        this.ack_socket = ss.accept();
        dis=new DataInputStream(ack_socket.getInputStream());
    }

    synchronized private boolean safeZone(String st){
        if(st.equals("finish")){
            FlowControl.isFinish = true;
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        try {
            while (true){
                String st = dis.readUTF();
                if(safeZone(st)) break;
                int k = Integer.valueOf(st);

                FlowControl.updateAcks(k,true);
            }
        } catch (Exception e) {

        }
    }
}

class AckTakingThreadSelectiveRep extends Thread {
    ServerSocket ss;
    Socket ack_socket;
    DataInputStream dis;
    AckTakingThreadSelectiveRep() throws IOException {
        this.ss = new ServerSocket(4500);
        this.ack_socket = ss.accept();
        dis=new DataInputStream(ack_socket.getInputStream());
    }

    synchronized private boolean safeZone(String st){
        if(st.equals("finish")){
            FlowControl.isFinish = true;
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        try {
            while (true){
                String st = dis.readUTF();
                if(safeZone(st)) break;
                int k = Integer.valueOf(st);
                System.out.println("Ack Get - "+k);
                FlowControl.updateAcks(k,true);
            }
        } catch (Exception e) {

        }
    }
}


public class Sender{
    String data_file; //to be removed
    String source_data_file;
    ArrayList<StringBuilder> packets = new ArrayList<StringBuilder>();
    int packet_size;
    Socket s;
    DataOutputStream dout;
    DataInputStream dis;

   public Sender(int packet_size, String source_data_file, String data_file) throws IOException {
       this.source_data_file = source_data_file;
       this.data_file = data_file;
       this.packet_size = packet_size;
   }

  public void preparePackets() throws IOException {
      FileReader fr = new FileReader(new File(this.source_data_file));
      StringBuilder str = new StringBuilder("");

      int k;
      while((k=fr.read())!=-1){
          str.append((char) k);
      }

      int len = str.length();

      FlowControl.no_of_packets = len/this.packet_size;

      for(int i=0; i<FlowControl.no_of_packets; i++){
          StringBuilder stt = new StringBuilder("");
          for(int j=0; j<this.packet_size; j++){
              stt.append(str.charAt(i*this.packet_size+j));
          }
          packets.add(stt);
      }
      System.out.print("COMPLETE PACKETS TO SEND: ");
      System.out.println(this.packets);
      FlowControl.setAcks();
  }

  public void transmit(StringBuilder datastr) throws IOException{
      System.out.println("sent-"+datastr.toString());
      dout.writeUTF(datastr.toString());
      dout.flush();

  }

  public void stopAndWait() throws InterruptedException, IOException {
      this.s=new Socket("localhost",6666);
      dout=new DataOutputStream(s.getOutputStream());
      dis=new DataInputStream(s.getInputStream());
    int idendifier = 1;
    int i = -1;

      dout.writeUTF(String.valueOf(FlowControl.no_of_packets));
      dout.flush();

    while(true){
        if(i==-1 || FlowControl.acks.get(i)){
            idendifier = (idendifier+1)%2;
            i++;
            if(i==FlowControl.no_of_packets) break;
        }else{
            System.out.println("No acknowledgement Received - Retransmition..");
        }

        StringBuilder datastr = new StringBuilder(this.packets.get(i));
        datastr.append(idendifier);
        FlowControl.acks.set(i,false);
        transmit(datastr);

        int maxtime = 2000,time=0;
        Thread.sleep(maxtime);
        String ackQ = dis.readUTF();

        String[] arr = ackQ.split(" ");
        boolean ack = Boolean.valueOf(arr[0]);
        if(ack){
            FlowControl.acks.set(i,true);
        }
    }
    dout.writeUTF("finish");
    dout.flush();
      s.close();
  }

  public void goBackNArq() throws IOException, InterruptedException {
       PacketSenderThread t1 = new PacketSenderThread(packets,0,0);
      t1.beginProcess();
        AckTakingThread t2 = new AckTakingThread();

        t1.start();
        t2.start();
  }

  public void selectiveRepeatArq() throws IOException {
      PacketSenderThreadSelectiveRep t1 = new PacketSenderThreadSelectiveRep(packets,0,0);
      t1.beginProcess();
      AckTakingThreadSelectiveRep t2 = new AckTakingThreadSelectiveRep();

      t1.start();
      t2.start();
  }
}
