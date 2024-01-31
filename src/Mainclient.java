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
        int v = 0;
        long s = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(s);
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
                v = Integer.parseInt(line.trim());
                System.out.println(v);
            }               
        }
        scanner.close();
        final String serverAddress = ipServer;
        final int port = serverPort;
        UdpClient client = new UdpClient();
        Thread clientThread = new Thread(() -> client.runClient(serverAddress, port, args));
        clientThread.start();
        while(true){
            if(timestamp.getElapsedSeconds()>(v/1000)){
                clientThread.interrupt();
                System.exit(1);
            }    
        }       
    }         
}