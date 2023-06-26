package ErrorDetection;

import javax.naming.BinaryRefAddr;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Receiver {
    private String received_data;
    private String file_name;
    private String modified_data;
    int dataPackSize;
    public Receiver(String filename, int dataPackSize){
        this.file_name = filename;
        this.dataPackSize = dataPackSize;
        this.received_data = "";
    }



    public void loadData() throws IOException {
        FileReader fr = new FileReader(new File(this.file_name));
        int ch;
        while ((ch = fr.read())!=-1){
            this.received_data += (char)ch;
        }

        fr.close();
    }

    public void validateCheckSumData(){
        ArrayList<StringBuilder> slist = new ArrayList<StringBuilder>();

        int len = this.received_data.length();


        int no_of_packets = len/this.dataPackSize;

        for(int i=0; i<no_of_packets; i++){
            StringBuilder str = new StringBuilder("");
            for(int j=0; j<this.dataPackSize; j++){
                str.append(this.received_data.charAt(i*this.dataPackSize+j));
            }


            slist.add(str);
        }

        StringBuilder ans = BinaryArithmetic.addValues(slist);
        BinaryArithmetic.oneComplement(ans);
        boolean isError = false;
        for(int i=0; i<this.dataPackSize; i++){
            if(ans.charAt(i)!='0') {
                isError = true;
                break;
            }
        }
        if(!isError){
            System.out.println("NO ERROR DETECTED - checksum - " + ans);
        }else{
            System.out.println("ERROR DETECTED - checksum - "+ ans);
        }
    }

    public void validateCRCdata(){
        // WE WILL TAKE ONE FIXED POLYNOMINAL HERE
        StringBuilder polynomial_divisor = new StringBuilder("1011");
        //  CHANGING THE DIVISOR HERE, CHANGE IN THE SENDER CLASS AS WELL
        StringBuilder base = new StringBuilder(this.received_data);
        StringBuilder rem = BinaryArithmetic.mod2DivRemainder(base,polynomial_divisor);
        System.out.print("RECEIVED DATA WTIH CRC ADDED: ");
        System.out.println(this.received_data);
        System.out.println("CRC: "+rem.toString());
        if(rem.toString().equals("000")) { //change it as well while changing plynmial
            System.out.println("NO ERROR DETECTED USING CRC");
        }else
            System.out.println("ERROR DETECTED USING CRC..");
    }

    public void validateLRCdata() throws IOException {

        int no_of_groups = this.received_data.length()/this.dataPackSize;

        StringBuilder lastRow = new StringBuilder("");

        for(int i=0; i<this.dataPackSize; i++){
            int cnt1 = 0;
            for(int j=0; j<no_of_groups; j++){
                if(this.received_data.charAt(j*this.dataPackSize+i)=='1') cnt1++;
            }
            if(cnt1%2==0) lastRow.append('0');
            else lastRow.append('1');
        }

        StringBuilder validParity = new StringBuilder("");

        for(int i=0; i<this.dataPackSize; i++){
            validParity.append('0');
        }

        System.out.println("RECEIVED DATA: "+this.received_data);
        if(validParity.toString().equals(lastRow.toString())){
            System.out.println("NO ERROR DETECTED USING LRC, parity - "+lastRow.toString());
        }else{
            System.out.println("ERROR DETECTED USING LRC, parity - "+lastRow.toString());
        }
    }

    public void validateVRCdata(){
        int cnt1 = 0;
        for(int j=0; j<this.received_data.length(); j++){
            if(this.received_data.charAt(j)=='1') cnt1++;
        }

        System.out.println("RECEIVED DATA: "+this.received_data);
        if(cnt1%2==0) System.out.println("NO ERROR DETECTED, EVEN PARITY MAINTAINED.");
        else System.out.println("ERROR DETECTED, EVEN PARITY NOT MAINTAINED.");
    }
}
