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
        byte[] arr = new byte[0];
        int T=0 ;
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server is running on port " + port);
            try {
                File file = new File("config.txt");
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
            
            //ToDo -> verificar file da mib
            //criar a MIB
            ConfigSnmpKeysMib config = new ConfigSnmpKeysMib();
            SystemSnmpKeysMib sys = new SystemSnmpKeysMib();
            DataSnmpKeysMib data = new DataSnmpKeysMib();
            SnmpKeysMib mib = new SnmpKeysMib(sys, config, data);
            //testes
            mib.getOids().put( "1.4",T);
            final int finalT = ((Integer) mib.getOidsPosition("1.4")).intValue();
            final byte[] finalArr = arr;
            String caminho = "1.2";
            
            boolean condition=true;
            int x=0;
            int k=1;
            String firstlevel;
            for(int i=1;i<=3;i++){
                condition=true;
                firstlevel=String.valueOf(i);
               
                while (condition) {
                    x++;
                  
                    String secondlevel= firstlevel + "." + String.valueOf(x);
                    System.out.println(secondlevel);
                    //arrayIntParaString(path); 
    
                    if(mib.contains(secondlevel)){
                        if(mib.contains(secondlevel+"."+String.valueOf(k))){ 
                            String thirdlevel=secondlevel+"."+String.valueOf(k);
                            System.out.println("novopath: "+thirdlevel);
                            System.out.println("mib: " +mib.getOidsPosition(thirdlevel));
                            k++;       
                        }
                        else{
                            k=1;
                        } 
                            System.out.println("novopath: "+secondlevel);
                            System.out.println("mib: " +mib.getOidsPosition(secondlevel));
                    }
                    else{
                        x=0;
                        condition=false;
                    }
                  
                }
            }
            
            System.out.println( mib.getOidsPosition("1.4"));
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
                        mib.getOids().put(Iid,Value);
                        System.out.println(mib.getOidsPosition(Iid));
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
       /*  MSKeys m1 = MSKeys.getInstance(arr);
      //  int x = ((Integer) mib.getOidsPosition("1.4")).intValue();
        while (true) {
            m1.update(arr);
            try {
               // Thread.sleep(x);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
    private static String arrayIntParaString(int[] array) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            // Adicionando o valor atual ao StringBuilder
            stringBuilder.append(array[i]);

            // Adicionando um ponto, exceto para o último elemento
            if (i < array.length - 1) {
                stringBuilder.append(".");
            }
        }
        return stringBuilder.toString();
    }
}
