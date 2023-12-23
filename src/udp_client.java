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
            short option = 0;
            Scanner scanner = new Scanner(System.in);
            while (option == 0) {

                System.out.println("\n");
                System.out.println("+-----------------------------+");
                System.out.println("|            MENU             |");
                System.out.println("+-----------------------------+");
                System.out.println( "| 1 -         Get             |\n" +
                                    "| 2 -         Set             |\n" +
                                    "| 3 -         Sair            |\n" +
                                    "|      Escolha a opçao :      |" );
                System.out.println("+-----------------------------+\n\n");
                option = scanner.nextShort();
                
                //testes

                //Map <Integer,Integer> L = new HashMap<Integer,Integer>();
                List<Array> L = new ArrayList<>();
                //int NL []= new int[2];
                int[] NL={0,0};
                int N= NL[1];
                
            
                
                switch(option){
                    case 1:
                        int p = updateFile();
                        snmp_get(p,NL,L);

                        break;
                    case 2:
                        updateFile();
                        break;
                    case 3:
                        break;
                    default:
                        option=0;
                        break;
                }
            }
            scanner.close();
            String message = "Hello, UDP Server!";
            
            byte[] sendData = message.getBytes();
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
    public List<Array> snmp_get(int p, int[] NL, List<Array> L){
        List<Array> aux = new ArrayList<Array>();
        for(int i=NL[0];i<NL[1];i++){
        aux.add(L.get(NL[i]));
        }
        return aux;
    }
}