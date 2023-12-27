import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class udp_client {

    public void runClient(String serverAddress, int serverPort) {

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
            Packet packet = new Packet();
            packet.setHeader("Header Data".getBytes());
            packet.setPayload(0, 0, "", updateFile(), 1, 2, "Instance1,Value1", 0, "");
            packet.setTrailer("Trailer Data".getBytes());

            byte[] sendData = packet.getPayload().toString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverInetAddress, serverPort);

            socket.send(sendPacket);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String responseMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Server response: " + responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int updateFile() {
        String filePath = "Pid.txt";
        int newValue=0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the integer from the file
            String line = reader.readLine();
            int originalValue = Integer.parseInt(line);
            newValue = originalValue + 1;
            // Write the updated value back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(Integer.toString(newValue));
            }
            System.out.println("Updated value successfully written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newValue;
    }
}