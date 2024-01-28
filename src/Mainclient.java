import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class Mainclient {
    public static void main(String[] args) {
        int serverPort = 0;
        String ipServer = "";
        int V = 0; 
        try {
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
                UdpClient client = new UdpClient();
                Thread clientThread = new Thread(() -> client.runClient(ipServer, serverPort, args));
                long startTime = System.currentTimeMillis();
                try{
                    
                    clientThread.start();
                } catch (Exception e){

                }finally{
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;

                    if(elapsedTime > V){
                            throw new Exception("Timeout");
                    }
                }
            }
            scanner.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }          
    }

}