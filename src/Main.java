import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {
    public static void main(String[] args) {
        int serverPort = 12345;
      //  udp_server server = new udp_server();

        // Start the server
        runServer(serverPort);
    }

    private static void runServer(int port) {

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server is running on port " + port);

            while (true) {
            	
                // Creats the thread for a new message received
                // Thread ComunicationThread = new Thread();
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] pduParts = receivedMessage.split("\0");
                if (pduParts.length >= 4) { // Check if there are at least 4 parts
                    try {
                        int securityModel = Integer.parseInt(pduParts[0]);
                        int numSecurityParams = Integer.parseInt(pduParts[1]);
                        int requestId = Integer.parseInt(pduParts[2]);
                        int primitiveType = Integer.parseInt(pduParts[3]);
                        System.out.println(requestId);
                        System.out.println(primitiveType);  

                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing PDU fields: " + e.getMessage());
                    }
                } else {
                    System.out.println("Incomplete PDU received.");
                }
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
