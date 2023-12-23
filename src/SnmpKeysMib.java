
public class SnmpKeysMib {
	private SystemSnmpKeysMib systemSnmpKeysMib;
	private ConfigSnmpKeysMib configSnmpKeysMib;
	private DataSnmpKeysMib dataSnmpKeysMib;

	
	public SnmpKeysMib(SystemSnmpKeysMib systemSnmpKeysMib, ConfigSnmpKeysMib configSnmpKeysMib,
			DataSnmpKeysMib dataSnmpKeysMib) {
		this.systemSnmpKeysMib = systemSnmpKeysMib;
		this.configSnmpKeysMib = configSnmpKeysMib;
		this.dataSnmpKeysMib = dataSnmpKeysMib;
	}
	public SnmpKeysMib(SnmpKeysMib s) {
		this.systemSnmpKeysMib = s.systemSnmpKeysMib;
		this.configSnmpKeysMib = s.configSnmpKeysMib;
		this.dataSnmpKeysMib = s.dataSnmpKeysMib;
	}
	
	public SnmpKeysMib() {
		this.systemSnmpKeysMib = new SystemSnmpKeysMib();
		this.configSnmpKeysMib = new ConfigSnmpKeysMib();
		this.dataSnmpKeysMib = new DataSnmpKeysMib();
	}

	public SystemSnmpKeysMib getSystemSnmpKeysMib() {
		return systemSnmpKeysMib;
	}
	public void setSystemSnmpKeysMib(SystemSnmpKeysMib systemSnmpKeysMib) {
		this.systemSnmpKeysMib = systemSnmpKeysMib;
	}
	public ConfigSnmpKeysMib getConfigSnmpKeysMib() {
		return configSnmpKeysMib;
	}
	public void setConfigSnmpKeysMib(ConfigSnmpKeysMib configSnmpKeysMib) {
		this.configSnmpKeysMib = configSnmpKeysMib;
	}
	public DataSnmpKeysMib getDataSnmpKeysMib() {
		return dataSnmpKeysMib;
	}
	public void setDataSnmpKeysMib(DataSnmpKeysMib dataSnmpKeysMib) {
		this.dataSnmpKeysMib = dataSnmpKeysMib;
	}
	
}
