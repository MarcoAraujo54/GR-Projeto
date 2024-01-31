import java.io.IOException;
import java.net.DatagramSocket;
/**
* Server side main
*
* @author Gustavo Oliveira
* @author José Peleja
* @author Marco Araújo
*
*/
public class Main {
    public static void main(String[] args) throws IOException {
        runServer();

    }
    private static void runServer() throws IOException{ 

        long S = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(S);
        timestamp.getElapsedSeconds();

        //Create MIB
        ConfigSnmpKeysMib config = new ConfigSnmpKeysMib();
        SystemSnmpKeysMib sys = new SystemSnmpKeysMib();
        DataSnmpKeysMib data = new DataSnmpKeysMib();
        SnmpKeysMib mib = new SnmpKeysMib(sys, config, data);

        //Configuration read 
        int port;
        ConfigLoad ConfigLoad = new ConfigLoad();
        port = ConfigLoad.loadConfig(mib);

        //Creation and update matrix
        MSKeys MSK = MSKeys.getInstance();
        try {
            MSK.create(mib);
        } catch (Exception e) {
            e.printStackTrace();
        }   
        new Thread(() -> MSK.updateMatrix(mib)).start();

        //Server start
        DatagramSocket socket = new DatagramSocket(port);
        System.out.println("UDP Server is running on port " + port);
        ComnServer server = new ComnServer(socket, mib);
        server.startServerThread();
    }
}
