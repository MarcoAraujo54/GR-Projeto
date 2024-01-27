import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
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
    private static void runServer(int port) throws IOException{
        String M = "";
        int T = 0;
        int V = 0;
        int X = 0;
        DatagramSocket socket = new DatagramSocket(port);
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
        //testes
        mib.getOids().put( "1.4",T);
        mib.getOids().put( "2.1",M);
        //Criaçao da matriz
        new Thread(() -> MSKeys.updateMatrix(mib)).start();
        ComnServer server = new ComnServer(socket, mib);       
        server.startServer();                    
    }
}