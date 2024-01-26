import java.util.HashMap;

public class SnmpKeysMib {
	private SystemSnmpKeysMib systemSnmpKeysMib;
	private ConfigSnmpKeysMib configSnmpKeysMib;
	private DataSnmpKeysMib dataSnmpKeysMib;
	private HashMap<String,Object> oids;
	
	public SnmpKeysMib(SystemSnmpKeysMib systemSnmpKeysMib, ConfigSnmpKeysMib configSnmpKeysMib, DataSnmpKeysMib dataSnmpKeysMib) {
		this.systemSnmpKeysMib = systemSnmpKeysMib;
		this.configSnmpKeysMib = configSnmpKeysMib;
		this.dataSnmpKeysMib = dataSnmpKeysMib;
		this.oids=mapa();
	}
	public SnmpKeysMib(SnmpKeysMib s) {
		this.systemSnmpKeysMib = s.systemSnmpKeysMib;
		this.configSnmpKeysMib = s.configSnmpKeysMib;
		this.dataSnmpKeysMib = s.dataSnmpKeysMib;
		this.oids=mapa();
	}
	
	public SnmpKeysMib() {
		this.systemSnmpKeysMib = new SystemSnmpKeysMib();
		this.configSnmpKeysMib = new ConfigSnmpKeysMib();
		this.dataSnmpKeysMib = new DataSnmpKeysMib();
		this.oids=mapa();
	}
	private HashMap<String,Object> mapa(){

		HashMap<String,Object> map = new HashMap<>();
		map.put("1", this.systemSnmpKeysMib);
			map.put("1.1",this.systemSnmpKeysMib.getSystemRestartDate());
			map.put("1.2",this.systemSnmpKeysMib.getSystemRestartTime());
			map.put("1.3",this.systemSnmpKeysMib.getSystemKeySize());
			map.put("1.4",this.systemSnmpKeysMib.getSystemIntervalUpdate());
			map.put("1.5",this.systemSnmpKeysMib.getSystemMaxNumberOfKey());
			map.put("1.6",this.systemSnmpKeysMib.getSystemKeysTimeToLive());
		map.put("2", this.configSnmpKeysMib);
			map.put("2.1",this.configSnmpKeysMib.getConfigMasterKey());
			map.put("2.2",this.configSnmpKeysMib.getConfigFirstCharOfKeysAlphabet());
			map.put("2.3",this.configSnmpKeysMib.getConfigCardinalityOfKeysAlphabet());
		map.put("3", this.dataSnmpKeysMib);
			map.put("3.1",this.dataSnmpKeysMib.getDataNumberOfValidKeys());
				map.put("3.1.1","Not Acessible");
			map.put("3.2","Not Acessible");
				map.put("3.2.1","Not Acessible");
				map.put("3.2.2","Not Acessible");
				map.put("3.2.3","Not Acessible");
				map.put("3.2.4","Not Acessible");
				map.put("3.2.5","Not Acessible");
				map.put("3.2.6","Not Acessible");
		return map;
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
	public Object getOidsPosition(String oid){
		return oids.get(oid);
	}
	public HashMap<String,Object> getOids(){
		return oids;
	}
	public boolean contains(String key){
		if(oids.containsKey(key)){
			return true;
		}
		return false;
	}
	public void getmib(){
		boolean condition=true;
		int x=0;
		int k=1;
		String firstlevel;
		for(int i=1;i<=3;i++){
			condition=true;
			firstlevel=String.valueOf(i);
		   
			while (condition) {
				x++;
			  
				String secondlevel= firstlevel + "." + String.valueOf(x);
				System.out.println(secondlevel);
				//arrayIntParaString(path); 

				if(this.oids.containsKey(secondlevel)){
					if(this.oids.containsKey(secondlevel+"."+String.valueOf(k))){ 
						String thirdlevel=secondlevel+"."+String.valueOf(k);
						System.out.println("novopath: "+thirdlevel);
						System.out.println("mib: " + this.getOidsPosition(thirdlevel));
						k++;       
					}
					else{
						k=1;
					} 
						System.out.println("novopath: "+secondlevel);
						System.out.println("mib: " + this.getOidsPosition(secondlevel));
				}
				else{
					x=0;
					condition=false;
				}                 
			}
		}

	}
}