import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
* Class to handle the client side of the udp connection
*
* @author Gustavo Oliveira
* @author José Peleja
* @author Marco Araújo
*
*/
public class UdpClient {
    public void runClient(String serverAddress, int serverPort,String args[]){
        int prim = 0;
        String idMan = "";
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);         
            if (args.length > 0) {
                idMan = args[0];
                String param = args[1];
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
            Map<String,String> Par = new HashMap<>();
            for (int i = 2; i < args.length; i += 2) {
                if (i + 1 < args.length) {
                    Par.put(args[i],args[i+1]);
                }
            }
            int Pid = updateFile();
            System.out.println("Pedido numero:"+Pid);
            int numPairs = Par.size();
            Map <String,String> Error = new HashMap<>();
            Pdu pdu = new Pdu(0,0,idMan,Pid,prim, numPairs ,Par , 0,Error);
            byte[] sendData = pdu.toMyString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverInetAddress, serverPort);
            
            socket.send(sendPacket);
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received from server: " + receivedMessage);
            System.exit(1);
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
            return newValue;
        }
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(newValue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return newValue;
    }
}
