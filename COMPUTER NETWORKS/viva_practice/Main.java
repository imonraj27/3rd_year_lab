
public class Main {
    public static void main(String[] args) {
        System.out.println("Hellow Imon!!");

        try {

            if (args[0].equals("R")) {
                Receiver receiver = new Receiver();
                receiver.run();
            } else {
                Sender sender = new Sender();
                sender.run();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}