import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Mainclient {
    public static void main(String[] args) {
        File file = new File("config.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);    
            if (scanner.hasNextLine()) {
                int serverPort = Integer.parseInt(scanner.nextLine());
                if (scanner.hasNextLine()) {
                    String ipServer = scanner.nextLine();
                    UdpClient client = new UdpClient();
                    Thread clientThread = new Thread(() -> client.runClient(ipServer, serverPort, args));
                    clientThread.start();
                } 
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.close();          
    }

}