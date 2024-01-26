
public class ConfigSnmpKeysMib {
	
	private String configMasterKey;
	private int configFirstCharOfKeysAlphabet;
	private int configCardinalityOfKeysAlphabet;
		
	public ConfigSnmpKeysMib() {
	    this.configMasterKey = "13457";
	    this.configFirstCharOfKeysAlphabet = 33;
	    this.configCardinalityOfKeysAlphabet = 94; 
	}

	public ConfigSnmpKeysMib(String configMasterKey, int configFirstCharOfKeysAlphabet, int configCardinalityOfKeysAlphabet) {
	    this.configMasterKey = configMasterKey;
	    this.configFirstCharOfKeysAlphabet = configFirstCharOfKeysAlphabet;
	    this.configCardinalityOfKeysAlphabet = configCardinalityOfKeysAlphabet;
	}

	public ConfigSnmpKeysMib(ConfigSnmpKeysMib other) {
	    this.configMasterKey = other.configMasterKey;
	    this.configFirstCharOfKeysAlphabet = other.configFirstCharOfKeysAlphabet;
	    this.configCardinalityOfKeysAlphabet = other.configCardinalityOfKeysAlphabet;
	}

	public String getConfigMasterKey() {
		return configMasterKey;
	}

	public void setConfigMasterKey(String configMasterKey) {
		this.configMasterKey = configMasterKey;
	}

	public int getConfigFirstCharOfKeysAlphabet() {
		return configFirstCharOfKeysAlphabet;
	}

	public void setConfigFirstCharOfKeysAlphabet(int configFirstCharOfKeysAlphabet) {
		this.configFirstCharOfKeysAlphabet = configFirstCharOfKeysAlphabet;
	}

	public int getConfigCardinalityOfKeysAlphabet() {
		return configCardinalityOfKeysAlphabet;
	}

	public void setConfigCardinalityOfKeysAlphabet(int configCardinalityOfKeysAlphabet) {
		this.configCardinalityOfKeysAlphabet = configCardinalityOfKeysAlphabet;
	}
}
