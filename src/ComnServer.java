import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ComnServer {
    private DatagramSocket socket;
    private SnmpKeysMib mib;

    public ComnServer(DatagramSocket socket, SnmpKeysMib mib) {
        this.socket = socket;
        this.mib = mib;
    }

    public void startServerThread() throws IOException {
        while (true) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            InetAddress senderIPAddress = receivePacket.getAddress();
            System.out.println(senderIPAddress);
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
            MSKeys MSK = MSKeys.getInstance();
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received from client: " + receivedMessage);
            Pdu pdu = new Pdu();
            pdu.ProcessPdu(receivedMessage);
            int requestId = pdu.getRequestId();
            int primitiveType = pdu.getPrimitiveType();
            Map<String, String> pairs = pdu.getPair();            
            Map<String, String> responsePair = new HashMap<>();
            Map<String, String> Aux = new HashMap<>();
            Map<String,String> responseError = new HashMap<>();
            if (primitiveType == 1) {
                for (Map.Entry<String, String> pair : pairs.entrySet()) {
                    String Iid = pair.getKey();
                    String valueStr = pair.getValue();
                    try {
                        int Value = Integer.parseInt(valueStr);
                        if (mib.contains(Iid)) {
                            System.out.println(Value);
                            Aux = mib.getmib(Iid, Value);
                            responsePair.putAll(Aux);
                        } else {
                            System.out.println("Oid_Inexistente");
                            responseError.put(Iid, "404");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Value has to be Integer");
                        responseError.put(Iid, "409");
                    }
                }
            }
            else if (primitiveType == 2) {
                for (Map.Entry<String, String> pair : pairs.entrySet()) {
                    String Iid = pair.getKey();
                    String valueStr = pair.getValue();
                    if (mib.contains(Iid)) {
                        mib.getOids().put(Iid, valueStr);
                        responsePair.put(Iid, mib.getOidsPosition(Iid).toString());
                        if (Iid.equals("2.1") || Iid.equals("1.3")) {
                            MSK.create(mib);
                        }
                        if (Iid.equals("3.2.6")) {
                            MSK.generateKeyC();
                        }
                    } else {
                        System.out.println("Oid_Inexistente");
                        responseError.put(Iid, "404");
                    }
                }
            }
            if(responsePair.size() == 0){
                responsePair.put("0","0");
            }
            if(responseError.size() == 0){
                responseError.put("0","0");
            }                  
            int numPairs = responsePair.size();
            int Errors = responseError.size();
            pdu = new Pdu(0,0,requestId,0, numPairs , responsePair , Errors, responseError);
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