import java.util.ArrayList;

public class FlowControl {
    public static int no_of_packets = 0;
  public static ArrayList<Boolean> acks = new ArrayList<Boolean>();
    public static StringBuilder temp_data = new StringBuilder("");
    public static boolean isFinish = false;

   synchronized static public void updateAcks(int i, boolean val){
        acks.set(i,val);
    }
    public static void setAcks(){
        for(int i=0; i<no_of_packets; i++){
            acks.add(false);
        }
    }
}
