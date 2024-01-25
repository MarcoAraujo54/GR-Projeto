import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pdu {
    private int securityModel; // S
    private int numSecurityParams; // NS
    private List<String> securityMecs; // Q
    private int requestId; // P
    private int primitiveType; // Y
    private int numberPairs; //NL ou NW
    private Map<String,String> pair; //L ou W
    private int numberErrors; // NR
    private  Map<String,String> errors; //R

    public Pdu(int securityModel, int numSecurityParams , int requestId, int primitiveType, int numberPairs, Map<String,String> pair , int numberErrors,  Map<String,String> errors) {
        this.securityModel = securityModel;
        this.numSecurityParams = numSecurityParams;
        this.securityMecs = new ArrayList<>();
        this.requestId = requestId;
        this.primitiveType = primitiveType;
        this.numberPairs = numberPairs;
        this.pair = pair;
        this.numberErrors = numberErrors;
        this.errors = errors;
    }

    public String toMyString() {
        return securityModel + "-" + numSecurityParams + "-" + securityMecs + "-" + requestId + "-" + primitiveType + "-" + numberPairs + "-" + pair + "-" + numberErrors + "-" + errors;
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
    
}