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
                    Thread clientThread = new Thread(() -> client.runClient(ipServer, serverPort, args));~
                    try{
                        long startTime = System.currentTimeMillis();
                        clientThread.start();
                    } catch (Exception e){

                    }finally{
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;

                        if(elapsedTime >){
                             throw new TimeoutException("Timetout");
                        }
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.close();          
    }

}