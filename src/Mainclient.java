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
        //Check the execution time for the manager
        long s = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(s);
        //Opens the file to get the port, serverIp and the max time before timeout
        File file = new File("config.txt");
        Scanner scanner = new Scanner(file);            
        for (int i = 0; i < 7 && scanner.hasNextLine(); i++) {
            String line = scanner.nextLine();
            if (i == 0) {
                serverPort = Integer.parseInt(line.trim());
            }
            else if (i == 1) {
                ipServer = line;
            }
            else if(i == 5){
                v = Integer.parseInt(line.trim());
            }               
        }
        scanner.close();

        final String serverAddress = ipServer;
        final int port = serverPort;
        //New thread is created to handle the processing of the PDU and communication
        UdpClient client = new UdpClient();
        Thread clientThread = new Thread(() -> client.runClient(serverAddress, port, args));
        clientThread.start();
        //Checks if the timeout max time was already reached
        while(true){
            if(timestamp.getElapsedSeconds()>(v/1000)){
                clientThread.interrupt();
                System.exit(1);
            }    
        }       
    }         
}