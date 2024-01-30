import java.io.File;
import java.util.Scanner;
/**
* Client side main
*
* @author Gustavo Oliveira
* @author José Peleja
* @author Marco Araújo
*
*/
public class Mainclient {
    public static void main(String[] args) throws Exception {
        int serverPort = 0;
        String ipServer = "";
        int V = 0;
        long S = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(S);
        File file = new File("config.txt");
        Scanner scanner = new Scanner(file);            
        for (int i = 0; i < 7 && scanner.hasNextLine(); i++) {
            String line = scanner.nextLine();
            if (i == 0) {
                serverPort = Integer.parseInt(line.trim());
                System.out.println(serverPort);
            }
            else if (i == 1) {
                ipServer = line;
                System.out.println(ipServer);
            }
            else if(i == 5){
                V = Integer.parseInt(line.trim());
                System.out.println(V);
            }               
        }
        scanner.close();
        final String serverAddress = ipServer;
        final int Port = serverPort;
        UdpClient client = new UdpClient();
        Thread clientThread = new Thread(() -> client.runClient(serverAddress, Port, args));
        clientThread.start();
        while(true){
            if(timestamp.getElapsedSeconds()>(V/1000)){
                clientThread.interrupt();
                System.exit(1);
            }    
        }       
    }         
}
