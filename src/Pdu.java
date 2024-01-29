import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pdu {
    private int securityModel; // S
    private int numSecurityParams; // NS
    private String idManager; // ID do Gestor
    private List<String> securityMecs; // Q
    private int requestId; // P
    private int primitiveType; // Y
    private int numberPairs; //NL ou NW
    private Map<String,String> pair; //L ou W
    private int numberErrors; // NR
    private Map<String,String> errors; //R

    public Pdu(int securityModel, int numSecurityParams ,String idManager, int requestId, int primitiveType, int numberPairs, Map<String,String> pair , int numberErrors,  Map<String,String> errors) {
        this.securityModel = securityModel;
        this.numSecurityParams = numSecurityParams;
        this.idManager = idManager;
        this.securityMecs = new ArrayList<>();
        this.requestId = requestId;
        this.primitiveType = primitiveType;
        this.numberPairs = numberPairs;
        this.pair = pair;
        this.numberErrors = numberErrors;
        this.errors = errors;
    }

    public Pdu() {
        this.securityModel = 0;
        this.numSecurityParams = 0; 
        this.idManager = "";
        this.requestId = 0; 
        this.primitiveType = 0;
        this.numberPairs = 0;
        this.pair = new HashMap<>(); 
        this.numberErrors = 0; 
        this.errors = new HashMap<>();
    }

    public String toMyString() {
        return this.securityModel + "-" + this.numSecurityParams + "-" + this.securityMecs + "-" + this.idManager + "-" + this.requestId + "-" + this.primitiveType + "-" + this.numberPairs + "-" + this.pair + "-" + this.numberErrors + "-" + this.errors;
    }
    
    public int getSecurityModel() {
        return securityModel;
    }

    public int getNumSecurityParams() {
        return numSecurityParams;
    }

    public List<String> getSecurityMecs() {
        return securityMecs;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getPrimitiveType() {
        return primitiveType;
    }
    
    public void setSecurityModel(int securityModel) {
        this.securityModel = securityModel;
    }

    public void setNumSecurityParams(int numSecurityParams) {
        this.numSecurityParams = numSecurityParams;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setPrimitiveType(int primitiveType) {
        this.primitiveType = primitiveType;
    }

    public Map<String,String> getPair() {
        return pair;
    }

    public void setPair(Map<String,String> par) {
        this.pair = par;
    }

    public int getNumberPairs() {
        return numberPairs;
    }

    public void setNumberPairs(int numberPairs) {
        this.numberPairs = numberPairs;
    }

    public int getNumberErrors() {
        return numberErrors;
    }

    public Map<String,String> getErrors() {
        return errors;
    }

    public void setNumberErrors(int numberErrors) {
        this.numberErrors = numberErrors;
    }

    public void setErrors( Map<String,String> errors) {
        this.errors = errors;
    }
    
    public String getIdManager() {
        return idManager;
    }

    public void setIdManager(String idManager) {
        this.idManager = idManager;
    }

    public void setSecurityMecs(List<String> securityMecs) {
        this.securityMecs = securityMecs;
    }

    public void ProcessPdu(String receivedMessage) {
        String[] pduParts = receivedMessage.split("-");
        this.securityModel = Integer.parseInt(pduParts[0]);
        this.numSecurityParams = Integer.parseInt(pduParts[1]);
        this.idManager = (pduParts[3]);
        this.requestId = Integer.parseInt(pduParts[4]);
        this.primitiveType = Integer.parseInt(pduParts[5]);
        this.numberPairs = Integer.parseInt(pduParts[6]);
        this.pair = parsePair(pduParts[7]);
        this.numberErrors = Integer.parseInt(pduParts[8]);
        this.errors = new HashMap<>();
    }

    private Map<String, String> parsePair(String pairsStr) {
        Map<String, String> pairsMap = new HashMap<>();
        String[] pairs = pairsStr.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String Iid = keyValue[0].replace("{", "").trim();
            String Value = keyValue[1].replace("}", "").trim();
            pairsMap.put(Iid, Value);
        }
    return pairsMap;
    }
}