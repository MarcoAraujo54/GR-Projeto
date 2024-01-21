import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        File file = new File("src/config.txt");
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
        byte[] arr = new byte[0];
        int T=0 ;
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server is running on port " + port);
            try {
                File file = new File("src/config.txt");
                Scanner scanner = new Scanner(file);
               
                for (int i = 0; i < 4 && scanner.hasNextLine(); i++) {
                    String line = scanner.nextLine();
                    if (i == 2) {
                        arr = new byte[line.length()];
                        for (int j = 0; j < line.length(); j++) {
                            arr[j] = (byte) (line.charAt(j) - '0');
                        }
                        System.out.println(Arrays.toString(arr));
                    }
                    else if(i == 3){
                        T = Integer.parseInt(line.trim());
                        System.out.println(T);
                    }

                }
               
                scanner.close();
                //updateMatrix(T,arr);
              
 
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
            final int finalT = T;
            final byte[] finalArr = arr;
            //ToDo -> verificar file da mib
            //criar a MIB
            ConfigSnmpKeysMib config = new ConfigSnmpKeysMib();
            SystemSnmpKeysMib sys = new SystemSnmpKeysMib();
            DataSnmpKeysMib data = new DataSnmpKeysMib();
            SnmpKeysMib mib = new SnmpKeysMib(sys, config, data);
            //testes
            for(int i=1;i<7;i++){
                System.out.println( mib.getOidsPosition("1."+i));
            }
            mib.getOids().put( "1.4",finalT);
            System.out.println(mib.getOidsPosition("1.4"));
            //fim de testes
            
            new Thread(() -> updateMatrix(finalT,finalArr)).start();
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
    private static void updateMatrix(int T, byte[] arr) {
        MSKeys m1 = MSKeys.getInstance(arr);
            while (true) {
                m1.update(arr);
                try {
                    Thread.sleep(T);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
}
