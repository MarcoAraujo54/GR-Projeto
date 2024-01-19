import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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
                    String pair = args[i] + "||" + args[i + 1];
                    Par.add(pair);
                }
            }
            int numPairs = (args.length - 1) / 2;
            System.out.println(numPairs);
            Pdu pdu = new Pdu(0,0,updateFile(),prim, numPairs ,Par);
            byte[] sendData = pdu.toMyString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverInetAddress, serverPort);

            socket.send(sendPacket);

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