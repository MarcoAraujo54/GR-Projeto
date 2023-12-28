public class Mainclient {
    public static void main(String[] args) {
        int serverPort = 12345;
        udp_client client = new udp_client();
        byte[] chave = {'A', 2, 'P', 4, 'x',6,7,'v',9,10,11,12};
        Thread clientThread = new Thread(() -> client.runClient("localhost", serverPort));
        
        Thread updateMSKEYS = new Thread(() -> {
            try {
                MSKeys.main(chave);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });  
        updateMSKEYS.start();

        clientThread.start();
    }

}