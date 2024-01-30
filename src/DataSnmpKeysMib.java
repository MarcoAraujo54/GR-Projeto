import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

	public void insertDataTableGeneratedKeysEntryType(String keyValue, String keyRequester, int validityTime, int keyVisibility, SnmpKeysMib mib){

		LocalDateTime dateTime = LocalDateTime.now();

		dateTime = dateTime.plusSeconds(validityTime/1000);

		int day = dateTime.getDayOfMonth();
		int month = dateTime.getMonthValue();
		int year = dateTime.getYear();
		int hour = dateTime.getHour();
		int minute = dateTime.getMinute();
		int second = dateTime.getSecond();

		int date = (int) (day + month * Math.pow(10, 2) + year * Math.pow(10, 4));
		int time = (int) (second + minute * Math.pow(10, 2) + hour * Math.pow(10, 4) + (Integer.parseInt(mib.getOidsPosition("1.6").toString()))/1000);
		
		int keyId= (this.dataTableGeneratedKeysEntryType.size()) > 0 ?  (this.dataTableGeneratedKeysEntryType.get(this.dataTableGeneratedKeysEntryType.size()-1).getKeyId())+1 : 0;

		KeysSnmpKeysMib key = new KeysSnmpKeysMib(keyId, keyValue, keyRequester, date, time, keyVisibility);
		if(keyVisibility<=2 && keyVisibility >=0){
			this.dataTableGeneratedKeysEntryType.add(key);
		}
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
		Iterator<KeysSnmpKeysMib> iterator = this.dataTableGeneratedKeysEntryType.iterator();
		while (iterator.hasNext()) {
			KeysSnmpKeysMib key = iterator.next();
			
			// Debugging: Print the values to check them
			System.out.println("Current Date: " + date + ", Expiration Date: " + key.getKeyExpirationDate());
			System.out.println("Current Time: " + time + ", Expiration Time: " + key.getKeyExpirationTime());

			if ((date >= key.getKeyExpirationDate()) && (time > key.getKeyExpirationTime())) {
				iterator.remove(); // Use iterator's remove method
			}
		}	
			
		
		//this.updtadeDataTableGeneratedKeys();
	}

	/*public void updtadeDataTableGeneratedKeys(){
		this.dataTableGeneratedKeys = new HashMap<Integer,KeysSnmpKeysMib>();
		for(KeysSnmpKeysMib key : this.dataTableGeneratedKeysEntryType){
			this.dataTableGeneratedKeys.put(key.getKeyId(), key);
		}
	}
*/
	public Object getDataTableGeneratedKeysEntryType(int id, String keyRequester){
		List<Object> readable = new ArrayList<Object>();
		this.updateDataTableGeneratedKeysEntryType();
		for(KeysSnmpKeysMib key : this.dataTableGeneratedKeysEntryType){
			if((key.getKeyRequester().equals(keyRequester) || key.getKeyVisibility() == 2)&& key.getKeyVisibility()!=0) {
				switch(id){
					case 1:
						readable.add(key.getKeyId());
					break;
					case 2:
						readable.add(key.getKeyValue());
					break;
					case 3:
						readable.add(key.getKeyRequester());
					break;
					case 4:
						readable.add(key.getKeyExpirationDate());
					break;
					case 5:
						readable.add(key.getKeyExpirationTime());
					break;
					case 6:
						readable.add(key.getKeyVisibility());
					break;
					default:
						readable.add("Incorrect OID");
				}
			}
		}
		System.out.println(readable);
		return readable;
	}
}
