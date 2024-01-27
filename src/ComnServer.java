import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class ComnServer {
    private DatagramSocket socket;
    private SnmpKeysMib mib;

    public ComnServer(DatagramSocket socket, SnmpKeysMib mib) {
        this.socket = socket;
        this.mib = mib;
    }

    public void startServer() throws IOException {
        while (true) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            DatagramSocket responseSocket = new DatagramSocket();
            new Thread(new RequestHandler(responseSocket, mib, receivePacket)).start();
        }
    }

    private static class RequestHandler implements Runnable {
        private DatagramSocket responseSocket;
        private SnmpKeysMib mib;
        private DatagramPacket receivePacket;

        public RequestHandler(DatagramSocket responseSocket ,SnmpKeysMib mib, DatagramPacket receivePacket) {
            this.responseSocket = responseSocket;
            this.mib = mib;
            this.receivePacket = receivePacket;
        }

        public void run(){
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] pduParts = receivedMessage.split("-");
            int requestId = Integer.parseInt(pduParts[3]);
            int primitiveType = Integer.parseInt(pduParts[4]);
            String Pairs = pduParts[6];
            System.out.println("Received from client: " + receivedMessage);
            Map<String, String> responsePair = new HashMap<>();
            Map<String,String> Error = new HashMap<>();
            if(primitiveType == 1){
                String[] listPairs = Pairs.split(",");
                for(int i =0; i<listPairs.length;i++){
                    String[] aux = listPairs[i].split("=");
                    if(aux.length != 1){
                        String Iid = aux[0].replace("{", "").trim();
                        String auxValue = aux[1].replace("}", "").trim();
                        try {
                            int Value = Integer.parseInt(auxValue);
                            if(mib.contains(Iid)){
                                System.out.println(Value);
                                responsePair = mib.getmib(Iid, Value);
                            }
                            else{
                                System.out.println("Oid_Inexistente");
                                String erro = "Oid_Inexistente";
                                Error.put(Iid, erro);
                            }
                        } catch (Exception e) {
                            String erro = "Value has to be Integer";
                            Error.put("0", erro);
                        }
                    } 
                    else{
                        System.out.println("Recebido sem Argumentos");
                        String erro = "Usage set:Oid,Value";
                        Error.put("0", erro);
                    }
                }
            }
            else if(primitiveType == 2){
                String[] listPairs = Pairs.split(",");
                for(int i =0; i<listPairs.length;i++){
                    String[] aux = listPairs[i].split("=");
                    if(aux.length != 1){
                        String Iid = aux[0].replace("{", "").trim();
                        String Value = aux[1].replace("}", "").trim();
                        if(mib.contains(Iid)){
                            mib.getOids().put(Iid,Value);
                            responsePair.put(Iid, mib.getOidsPosition(Iid).toString());
                            if(Iid == "3.2.6"){
                                //MSKeys.creatC(); criar a chave
                            }
                        }
                        else{
                            System.out.println("Oid_Inexistente");
                            String erro = "Oid_Inexistente";
                            Error.put(Iid, erro);
                        }
                    }
                    else{
                        System.out.println("Recebido sem Argumentos");
                        String erro = "Usage set:Oid,Value";
                        Error.put("0", erro);
                    }                       
                }
            }
            if(responsePair.size() == 0){
                responsePair.put("0","0");
            }
            if(Error.size() == 0){
                Error.put("0","0");
            }                  
            int numPairs = responsePair.size();;
            int responseErrors = Error.size();
            Pdu pdu = new Pdu(0,0,requestId,0, numPairs , responsePair , responseErrors, Error);
            byte[] sendData = pdu.toMyString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            try {
                responseSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }            
        }    
    }
}