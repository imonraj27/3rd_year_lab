package ErrorDetection;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        Sender sender = new Sender("./ErrorDetection/sourcedata.txt",4);
        Receiver receiver = new Receiver("./ErrorDetection/channel_passed_data.txt",4);
        Channel channel = new Channel("./ErrorDetection/sent_data.txt","./ErrorDetection/channel_passed_data.txt");
        try {
//            sender.loadData();
//            sender.createCheckSumData("./ErrorDetection/sent_data.txt");
//            channel.introduceRandomError();
//            receiver.loadData();
//            receiver.validateCheckSumData();


//            sender.loadData();
//            sender.createCRCData("./ErrorDetection/sent_data.txt");
//            channel.introduceRandomError();
//            receiver.loadData();
//            receiver.validateCRCdata();

//            sender.loadData();
//            sender.createLRCdata("./ErrorDetection/sent_data.txt");
//            channel.introduceRandomError();
//            receiver.loadData();
//            receiver.validateLRCdata();

            sender.loadData();
            sender.creareVRCdata("./ErrorDetection/sent_data.txt");
            channel.introduceRandomError();
            receiver.loadData();
            receiver.validateVRCdata();
        } catch (Exception e) {
//            e.printStackTrace();
        }

    }
}
