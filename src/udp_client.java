import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
                System.out.println(
                    "| 1 -         Get             |\n" +
                    "| 2 -         Set             |\n" +
                    "| 3 -         Sair            |\n" +
                    "|      Escolha a opçao :      |" 
                );
                System.out.println("+-----------------------------+\n\n");
                option = scanner.nextShort();

                switch(option){
                    case 1:
                        updateFile();
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
    public static void updateFile() {
        String filePath = "Pid.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the integer from the file
            String line = reader.readLine();
            int originalValue = Integer.parseInt(line);
            int newValue = originalValue + 1;
            // Write the updated value back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(Integer.toString(newValue));
            }
            System.out.println("Updated value successfully written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}