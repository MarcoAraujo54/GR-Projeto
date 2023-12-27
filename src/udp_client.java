import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class udp_client {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    public udp_client(String ipAddress, int port) throws Exception {
        socket = new DatagramSocket();
        address = InetAddress.getByName(ipAddress);
        this.port = port;
    }

    public void sendPacket(Packet packet) throws Exception {
        byte[] data = packet.getPacket();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
        socket.send(datagramPacket);
    }

    public static void main(String[] args) {
        try {
            udp_client client = new udp_client("localhost", 9876);

            Packet packet = new Packet();
            packet.setHeader("Header Data".getBytes());
            packet.setPayload(0, 0, "", updateFile(), 1, 2, "Instance1,Value1;Instance2,Value2", 0, "");
            packet.setTrailer("Trailer Data".getBytes());

            client.sendPacket(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int updateFile() {
        String filePath = "src/Pid.txt";
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