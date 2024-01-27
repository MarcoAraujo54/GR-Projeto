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
	public void getmib(String StartPath,int nextpos){
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
		System.out.println("kkkkkkkkkkkkk   "+k);
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
						}
					if(this.oids.containsKey(secondlevel+"."+String.valueOf(k))){ 
						String thirdlevel=secondlevel+"."+String.valueOf(k);
						System.out.println("novopath: "+thirdlevel);
						System.out.println("mib: " + this.getOidsPosition(thirdlevel));
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
				System.out.println("aqui");
				if(k<=1){
					x++;}
				}
					
		}
	}
}