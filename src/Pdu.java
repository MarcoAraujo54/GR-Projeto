import java.io.Serializable;

public class Pdu implements Serializable {
    private int securityModel; // S
    private int numSecurityParams; // NS
    private int requestId; // P
    private int primitiveType; // Y

    public Pdu(int securityModel, int numSecurityParams, int requestId, int primitiveType) {
        this.securityModel = securityModel;
        this.numSecurityParams = numSecurityParams;
        this.requestId = requestId;
        this.primitiveType = primitiveType;
    }

    public String toMyString() {
        return securityModel + "\0" + numSecurityParams + "\0" + requestId + "\0" + primitiveType + "\0";
    }

    public int getSecurityModel() {
        return securityModel;
    }

    public int getNumSecurityParams() {
        return numSecurityParams;
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
}