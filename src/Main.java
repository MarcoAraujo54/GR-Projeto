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
            System.out.println(socket);
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
            // Chama o método generateKeyC para obter a nova chave C
            //testes
            mib.getOids().put( "1.4",T);
            mib.getOids().put( "2.1",M);
            //Criaçao da matriz
            new Thread(() -> updateMatrix(mib)).start();
            
            while (true) {                 
                /*long elapsedTime = executionTime();
                long S = elapsedTime;
                System.out.println("Passaram:"+S);*/
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                new Thread(() -> {
                    try {
                        Comunication.run(socket,mib,receivePacket);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }).start();
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