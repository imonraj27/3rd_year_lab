package ErrorDetection;

import java.io.*;

public class Channel { // CLASS TO SIMULATE THE NOISY ENVIRONMENT
    private String passing_data;
    private String source_file;
    private String destination_file;

    private int randomInteger(int start, int end) { // RETURNS A RANDOM INTEGER
                                                    // BETWEEN START AND END
        return (int) Math.floor(Math.random() * (end - start) + (double) start);
    }

    public Channel(String source_file, String destination_file) {
        this.passing_data = "";
        this.source_file = source_file;
        this.destination_file = destination_file;
    }

    public void introduceRandomError() throws IOException {
        /* INTRODUCES RANDOM NO OF ERRORS IN RANDOM INDEXES OF THE BINARY SEQUENCE */
        FileReader fr = new FileReader(new File(this.source_file));
        int a;
        while ((a = fr.read()) != -1) {
            this.passing_data += (char) a;
        }
        fr.close();
        int len = this.passing_data.length();
        StringBuilder str = new StringBuilder(this.passing_data);

        int no_of_errors = this.randomInteger(0, 2);
        // RANDOMLY DECIDES HOW MANY BITS TO BE ERROR PRONE

        for (int i = 0; i < no_of_errors; i++) {
            int random_index = this.randomInteger(0, len);
            // RANDOM INDEX FOR ERROR
            if (str.charAt(random_index) == '0')
                str.setCharAt(random_index, '1');
            else
                str.setCharAt(random_index, '0');
        }

        FileWriter fw = new FileWriter(this.destination_file);
        fw.write(str.toString());
        fw.close();
    }
}
