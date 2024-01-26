import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;
import java.net.DatagramSocket;

public class Comunication {
    public static void run(DatagramSocket socket, SnmpKeysMib mib,DatagramPacket receivePacket) throws IOException{
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        String[] pduParts = receivedMessage.split("-");
        //int securityModel = Integer.parseInt(pduParts[0]);
        //int numSecurityParams = Integer.parseInt(pduParts[1]);
        //String listSecurity = pduParts[2];
        int requestId = Integer.parseInt(pduParts[3]);
        int primitiveType = Integer.parseInt(pduParts[4]);
        //int numberPairs =  Integer.parseInt(pduParts[5]);
        String Pairs = pduParts[6];
        //int numberErrors = Integer.parseInt(pduParts[7]);
        //String errors = pduParts[8];
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
        socket.send(sendPacket); 
    }
}
