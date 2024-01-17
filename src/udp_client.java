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

    public void runClient(String serverAddress, int serverPort,String args[]) {
        int prim = 0;
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);         
            if (args.length > 0) {
                String param = args[0];
                if (param.equals("response")) {
                    prim = 0;
                }else if (param.equals("get")) {
                    prim = 1;
                } else if (param.equals("set")) {
                    prim = 2;
                } else {
                    System.out.println("Unrecognized parameter");
                }
            } else {
                System.out.println("No parameter provided");
            } 
            Pdu pdu = new Pdu(0,0,updateFile(),prim);
            byte[] sendData = pdu.toMyString().getBytes();
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
            String line = reader.readLine();
            int originalValue = Integer.parseInt(line);
            newValue = originalValue + 1;
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