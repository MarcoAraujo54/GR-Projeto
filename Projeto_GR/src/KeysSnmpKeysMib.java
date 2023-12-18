
public class KeysSnmpKeysMib {
	
	private int keyId;
	private String keyValue;
	private String keyRequester;
	private int keyExpirationDate;
	private int keyExpirationTime; 
	private int keyVisibility;
	
	public KeysSnmpKeysMib() {
		this.keyId = 0; 
		this.keyValue = "DefaultKeyValue"; 
		this.keyRequester = "DefaultRequester"; 
		this.keyExpirationDate = 0; 
		this.keyExpirationTime = 0; 
		this.keyVisibility = 0;
	}
		
	public KeysSnmpKeysMib(int keyId, String keyValue, String keyRequester, int keyExpirationDate, int keyExpirationTime, int keyVisibility) {
	    this.keyId = keyId;
	    this.keyValue = keyValue;
	    this.keyRequester = keyRequester;
	    this.keyExpirationDate = keyExpirationDate;
	    this.keyExpirationTime = keyExpirationTime;
	    this.keyVisibility = keyVisibility;
	}

	
	public KeysSnmpKeysMib(KeysSnmpKeysMib other) {
	    this.keyId = other.keyId;
	    this.keyValue = other.keyValue;
	    this.keyRequester = other.keyRequester;
	    this.keyExpirationDate = other.keyExpirationDate;
	    this.keyExpirationTime = other.keyExpirationTime;
	    this.keyVisibility = other.keyVisibility;
	}
	
	public int getKeyId() {
		return keyId;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public String getKeyRequester() {
		return keyRequester;
	}

	public int getKeyExpirationDate() {
		return keyExpirationDate;
	}

	public int getKeyExpirationTime() {
		return keyExpirationTime;
	}

	public int getKeyVisibility() {
		return keyVisibility;
	}

	public void setKeyVisibility(int keyVisibility) {
		this.keyVisibility = keyVisibility;
	}
	
}
