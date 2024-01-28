import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigLoad {
    private int port;
    private String M;
    private int K;
    private int T;
    private int V; // Validade
    private int X; // Máximo de entradas na tabela

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
                    M = line;
                    System.out.println(M);
                }
                else if(i == 3){
                    K = Integer.parseInt(line.trim());
                    System.out.println(K);
                }
                else if(i == 4){
                    T = Integer.parseInt(line.trim());
                    System.out.println(T);
                }
                else if(i == 5){
                    V = Integer.parseInt(line.trim());
                    System.out.println(V);
                }
                else if(i == 6){
                    X = Integer.parseInt(line.trim());
                    System.out.println(X);
                }
            }
            scanner.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        mib.getOids().put( "1.3",K);
        mib.getOids().put( "1.4",T);
        mib.getOids().put( "1.5",X);
        mib.getOids().put( "1.6",V);
        mib.getOids().put( "2.1",M);

    return port;
    }
    
}
