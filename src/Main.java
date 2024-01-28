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
        DatagramSocket socket = new DatagramSocket(port);
        System.out.println("UDP Server is running on port " + port);            
        //criar a MIB
        ConfigSnmpKeysMib config = new ConfigSnmpKeysMib();
        SystemSnmpKeysMib sys = new SystemSnmpKeysMib();
        DataSnmpKeysMib data = new DataSnmpKeysMib();
        SnmpKeysMib mib = new SnmpKeysMib(sys, config, data);
        //Criaçao da matriz
        ConfigLoad ConfigLoad = new ConfigLoad();
        ConfigLoad.loadConfig(mib);

        MSKeys MSK = MSKeys.getInstance();
        MSK.create(mib);
    
        new Thread(() -> MSK.updateMatrix(mib)).start();
        ComnServer server = new ComnServer(socket, mib);       
        server.startServerThread();                    
    }
}