import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static long startTimeStamp = System.currentTimeMillis();
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
        String M = "";
        int T = 0;
        int V = 0;
        int X = 0;
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server is running on port " + port);
            try {
                File file = new File("config.txt");
                Scanner scanner = new Scanner(file);
                
                for (int i = 0; i < 6 && scanner.hasNextLine(); i++) {
                    String line = scanner.nextLine();
                    if (i == 2) {
                        M = line;
                        System.out.println(M);
                    }
                    else if(i == 3){
                        T = Integer.parseInt(line.trim());
                        System.out.println(T);
                    }
                    else if(i == 4){
                        V = Integer.parseInt(line.trim());
                        System.out.println(V);
                    }
                    else if(i == 5){
                        X = Integer.parseInt(line.trim());
                        System.out.println(X);
                    }
                }
                scanner.close();

            } 
            catch (FileNotFoundException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }            
            //ToDo -> verificar file da mib
            //criar a MIB
            ConfigSnmpKeysMib config = new ConfigSnmpKeysMib();
            SystemSnmpKeysMib sys = new SystemSnmpKeysMib();
            DataSnmpKeysMib data = new DataSnmpKeysMib();
            SnmpKeysMib mib = new SnmpKeysMib(sys, config, data);
            
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
            // Chama o método generateKeyC para obter a nova chave C
            //testes
            mib.getOids().put( "1.4",T);
            mib.getOids().put( "2.1",M);
            //Criaçao da matriz
            new Thread(() -> updateMatrix(mib)).start();

            while (true) {                 
                // Creats the thread for a new message received
                Thread ComunicationThread = new Thread();
                ComunicationThread.start();
                long elapsedTime = executionTime();
                long S = elapsedTime;
                System.out.println("Passaram:"+S);

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] pduParts = receivedMessage.split("-");
                //int securityModel = Integer.parseInt(pduParts[0]);
                //int numSecurityParams = Integer.parseInt(pduParts[1]);
                //String listSecurity = pduParts[2];
                int requestId = Integer.parseInt(pduParts[3]);
                int primitiveType = Integer.parseInt(pduParts[4]);
                //int numberPairs =  Integer.parseInt(pduParts[5]);
                String Pairs = pduParts[6];
                //int numberErrors = Integer.parseInt(pduParts[7]);
                //String errors = pduParts[8];
                System.out.println("Received from client: " + receivedMessage);
                Map<String, String> responsePair = new HashMap<>();
                Map<String,String> Error = new HashMap<>();
                if(primitiveType == 1){
                    String[] listPairs = Pairs.split(",");
                    for(int i =0; i<listPairs.length;i++){
                        String[] aux = listPairs[i].split("=");
                        if(aux.length != 1){
                            String Iid = aux[0].replace("{", "").trim();
                            String auxValue = aux[1].replace("}", "").trim();
                            try {
                                int Value = Integer.parseInt(auxValue);
                                if(mib.contains(Iid)){
                                    System.out.println(Value);
                                }
                                else{
                                    System.out.println("Oid_Inexistente");
                                    String erro = "Oid_Inexistente";
                                    Error.put(Iid, erro);
                                }
                            } catch (Exception e) {
                                String erro = "Value has to be Integer";
                                Error.put("0", erro);
                            }
                        } 
                        else{
                            System.out.println("Recebido sem Argumentos");
                            String erro = "Usage set:Oid,Value";
                            Error.put("0", erro);
                        }
                    }
                }
                else if(primitiveType == 2){
                    String[] listPairs = Pairs.split(",");
                    for(int i =0; i<listPairs.length;i++){
                        String[] aux = listPairs[i].split("=");
                        if(aux.length != 1){
                            String Iid = aux[0].replace("{", "").trim();
                            String Value = aux[1].replace("}", "").trim();
                            if(mib.contains(Iid)){
                                mib.getOids().put(Iid,Value);
                                responsePair.put(Iid, mib.getOidsPosition(Iid).toString());
                            }
                            else{
                                System.out.println("Oid_Inexistente");
                                String erro = "Oid_Inexistente";
                                Error.put(Iid, erro);
                            }
                        }
                        else{
                            System.out.println("Recebido sem Argumentos");
                            String erro = "Usage set:Oid,Value";
                            Error.put("0", erro);
                        }                       
                    }
                }
                if(responsePair.size() == 0){
                    responsePair.put("0","0");
                }
                if(Error.size() == 0){
                    Error.put("0","0");
                }                  
                int numPairs = responsePair.size();;
                int responseErrors = Error.size();
                Pdu pdu = new Pdu(0,0,requestId,0, numPairs , responsePair , responseErrors, Error);
                byte[] sendData = pdu.toMyString().getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket); 
            }        
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void updateMatrix(SnmpKeysMib mib) {
        while (true) {
            String stringValue = mib.getOidsPosition("1.4").toString();
            int finalT = Integer.parseInt(stringValue);
            String arr = (mib.getOidsPosition("2.1")).toString();
            byte[] Array = arr.getBytes();
            MSKeys m1 = MSKeys.getInstance(Array);
            m1.update(Array);
            try {
                Thread.sleep(finalT);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static long executionTime() {
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedMillis = currentTimeMillis - startTimeStamp;
        return elapsedMillis;
    }
}