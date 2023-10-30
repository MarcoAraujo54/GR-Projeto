import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {
    public static void main(String[] args) {
        int serverPort = 12345;
        udp_server server = new udp_server();

        // Start the server
        runServer(serverPort);

    }
    private static void runServer(int port) {

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server is running on port " + port);

            while (true) {
            	//teste 123
                // Creats the thread for a new message received
                Thread ComunicationThread = new Thread();
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from client: " + receivedMessage);
                String responseMessage = "Server received: " + receivedMessage;
                byte[] sendData = responseMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}