import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
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
            List<String> Par = new ArrayList<>();
            for (int i = 1; i < args.length; i += 2) {
                if (i + 1 < args.length) {
                    String pair = args[i] + "//" + args[i + 1];
                    Par.add(pair);
                }
            }
            int numPairs = Par.size();
            List<String> Error = new ArrayList<>();
            Pdu pdu = new Pdu(0,0,updateFile(),prim, numPairs ,Par , 0,Error);
            byte[] sendData = pdu.toMyString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverInetAddress, serverPort);

            socket.send(sendPacket);
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received from server: " + receivedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int updateFile() {
        String filePath = "Pid.txt";
        int newValue = 0;
        File file = new File(filePath);
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextInt()) {
                int originalValue = scanner.nextInt();
                newValue = originalValue + 1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return newValue; // Retorna o valor padrão se ocorrer um erro
        }
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(newValue);
            System.out.println("Updated value successfully written to the file.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return newValue;
    }
}