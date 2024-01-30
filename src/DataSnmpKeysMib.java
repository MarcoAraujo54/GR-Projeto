import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.lang.Math;
/**
* Data group of the snmpKeysMib
*
* @author Gustavo Oliveira
* @author José Peleja
* @author Marco Araújo
*
*/
public class DataSnmpKeysMib extends KeysSnmpKeysMib {

	private int dataNumberOfValidKeys;
	private List<KeysSnmpKeysMib> dataTableGeneratedKeysEntryType;
	private KeysSnmpKeysMib dataTableGeneratedKeysEntry;
	
	
	public DataSnmpKeysMib(int dataNumberOfValidKeys, List<KeysSnmpKeysMib> dataTableGeneratedKeysEntryType,
		KeysSnmpKeysMib dataTableGeneratedKeysEntry, Map<Integer, KeysSnmpKeysMib> dataTableGeneratedKeys) {
		this.dataNumberOfValidKeys = dataNumberOfValidKeys;
		this.dataTableGeneratedKeysEntryType = dataTableGeneratedKeysEntryType;
		this.dataTableGeneratedKeysEntry = dataTableGeneratedKeysEntry;
	}
	public DataSnmpKeysMib(DataSnmpKeysMib data){
		this.dataNumberOfValidKeys = data.dataNumberOfValidKeys;
		this.dataTableGeneratedKeysEntryType = data.dataTableGeneratedKeysEntryType;
		this.dataTableGeneratedKeysEntry = data.dataTableGeneratedKeysEntry;
		
	}
	public DataSnmpKeysMib(){
		this.dataNumberOfValidKeys = 0;
		this.dataTableGeneratedKeysEntryType = new ArrayList<KeysSnmpKeysMib>();
		this.dataTableGeneratedKeysEntry = new KeysSnmpKeysMib();
	}

	public int getDataNumberOfValidKeys() {
		return dataNumberOfValidKeys;
	}

	public void insertDataTableGeneratedKeysEntryType(String keyValue, String keyRequester, int validityTime, int keyVisibility, int maxSize) throws Exception{

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
		
		int keyId= (this.dataTableGeneratedKeysEntryType.size()) > 0 ?  (this.dataTableGeneratedKeysEntryType.get(this.dataTableGeneratedKeysEntryType.size()-1).getKeyId())+1 : 0;

		KeysSnmpKeysMib key = new KeysSnmpKeysMib(keyId, keyValue, keyRequester, date, time, keyVisibility);
		this.updateDataTableGeneratedKeysEntryType();
		if(keyVisibility<=2 && keyVisibility >=0){
			if(this.dataTableGeneratedKeysEntryType.size()<maxSize){
				this.dataTableGeneratedKeysEntryType.add(key);
			}
			else{
				throw new Exception("size");
			}
		}
		this.updateDataTableGeneratedKeysEntryType();
	}

	/**
	*
	* Function to update the list of valid keys removing the entries that are no longer valid
	* 
	*/
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
			if ((date >= key.getKeyExpirationDate()) && (time > key.getKeyExpirationTime())) {
				iterator.remove(); 
			}
		}			
		this.dataNumberOfValidKeys= this.dataTableGeneratedKeysEntryType.size();
	}

	/**
	*
	* Function to hand gets of the list values depending on the id of the manager/client that requests the key
	*
	* @param id last number of the oid representing which value to get from KeysSnmpKeysMib.
	* @param keyRequeter the identification of the manager/client that requested the key
 	* 
	* @return readble object with the list of readble values for the manager/client 
	*
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
		return readable;
	}
}
