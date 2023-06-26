package ErrorDetection;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Sender {
    private StringBuilder source_data; // THE RAW DATA
    private String file_name; // FILE FROM RAW DATA WILL BE TAKEN
    int dataPackSize; // SIZE OF DATA PACKET


    public Sender(String filename, int dataPackSize){
        this.file_name = filename;
        this.dataPackSize = dataPackSize;
        this.source_data = new StringBuilder("");
    }

    public void print(){
        System.out.println(this.source_data);
    }

    public void loadData() throws IOException {
        FileReader fr = new FileReader(new File(this.file_name));
        int ch;
        while ((ch = fr.read())!=-1){ // LOADING DATA FROM THE FILE
            this.source_data.append((char) ch);
        }

        fr.close();
    }

    public void createCheckSumData(String outputfilename) throws IOException {
        ArrayList<StringBuilder> slist = new ArrayList<StringBuilder>();
        // THIS LIST STORES EACH PACKET

        int len = this.source_data.length();
        int no_of_packets = len/this.dataPackSize;

        for(int i=0; i<no_of_packets; i++){
            StringBuilder str = new StringBuilder("");
            for(int j=0; j<this.dataPackSize; j++){
                str.append(this.source_data.charAt(i*this.dataPackSize+j));
            }
            slist.add(str);
        }

        // CHECKSUM CALCULATE STARTS.....
        StringBuilder ans = BinaryArithmetic.addValues(slist);
        StringBuilder modified_data = new StringBuilder(this.source_data);
        BinaryArithmetic.oneComplement(ans); // CHECKSUM CALCULATION END
        modified_data.append(ans);

        // WRITE DATA WITH CHECKSUM INTO FILE THAT SENT TO CHANNEL
        FileWriter fw = new FileWriter(new File(outputfilename));
        fw.write(String.valueOf(modified_data));
        fw.close();
    }

    public void createCRCData(String outputfilename) throws IOException {
        //WE WILL TAKE ONE FIXED POLYNOMINAL HERE
        StringBuilder polynomial_divisor = new StringBuilder("1011");
        //  CHANGING THE DIVISOR HERE, CHANGE IN THE RECEIVER CLASS AS WELL
      StringBuilder base = new StringBuilder(this.source_data);
      base.append("000"); // ADDING EXTRA BITS
      StringBuilder rem = BinaryArithmetic.mod2DivRemainder(base,polynomial_divisor);
      String to_be_sent = String.valueOf(this.source_data) + String.valueOf(rem);
      System.out.println("SENT DATA WTIH CRC ADDED: "+to_be_sent);
      FileWriter fw = new FileWriter(new File(outputfilename));
      fw.write(to_be_sent);
      fw.close();
    }

    public void createLRCdata(String outputfilename) throws IOException {

        int no_of_groups = this.source_data.length()/this.dataPackSize;

        StringBuilder lastRow = new StringBuilder(""); // STORES THE LONG. PARITY BITS

        // PARITY PACKET CALC. CONSIDERING EVEN PARITY
        for(int i=0; i<this.dataPackSize; i++){
            int cnt1 = 0;
            for(int j=0; j<no_of_groups; j++){
                if(this.source_data.charAt(j*this.dataPackSize+i)=='1') cnt1++;
            }
            if(cnt1%2==0) lastRow.append('0');
            else lastRow.append('1');
        }

        String to_be_sent = String.valueOf(this.source_data) + String.valueOf(lastRow);
        System.out.println("SENT DATA WTIH LRC ADDED: "+to_be_sent);
        FileWriter fw = new FileWriter(new File(outputfilename));
        fw.write(to_be_sent);
        fw.close();
    }

    public void creareVRCdata(String outputfilename) throws IOException{
        StringBuilder str = new StringBuilder(this.source_data);
        int cnt1 = 0;
        for(int j=0; j<str.length(); j++){
            if(this.source_data.charAt(j)=='1') cnt1++;
        }
        if(cnt1%2==0) str.append('0');
        else str.append('1');

        System.out.println("SENT DATA WTIH LRC ADDED: "+str.toString());
        FileWriter fw = new FileWriter(new File(outputfilename));
        fw.write(str.toString());
        fw.close();
    }
}
