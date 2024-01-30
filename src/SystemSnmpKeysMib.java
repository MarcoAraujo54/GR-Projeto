import java.time.LocalDateTime;

public class SystemSnmpKeysMib {
	
	private int systemRestartDate;
	private int systemRestartTime;
	private int systemKeySize;
	private int systemIntervalUpdate;
	private int systemMaxNumberOfKey;
	private int systemKeysTimeToLive;
		
	public SystemSnmpKeysMib() {
		this.updateDate();
		this.systemKeySize = 0; 
		this.systemIntervalUpdate = 0; 
		this.systemMaxNumberOfKey = 0; 
		this.systemKeysTimeToLive = 0; 
	}
	
	public SystemSnmpKeysMib(int systemRestartDate, int systemRestartTime, int systemKeySize, int systemIntervalUpdate, int systemMaxNumberOfKey, int systemKeysTimeToLive) {
	    this.systemRestartDate = systemRestartDate;
	    this.systemRestartTime = systemRestartTime;
	    this.systemKeySize = systemKeySize;
	    this.systemIntervalUpdate = systemIntervalUpdate;
	    this.systemMaxNumberOfKey = systemMaxNumberOfKey;
	    this.systemKeysTimeToLive = systemKeysTimeToLive;
	}
	
	public SystemSnmpKeysMib(SystemSnmpKeysMib other) {
	    this.systemRestartDate = other.systemRestartDate;
	    this.systemRestartTime = other.systemRestartTime;
	    this.systemKeySize = other.systemKeySize;
	    this.systemIntervalUpdate = other.systemIntervalUpdate;
	    this.systemMaxNumberOfKey = other.systemMaxNumberOfKey;
	    this.systemKeysTimeToLive = other.systemKeysTimeToLive;
	}
	
	public int getSystemRestartDate() {
		return this.systemRestartDate;
	}
	
	public void setSystemRestartDate(int systemRestartDate) {
		this.systemRestartDate = systemRestartDate;
	}
	
	public int getSystemRestartTime() {
		return this.systemRestartTime;
	}
	
	public void setSystemRestartTime(int systemRestartTime) {
		this.systemRestartTime = systemRestartTime;
	}
	
	public int getSystemKeySize() {
		return systemKeySize;
	}
	
	public void setSystemKeySize(int systemKeySize) {
		this.systemKeySize = systemKeySize;
	}
	
	public int getSystemIntervalUpdate() {
		return systemIntervalUpdate;
	}
	
	public void setSystemIntervalUpdate(int systemIntervalUpdate) {
		this.systemIntervalUpdate = systemIntervalUpdate;
	}
	
	public int getSystemMaxNumberOfKey() {
		return systemMaxNumberOfKey;
	}
	
	public void setSystemMaxNumberOfKey(int systemMaxNumberOfKey) {
		this.systemMaxNumberOfKey = systemMaxNumberOfKey;
	}
	
	public int getSystemKeysTimeToLive() {
		return systemKeysTimeToLive;
	}
	
	public void setSystemKeysTimeToLive(int systemKeysTimeToLive) {
		this.systemKeysTimeToLive = systemKeysTimeToLive;
	}
	public void updateDate(){
		LocalDateTime dateTime = LocalDateTime.now();

		int day = dateTime.getDayOfMonth();
		int month = dateTime.getMonthValue();
		int year = dateTime.getYear();
		int hour = dateTime.getHour();
		int minute = dateTime.getMinute();
		int second = dateTime.getSecond();

		this.systemRestartDate = (int) (day + month * Math.pow(10, 2) + year * Math.pow(10, 4));
		this.systemRestartTime = (int) (second + minute * Math.pow(10, 2) + hour * Math.pow(10, 4));
	}
}
