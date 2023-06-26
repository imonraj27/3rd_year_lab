
import java.util.ArrayList;
import java.util.Scanner;

class Station {
   public int data_bit;
   int id;

   Station(){}
   public Station(int id){
       data_bit = (int) Math.floor(Math.random()*3-1);
      // code = codee;
       this.id = id;
       if(data_bit==0) {
           System.out.println("Station " + id + " generates no data.");
           return;
       }
       System.out.println("Station "+id+" generates data bit "+((data_bit==-1)?0:1));
   }
}

public class CDMA {
    static  int[][] walshTable;
    static void generateWalshCodes(int N){
      int  n = (int) Math.pow(2,Math.floor(Math.log(N)/Math.log(2))+1);
        System.out.println(n);
        walshTable = new int[n][n];
        int prim_size = 2;
        walshTable[0][0] = 1;
        walshTable[0][1] = 1;
        walshTable[1][0] = 1;
        walshTable[1][1] = -1;

        while(prim_size<n){
            for(int i=0; i<2; i++){
                for(int j=0; j<2; j++){
                    for(int l=0; l<prim_size; l++){
                        for(int k=0; k<prim_size; k++){
                            int ti = i*prim_size+l;
                            int tj = j*prim_size+k;
                            walshTable[ti][tj] = walshTable[i][j]*walshTable[l][k];
                        }
                    }
                }
            }
            prim_size *= 2;
        }

        for(int l=0; l<n; l++){
            for(int k=0; k<n; k++){
                if(l>=N) walshTable[l][k]=0;
            }
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of stations: ");
        int no_of_stations = sc.nextInt();

        generateWalshCodes(no_of_stations);

        int tomax = (int) Math.pow(2,Math.floor(Math.log(no_of_stations)/Math.log(2))+1);

        System.out.println("----------WALSH TABLE---------");
        for(int i=0; i<no_of_stations; i++){
            for(int j=0; j<tomax; j++){
                System.out.print(walshTable[i][j]+"\t");
            }
            System.out.println();
        }
        System.out.println("---------------------------");
        ArrayList<Station> list = new ArrayList<Station>();


        for(int i=0; i<no_of_stations; i++){
            list.add(new Station(i));
        }

        int[] channel = new int[tomax];

        for(int i=0; i<no_of_stations; i++){
            for(int j=0; j<tomax; j++){
                channel[j] += (list.get(i).data_bit)*walshTable[i][j];
            }
        }

        System.out.println("---------------------------");
        System.out.println("Channel Data: ");
        for(int i=0; i<tomax; i++){
            System.out.print(channel[i]+"\t");
        }

        System.out.println("\n---------------------------");
        System.out.println("\nWhich station you want to see: ");
        int id = sc.nextInt();

        int sum = 0;
        for(int i=0; i<tomax; i++){
            sum += channel[i]*walshTable[id][i];
        }
        int ans = sum/tomax;

        if(ans==0){
            System.out.println("No data bit from station "+id);
        }else
        System.out.println("Data bit from station "+id+" is : "+((ans==-1)?0:1));
    }
}
