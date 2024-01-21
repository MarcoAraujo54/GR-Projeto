import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSnmpKeysMib {

	private int dataNumberOfValidKeys;
	private List<KeysSnmpKeysMib> dataTableGeneratedKeysEntryType;
	private KeysSnmpKeysMib dataTableGeneratedKeysEntry;
	private Map<Integer, KeysSnmpKeysMib> dataTableGeneratedKeys;
	
	
	public DataSnmpKeysMib(int dataNumberOfValidKeys, List<KeysSnmpKeysMib> dataTableGeneratedKeysEntryType,
			KeysSnmpKeysMib dataTableGeneratedKeysEntry, Map<Integer, KeysSnmpKeysMib> dataTableGeneratedKeys) {
		this.dataNumberOfValidKeys = dataNumberOfValidKeys;
		this.dataTableGeneratedKeysEntryType = dataTableGeneratedKeysEntryType;
		this.dataTableGeneratedKeysEntry = dataTableGeneratedKeysEntry;
		this.dataTableGeneratedKeys = dataTableGeneratedKeys;
	}
	public DataSnmpKeysMib(DataSnmpKeysMib data){
		this.dataNumberOfValidKeys = data.dataNumberOfValidKeys;
		this.dataTableGeneratedKeysEntryType = data.dataTableGeneratedKeysEntryType;
		this.dataTableGeneratedKeysEntry = data.dataTableGeneratedKeysEntry;
		this.dataTableGeneratedKeys = data.dataTableGeneratedKeys;
	}
	public DataSnmpKeysMib(){
		this.dataNumberOfValidKeys = 0;
		this.dataTableGeneratedKeysEntryType = new ArrayList<KeysSnmpKeysMib>();
		this.dataTableGeneratedKeysEntry = new KeysSnmpKeysMib();
		this.dataTableGeneratedKeys = new HashMap<Integer,KeysSnmpKeysMib>();
	}


	public int getDataNumberOfValidKeys() {
		return dataNumberOfValidKeys;
	}

	
}
