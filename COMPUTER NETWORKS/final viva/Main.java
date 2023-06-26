public class Main {
    public static void main(String[] args) {
        try {
            if (args[0].equals("c")) {
                Client client = new Client();
                client.run_process();
            } else {
                Server server = new Server();
                server.run_process();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
