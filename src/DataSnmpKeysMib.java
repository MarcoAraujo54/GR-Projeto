import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.lang.Math;

public class DataSnmpKeysMib extends KeysSnmpKeysMib {

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

	public void insertDataTableGeneratedKeysEntryType(int keyId, String ketValue, int keyRequester, int validityTime, int keyVisibility){

		LocalDateTime dateTime = LocalDateTime.now();

		dateTime = dateTime.plusSeconds(validityTime/1000);

		int day = dateTime.getDayOfMonth();
		int month = dateTime.getMonthValue();
		int year = dateTime.getYear();
		int hour = dateTime.getHour();
		int minute = dateTime.getMinute();
		int second = dateTime.getSecond();

		int date = (int) (day + month * Math.pow(10, 2) + year * Math.pow(10, 4));
		int time = (int) (second + minute * Math.pow(10, 2) + hour * Math.pow(10, 4));

		KeysSnmpKeysMib key = new KeysSnmpKeysMib(keyId, keyValue, keyRequest.toString(), date, time, keyVisibility);
		this.dataTableGeneratedKeysEntry.add(key);
		this.updateDataTableGeneratedKeysEntryType();
	}

	public void updateDataTableGeneratedKeysEntryType(){

		LocalDateTime dateTime = LocalDateTime.now();

		int day = dateTime.getDayOfMonth();
		int month = dateTime.getMonthValue();
		int year = dateTime.getYear();
		int hour = dateTime.getHour();
		int minute = dateTime.getMinute();
		int second = dateTime.getSecond();

		int date = (int) (day + month * Math.pow(10, 2) + year * Math.pow(10, 4));
		int time = (int) (second + minute * Math.pow(10, 2) + hour * Math.pow(10, 4));

		for(KeysSnmpKeysMib key : this.dataTableGeneratedKeysEntry){
			if((date > key.getKeyExpirationDate) && (time > keyExpirationTime)){
				this.dataTableGeneratedKeysEntry.remove(key);
			}
		}
		this.updtadeDataTableGeneratedKeys();
	}

	public void updtadeDataTableGeneratedKeys(){
		this.dataTableGeneratedKeys = new HashMap<Integer,KeysSnmpKeysMib>();
		for(KeysSnmpKeysMib key : this.dataTableGeneratedKeysEntry){
			this.dataTableGeneratedKeys.put(key.getKeyId(), key);
		}
	}
}
