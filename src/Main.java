import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File file = new File("config.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                int serverPort = Integer.parseInt(scanner.nextLine());
                // Start the server
                runServer(serverPort);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.close();          
    }
    private static void runServer(int port) {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server is running on port " + port);
            while (true) {
                // Creats the thread for a new message received
                Thread ComunicationThread = new Thread();
                ComunicationThread.start();
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] pduParts = receivedMessage.split("-");
                int securityModel = Integer.parseInt(pduParts[0]);
                int numSecurityParams = Integer.parseInt(pduParts[1]);
                String listSecurity = pduParts[2];
                int requestId = Integer.parseInt(pduParts[3]);
                int primitiveType = Integer.parseInt(pduParts[4]);
                int numberPairs =  Integer.parseInt(pduParts[5]);
                String Pairs = pduParts[6];
                int numberErrors = Integer.parseInt(pduParts[7]);
                String errors = pduParts[8];
                if(primitiveType == 1){
                    String[] listPairs = Pairs.split(",");
                    for(int i =0; i<listPairs.length;i++){
                        String[] aux = listPairs[i].split("//");
                        String Iid = aux[0].replace("[", "");
                        Iid = Iid.replace(" ","");
                        String auxValue = aux[1].replace("]", "");
                        int Value = Integer.parseInt(auxValue);
                        System.out.println(Iid);
                        System.out.println(Value);
                        //processar o get
                    }
                }
                else if(primitiveType == 2){
                    String[] listPairs = Pairs.split(",");
                    for(int i =0; i<listPairs.length;i++){
                        String[] aux = listPairs[i].split("//");
                        String Iid = aux[0].replace("[", "");
                        Iid = Iid.replace(" ","");
                        String Value = aux[1].replace("]", "");
                        System.out.println(Iid);
                        System.out.println(Value);
                        //processar o set
                    }
                }                  
                System.out.println("Received from client: " + receivedMessage);
                List<String> responsePair = new ArrayList<>();
                int numPairs = responsePair.size();
                List<String> Error = new ArrayList<>();
                int responseErrors = Error.size();
                Pdu pdu = new Pdu(0,0,requestId,0, numPairs , responsePair , responseErrors,Error);
                byte[] sendData = pdu.toMyString().getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
