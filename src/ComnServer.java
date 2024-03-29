import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* Class to handle the server side of the udp connection, it handles some errors aswell
*
* @author Gustavo Oliveira
* @author José Peleja
* @author Marco Araújo
*
*/
public class ComnServer {
    private DatagramSocket socket;
    private SnmpKeysMib mib;

    public ComnServer(DatagramSocket socket, SnmpKeysMib mib){
        this.socket = socket;
        this.mib = mib;
    }

    public void startServerThread() throws IOException{
        while (true) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            //receive in the service socket
            this.socket.receive(receivePacket);
            //Creation of a new UDP socket to the response
            DatagramSocket responseSocket = new DatagramSocket();
            
            new Thread(new RequestHandler(responseSocket, mib, receivePacket)).start();
        }
    }

    private static class RequestHandler implements Runnable{
        private DatagramSocket responseSocket;
        private SnmpKeysMib mib;
        private DatagramPacket receivePacket;

        public RequestHandler(DatagramSocket responseSocket ,SnmpKeysMib mib, DatagramPacket receivePacket){
            this.responseSocket = responseSocket;
            this.mib = mib;
            this.receivePacket = receivePacket;
        }

        public void run(){
            MSKeys MSK = MSKeys.getInstance();
            String receivedMessage = new String(this.receivePacket.getData(), 0, this.receivePacket.getLength());
            
            System.out.println("Received from client: " + receivedMessage);
            Pdu pdu = new Pdu();
            
            //Process of the received message inside the class PDU
            pdu.ProcessPdu(receivedMessage);
            
            int requestId = pdu.getRequestId();
            int primitiveType = pdu.getPrimitiveType();
            String idManager = pdu.getIdManager();

            Map<String, String> pairs = pdu.getPair();            
            Map<String, String> responsePair = new HashMap<>();
            Map<String, String> Aux = new HashMap<>();
            Map<String,String> responseError = new HashMap<>();
            //List of the OIDs that are readable and writable
            List<String> readWriteOids = Arrays.asList("1.3","1.4","1.5","1.6","2.1","2.2","2.3","3.2.6");
            
            //Primitive GET
            if (primitiveType == 1){
                mib.updateData(idManager);
                for (Map.Entry<String, String> pair : pairs.entrySet()){
                    String Iid = pair.getKey();
                    String valueStr = pair.getValue();
                    try {
                        int Value = Integer.parseInt(valueStr);
                        if (mib.contains(Iid)) {
                            Aux = mib.getmib(Iid, Value, idManager);
                            responsePair.putAll(Aux);
                        } else {
                            System.out.println("Nonexistent_Oid");
                            responseError.put(Iid, "404");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Value has to be Integer");
                        responseError.put(Iid, "409");
                    }
                }                
            }
            //Primitive SET
            else if (primitiveType == 2) {
                for (Map.Entry<String, String> pair : pairs.entrySet()){
                    String Iid = pair.getKey();
                    String valueStr = pair.getValue();
                    
                    if (mib.contains(Iid)){
                        if(readWriteOids.contains(Iid)){
                            Object aux = mib.getOidsPosition(Iid);
                            mib.getOids().put(Iid, valueStr);
                            
                            // In case the request involves the master Key or the size of K new matrix should be calculated
                            if (Iid.equals("2.1") || Iid.equals("1.3")){
                                try {
                                    MSK.create(mib);
                                } catch (Exception e){
                                    mib.getOids().put(Iid,aux);
                                    responseError.put(Iid, "411");
                                }
                                //updates the time when a new matrix was created
                                mib.getSystemSnmpKeysMib().updateDate();
                                mib.getOids().put("1.1",mib.getSystemSnmpKeysMib().getSystemRestartDate());
                                mib.getOids().put("1.2",mib.getSystemSnmpKeysMib().getSystemRestartTime());
                            }
                            // Creation of new key on request
                            if (Iid.equals("3.2.6")){
                                try{ 
                                    this.mib.getDataSnmpKeysMib().
                                    insertDataTableGeneratedKeysEntryType(MSK.generateKeyC().toString(), idManager,
                                    Integer.parseInt(mib.getOidsPosition("1.6").toString()), Integer.parseInt(valueStr), Integer.parseInt(mib.getOidsPosition("1.5").toString()));
                                    mib.getOids().put("3.1", mib.getDataSnmpKeysMib().getDataNumberOfValidKeys());
                                }
                                catch(Exception e){
                                    System.out.println("List of ValidKeys is full");
                                    responseError.put(Iid, "410");
                                }

                            }
                            responsePair.put(Iid, mib.getOidsPosition(Iid).toString());
                        }else{
                            System.out.println("Oid_ReadOnly");
                            responseError.put(Iid, "405");
                        }
                    } else {
                        System.out.println("Nonexistent_Oid");
                        responseError.put(Iid, "404");
                    }
                }
            }
            //In case the list is empty should send an empty pair
            if(responsePair.size() == 0){
                responsePair.put("0","0");
            }
            //In case the list is empty should send an empty pair
            if(responseError.size() == 0){
                responseError.put("0","0");
            }                  
            int numPairs = responsePair.size();
            int errors = responseError.size();

            //Pdu formation to send to the client side of the communication
            pdu = new Pdu(0,0,idManager,requestId,0, numPairs , responsePair , errors, responseError);
            byte[] sendData = pdu.toMyString().getBytes();
            
            //Formation of the packet to be sent through the UDP socket
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.receivePacket.getAddress(), this.receivePacket.getPort());
            try {
                //Send response to the client in the new created response socket
                this.responseSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }            
        }    
    }
}