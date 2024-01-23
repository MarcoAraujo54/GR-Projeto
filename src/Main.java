import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
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
        String arr = "";
        int T=0 ;
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server is running on port " + port);
            try {
                File file = new File("config.txt");
                Scanner scanner = new Scanner(file);
               
                for (int i = 0; i < 4 && scanner.hasNextLine(); i++) {
                    String line = scanner.nextLine();
                    if (i == 2) {
                        arr = line;
                    }
                    else if(i == 3){
                        T = Integer.parseInt(line.trim());
                        System.out.println(T);
                    }
                    else if(i == 4){

                    }
                }
               
                scanner.close();
                //updateMatrix(T,arr);
              
 
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
            
            //ToDo -> verificar file da mib
            //criar a MIB
            ConfigSnmpKeysMib config = new ConfigSnmpKeysMib();
            SystemSnmpKeysMib sys = new SystemSnmpKeysMib();
            DataSnmpKeysMib data = new DataSnmpKeysMib();
            SnmpKeysMib mib = new SnmpKeysMib(sys, config, data);
            //testes
            mib.getOids().put( "1.4",T);
            mib.getOids().put( "2.1",arr);
            int finalT = ((Integer) mib.getOidsPosition("1.4")).intValue();
            String arrString = (mib.getOidsPosition("2.1")).toString();
            System.out.println(arrString);
            byte[] Array = arrString.getBytes("UTF-8");
            System.out.println(Array);
            //fim de testes 
            new Thread(() -> updateMatrix(mib)).start();

            
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
                        auxValue = auxValue.replace(" ","");
                        int Value = Integer.parseInt(auxValue); 
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
                        Value = Value.replace(" ","");
                        mib.getOids().put(Iid,Value);
                        System.out.println(mib.getOidsPosition(Iid));
                        String anormal = mib.getOidsPosition("2.1").toString();
                        System.out.println("what"+anormal);
                        byte[] Arr = anormal.getBytes("UTF-8");
                        System.out.println("whatdafuck"+Arr);
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
    private static void updateMatrix(SnmpKeysMib mib) {
        try {
        while (true) {
            String stringValue = mib.getOidsPosition("1.4").toString();
            int finalT = Integer.parseInt(stringValue);
            String arr = (mib.getOidsPosition("2.1")).toString();
            byte[] Array = arr.getBytes("UTF-8");
            MSKeys m1 = MSKeys.getInstance(Array);
            m1.update(Array);
            try {
                Thread.sleep(finalT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
