import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
* Class to load the config file to the server
*
* @author Gustavo Oliveira
* @author José Peleja
* @author Marco Araújo
*
*/

public class ConfigLoad {
    private int port; // UDPport
    private String m; // Masterkey
    private int k; // Key size
    private int t; // Interval Update
    private int v; // Validity
    private int x; // Maximum of table entry

    public int loadConfig(SnmpKeysMib mib){
        try {
            File file = new File("config.txt");
            Scanner scanner = new Scanner(file);            
            for (int i = 0; i < 7 && scanner.hasNextLine(); i++) {
                String line = scanner.nextLine();
                if (i == 0) {
                    port = Integer.parseInt(line.trim());
                    System.out.println(port);
                }
                else if (i == 2) {
                    m = line;
                    System.out.println(m);
                }
                else if(i == 3){
                    k = Integer.parseInt(line.trim());
                    System.out.println(k);
                }
                else if(i == 4){
                    t = Integer.parseInt(line.trim());
                    System.out.println(t);
                }
                else if(i == 5){
                    v = Integer.parseInt(line.trim());
                    System.out.println(v);
                }
                else if(i == 6){
                    x = Integer.parseInt(line.trim());
                    System.out.println(x);
                }
            }
            scanner.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        //Puts the configuration values inside the MIB
        mib.getOids().put( "1.3",k);
        mib.getOids().put( "1.4",t);
        mib.getOids().put( "1.5",x);
        mib.getOids().put( "1.6",v);
        mib.getOids().put( "2.1",m);
    //Returns port to the connection
    return port;
    }
    
}
