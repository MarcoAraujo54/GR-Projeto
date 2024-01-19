import java.util.ArrayList;
import java.util.List;

public class Pdu {
    private int securityModel; // S
    private int numSecurityParams; // NS
    private ArrayList<String> securityMecs; // Q
    private int requestId; // P
    private int primitiveType; // Y
    private int numberPairs; //NL ou NW
    private List<String> par; //L ou W

    public Pdu(int securityModel, int numSecurityParams , int requestId, int primitiveType, int numberPairs, List<String> par ) {
        this.securityModel = securityModel;
        this.numSecurityParams = numSecurityParams;
        this.securityMecs = new ArrayList<>();
        this.requestId = requestId;
        this.primitiveType = primitiveType;
        this.numberPairs = numberPairs;
        this.par = par;
    }

    public String toMyString() {
        return securityModel + "-" + numSecurityParams + "-" + securityMecs + "-" + requestId + "-" + primitiveType + "-" + numberPairs + "-" + par;
    }
    
    public int getSecurityModel() {
        return securityModel;
    }

    public int getNumSecurityParams() {
        return numSecurityParams;
    }

    public ArrayList<String> getSecurityMecs() {
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

    public List<String> getPar() {
        return par;
    }

    public void setPar(List<String> par) {
        this.par = par;
    }

    public int getNumberPairs() {
        return numberPairs;
    }

    public void setNumberPairs(int numberPairs) {
        this.numberPairs = numberPairs;
    }
    
}