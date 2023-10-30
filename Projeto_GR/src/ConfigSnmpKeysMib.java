
public class ConfigSnmpKeysMib {
	
	private String configMasterKey;
	private int configFirstCharOfKeysAlphabet;
	private int configCardinalityOfKeysAlphabet;

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
	
	public ConfigSnmpKeysMib() {
	    this.configMasterKey = "DefaultMasterKey";
	    this.configFirstCharOfKeysAlphabet = 0;
	    this.configCardinalityOfKeysAlphabet = 0; 
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

}
