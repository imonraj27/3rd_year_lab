import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {


            Scanner sc = new Scanner(System.in);

            if(sc.nextInt()==0){
                Sender sender = new Sender(4,"source_data.txt","data_file.txt");
                sender.preparePackets();
                sender.stopAndWait();
//                sender.goBackNArq();
//                sender.selectiveRepeatArq();
            }else{
                Receiver receiver = new Receiver(4,"data_file.txt");
                receiver.stopAndWait();
//                receiver.goBackNArq();
//                receiver.selectiveRepeatArq();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
