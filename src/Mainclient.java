public class Mainclient {
    public static void main(String[] args) {
        int serverPort = 12345;
        udp_client client = new udp_client();

        Thread clientThread = new Thread(() -> client.runClient("localhost", serverPort, args));

        clientThread.start();
    }

}