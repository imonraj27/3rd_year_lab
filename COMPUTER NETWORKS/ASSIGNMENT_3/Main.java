
/**----------------------------------------
 * COMPUTER NETWORKS ASSIGNMENT 3
 * IMPLEMENTATION OF CSMA MAC LAYER TECHNIQUE.
 * ALL DIFFERENT VARIATIONS OF CSMA - 1-PERSISTENT,
 * NON-PERSISTENT AND P-PERSISTENT ARE DONE.
 * 
 * SUBMITTED BY - 
 * IMON RAJ
 * BCSE III
 * JU CSE 2020-2024
 * --------------------------------------**/

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

class Channel {
    static boolean isBusy = false; // VARIABLE TO CHECK IF CHANNEL BUSY -> SHARED VARIABLE -> SYNCHRONIZED
    static Semaphore mutex = new Semaphore(1); // SEMAPHORE TO KEEP isBusy SYNCHRONIZED
    static double p; // PROBABILITY FOR P_PERSISTENT
    static int noofstations;
}

class Station extends Thread {
    int id; // EACH STATION WILL HAVE ID
    int method; // FOR DIFFERENT METHODS OF CSMA (EG->NON-PERSISTENT)

    Station(int id, int method) {
        this.id = id;
        System.out.println("Channel " + id + " is initialized...");
        this.method = method;
    }

    void sendFrame(int id) throws Exception { // SENDS A FRAME TO MEDIUM
        System.out.println("Station " + id + " sends Frame..");
        sleep(1000); // FRAME TRANSMISSION TIME
    }

    void releaseMedium(int id) throws Exception { // RELEASES THE MEDIUM
        Channel.mutex.acquire();
        Channel.isBusy = false;
        System.out.println("Station " + id + " releases Medium..\n==");
        Channel.mutex.release();
    }

    void one_persistent() throws Exception {
        while (true) {
            // ---WAIT RANDOMLY BEFORE INTENTION TO SEND---
            sleep(Math.round(Math.random() * 3000) + 2000);

            System.out.println("Station " + id + " starts to sense medium..");
            while (true) { // CONTINUOUS SENSING
                Channel.mutex.acquire();
                if (!Channel.isBusy) {
                    System.out.println("Station " + id + " finds medium IDLE.. Medium Acquired..");
                    Channel.isBusy = true;
                    Channel.mutex.release();
                    break;
                }
                Channel.mutex.release();
            }
            sendFrame(id);
            releaseMedium(id);
        }
    }

    void non_persistent() throws Exception {
        while (true) {
            sleep(Math.round(Math.random() * 5000) + 2000);

            while (true) {
                Channel.mutex.acquire();
                System.out.println("Station " + id + " senses medium..");
                if (!Channel.isBusy) {
                    System.out.println("Station " + id + " finds medium IDLE.. Medium Acquired..");
                    Channel.isBusy = true;
                    Channel.mutex.release();
                    break;
                }
                System.out.println("Station " + id + " finds medium busy.. waits randomly..");
                Channel.mutex.release();
                sleep(Math.round(Math.random() * 3000) + 2000); // WAIT RANDOMLY AFTER ONE SENSE
            }
            sendFrame(id);
            releaseMedium(id);
        }
    }

    void p_persistent() throws Exception {
        DecimalFormat df = new DecimalFormat("0.00");
        while (true) {
            sleep(Math.round(Math.random() * 3000) + 2000);
            System.out.println("Station " + id + " starts to sense medium..");
            double k;
            while (true) {
                Channel.mutex.acquire();
                if (!Channel.isBusy) {
                    System.out.println(
                            "Station " + id + " finds medium IDLE. generates -> (" + df.format(k = Math.random())
                                    + ((k <= Channel.p) ? ") -> WILL SEND ACQUIRE MEDIUM.." : ") -> WILL NOT SEND.."));
                    if (k <= Channel.p)
                        Channel.isBusy = true;
                    Channel.mutex.release();
                    break;
                }
                Channel.mutex.release();
            }
            if (k <= Channel.p) { // SENDS FRAME WITH A PR0BABILITY
                sendFrame(id);
                releaseMedium(id);
            } else { // OTHERWISE WAITS BACKOFF
                System.out.println("Station " + id + " waits backoff..");
                sleep(1000);
            }

        }
    }

    @Override
    public void run() {
        try {
            if (method == 1) { // 1-persistent
                one_persistent();
            } else if (method == 2) { // non-persistent
                non_persistent();
            } else if (method == 3) { // non-persistent
                p_persistent();
            }
        } catch (Exception e) {
        }
    }
}

public class Main {
    public static void main(String[] args) {
        int n, method;
        System.out.println("How many stations: ");
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        Channel.noofstations = n;
        System.out.println("Enter Method:\n1. 1-persistent\n2. non-persistent\n3. p-persistent");
        method = sc.nextInt();

        if (method == 3) {
            System.out.println("Enter value of P: ");
            Channel.p = sc.nextDouble();
        }
        sc.close();

        ArrayList<Station> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            try {
                list.add(new Station(i, method));
            } catch (Exception e) {
            }
        }

        for (int i = 0; i < n; i++) {
            // list.get(i).recTh.start();
            list.get(i).start();
        }
    }
}