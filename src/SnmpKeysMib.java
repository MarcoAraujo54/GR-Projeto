import java.util.HashMap;
import java.util.Scanner;

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
		map.put("0", "SnmpKeysMib");
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
			map.put("3.2","Not Acessible");
				map.put("3.2.1", this.dataSnmpKeysMib.getDataTableGeneratedKeysEntryType(1, null));
				map.put("3.2.2",this.dataSnmpKeysMib.getDataTableGeneratedKeysEntryType(2, null));
				map.put("3.2.3",this.dataSnmpKeysMib.getDataTableGeneratedKeysEntryType(3, null));
				map.put("3.2.4",this.dataSnmpKeysMib.getDataTableGeneratedKeysEntryType(4, null));
				map.put("3.2.5",this.dataSnmpKeysMib.getDataTableGeneratedKeysEntryType(5, null));
				map.put("3.2.6",this.dataSnmpKeysMib.getDataTableGeneratedKeysEntryType(6, null));
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
	public Object getOidsPosition(String oid ){
		return oids.get(oid);
	}

	public Object getOidsPosition(String oid, String KeyRequester ){
		Scanner scanner = new Scanner(oid).useDelimiter("\\.");
		int[] num= new int[5];
		int i=0;
		while(scanner.hasNextInt()){
			num[i]= scanner.hasNextInt() ? scanner.nextInt() : 0;
			i++;
			}
		oids.put(oid,this.dataSnmpKeysMib.getDataTableGeneratedKeysEntryType(num[i-1], KeyRequester));
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
	public HashMap<String,String> getmib(String StartPath,int nextpos, String KeyReq){
		HashMap<String,String> mapa = new HashMap<>();
		Scanner scanner = new Scanner(StartPath).useDelimiter("\\.");
		int[] num= new int[5];
		int i=0;
		while(scanner.hasNextInt()){
        num[i]= scanner.hasNextInt() ? scanner.nextInt() : 0;
		i++;
		}
        scanner.close();
		boolean condition=true;
		int x=num[1];
		int k=num[2];
		String firstlevel;
		int cont=0;
		for(i=num[0];i<=3;i++){
			
			condition=true;
			firstlevel=String.valueOf(i);
			
				while (condition && cont<=nextpos) {
				
				String secondlevel= firstlevel + "." + String.valueOf(x);
				//System.out.println(secondlevel);
				if(this.oids.containsKey(secondlevel)){
						cont++;
					if(k<=1){
						System.out.println(cont);
						System.out.println("novopath: "+secondlevel);
						System.out.println("mib: " + this.getOidsPosition(secondlevel));
						if(this.getOidsPosition(secondlevel).equals("Not Acessible")){
							cont--;
						}
						else{
							mapa.put(secondlevel,this.getOidsPosition(secondlevel).toString());
						}
					}
					if(this.oids.containsKey(secondlevel+"."+String.valueOf(k)) && cont<=nextpos){ 
						String thirdlevel=secondlevel+"."+String.valueOf(k);
						System.out.println("novopath: "+thirdlevel);
						System.out.println("mib: " + this.getOidsPosition(thirdlevel,KeyReq));
						mapa.put(thirdlevel,this.getOidsPosition(thirdlevel,KeyReq).toString());
						k++;
					}
					else{
						k=1;	
					} 
						
				}
				else{
					x=0;
					condition=false;	
				} 
				if(k<=1){
					x++;}
				}
					
		}
		return mapa;
	}
}