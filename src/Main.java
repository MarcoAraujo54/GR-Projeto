import java.io.IOException;
import java.net.DatagramSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        runServer();

    }
    private static void runServer() throws IOException{ 

        long S = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(S);
        timestamp.getElapsedSeconds();

        //Criar a MIB
        ConfigSnmpKeysMib config = new ConfigSnmpKeysMib();
        SystemSnmpKeysMib sys = new SystemSnmpKeysMib();
        DataSnmpKeysMib data = new DataSnmpKeysMib();
        SnmpKeysMib mib = new SnmpKeysMib(sys, config, data);

        //Leitura do config 
        int port;
        ConfigLoad ConfigLoad = new ConfigLoad();
        port = ConfigLoad.loadConfig(mib);

        //Criaçao e update da matriz
        MSKeys MSK = MSKeys.getInstance();
        MSK.create(mib);    
        new Thread(() -> MSK.updateMatrix(mib)).start();

        //Inicio do servidor
        DatagramSocket socket = new DatagramSocket(port);
        System.out.println("UDP Server is running on port " + port);
        ComnServer server = new ComnServer(socket, mib);
        server.startServerThread();
    }
}