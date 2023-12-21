import java.util.List;
import java.util.Map;

public class DataSnmpKeysMib {
	private int dataNumberOfValidKeys;
	private List<KeysSnmpKeysMib> DataTableGeneratedKeysEntryType;
	private KeysSnmpKeysMib dataTableGeneratedKeysEntry;
	private Map<Integer, KeysSnmpKeysMib> dataTableGeneratedKeys;
	
	public int getDataNumberOfValidKeys() {
		return dataNumberOfValidKeys;
	}
	public void setDataNumberOfValidKeys(int dataNumberOfValidKeys) {
		this.dataNumberOfValidKeys = dataNumberOfValidKeys;
	}
	public List<KeysSnmpKeysMib> getDataTableGeneratedKeysEntryType() {
		return DataTableGeneratedKeysEntryType;
	}
	public void setDataTableGeneratedKeysEntryType(List<KeysSnmpKeysMib> dataTableGeneratedKeysEntryType) {
		DataTableGeneratedKeysEntryType = dataTableGeneratedKeysEntryType;
	}
	public KeysSnmpKeysMib getDataTableGeneratedKeysEntry() {
		return dataTableGeneratedKeysEntry;
	}
	public void setDataTableGeneratedKeysEntry(KeysSnmpKeysMib dataTableGeneratedKeysEntry) {
		this.dataTableGeneratedKeysEntry = dataTableGeneratedKeysEntry;
	}
	public Map<Integer, KeysSnmpKeysMib> getDataTableGeneratedKeys() {
		return dataTableGeneratedKeys;
	}
	public void setDataTableGeneratedKeys(Map<Integer, KeysSnmpKeysMib> dataTableGeneratedKeys) {
		this.dataTableGeneratedKeys = dataTableGeneratedKeys;
	} 

	
}
