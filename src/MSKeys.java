import java.util.Random;
import java.util.Arrays;
/**
* Generates and manages the value of the keys
*
* @author Gustavo Oliveira
* @author José Peleja
* @author Marco Araújo
*
*/
public class MSKeys {

	private int N;
	private int K;
	private byte Z[][];
	private byte M1[];
	private byte M2[];
	private byte Za[][];
	private byte Zb[][];
	private byte Zc[][];
	private byte Zd[][];
	private static MSKeys single_instance;

	private MSKeys(){
		this.N=0;
	}
	  /**
     * Returns the instance of the MSKeys class (Singleton pattern).
     *
     * @return The single instance of MSKeys.
     */
	public synchronized static MSKeys getInstance(){
		if (single_instance == null){
			single_instance = new MSKeys();
		}
		return single_instance;
	}
	/**
     * Transposes the given matrix.
     *
     * @param matrix The matrix to be transposed.
     * @return The transposed matrix.
     */
	private static byte[][] transpose(byte[][] matrix) { 
		int numRows = matrix.length;
		int numCols = matrix[0].length;
		
		byte[][] transposed	 = new byte[numCols][numRows];
		
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				transposed[j][i] = matrix[i][j];
			
			}
		}
		
		return transposed;
	}
	/**
     * Rotates the given array to the left by d positions.
     *
     * @param M The array to be rotated.
     * @param d The number of positions to rotate.
     * @return The rotated array.
     */
	private byte[] rotate( byte M[], int d) 
	{
		int n = M.length;
	        while (d > n) {
	            d = d - n;
	        }
	        byte temp[] = new byte[n - d];
	 
	        for (int i = 0; i < n - d; i++)
	            temp[i] = M[i];

	        for (int i = n - d; i < n; i++) {
	            M[i - n + d] = M[i];
	        }

	        for (int i = 0; i < n - d; i++) {
	            M[i + d] = temp[i];
	        }
	       
        return M ;
    }
	 /**
     * Updates the values of M1 and M2 based on the provided master key and SNMP MIB information.
     *
     * @param key The master key.
     * @param mib SNMP MIB information.
     */
	private void updateMValues(byte key[] ,SnmpKeysMib mib){ 

		int k = Integer.parseInt(mib.getOidsPosition("1.3").toString());
		this.K = k;
		this.M1= new byte[K];
		this.M2= new byte[K];
		for(int i=0; i<K; i++) {
			this.M1[i]= key[i];
		}
		for(int i=0; i<K; i++) {
			this.M2[i]= key[i+K];
		}	
	}
	/**
     * Generates matrix Za based on the values of M1.
     */
	private void GenZa(){
		byte temp[]= new byte [this.K];
		System.out.println(K);
		this.Za= new byte [K][K];
			for(int i=0; i < this.K; i++) {
				for (int j = 0; j < M1.length; j++) {
						temp[j] = M1[j];
					}
				temp = rotate(temp,i);
				for(int j=0; j< this.K; j++) {
					this.Za[i][j]=temp[j];
				}
				// printArray(this.Za[i],this.K);
			}
		}
	 /**
     * Generates matrix Zb based on the values of M2.
     */
	private void GenZb(){
		byte temp2[]= new byte [this.K];
		this.Zb= new byte [K][K];
		for(int i=0; i < this.K; i++) {
			for (int j = 0; j < M2.length; j++) {
					temp2[j] = M2[j];
				}
			temp2 = rotate(temp2,i);
			for(int j=0; j < this.K; j++) {
				this.Zb[i][j]=temp2[j];
			}
			
		}
		this.Zb = transpose(Zb);
		for(int i=0; i < this.K; i++) {
		//printArray(this.Zb[i],this.K);
		}
	}
	/**
     * Generates matrix Zc with random values.
     */
	private void GenZc() {
		this.Zc= new byte [K][K];
		for(int i=0; i < this.K; i++) {
			for (int j = 0; j < this.K; j++)
			{
				this.Zc[i][j]= this.Random();
			}
		}
	}
	 /**
     * Generates matrix Zd with random values.
     */
	private void GenZd() {
		this.Zd= new byte [K][K];
		for(int i=0; i < this.K; i++) {
			for (int j = 0; j < this.K; j++)
			{
				this.Zd[i][j]= this.Random();
			}
		}
		for(int i=0; i < this.K; i++) {
			//printArray(this.Zd[i],this.K);
			}
	}

	private byte Random() {
		return (byte)  new Random().nextInt(256); //Gere valores entre 0 e 255  
		
	}
	 /**
     * Generates matrix Z by combining Za, Zb, Zc, and Zd.
     */
	private void GenZ() {
		this.Z= new byte [K][K];	
		for (int i = 0; i < this.K; i++) {
			for (int j = 0; j < this.K; j++) {
				this.Z[i][j] = (byte) (this.Za[i][j] ^ this.Zb[i][j] ^ this.Zc[i][j] ^ this.Zd[i][j]);
			}
		}			
	}
	 /**
     * Creates matrices Za, Zb, Zc, Zd, and Z based on the provided MIB information.
     *
     * @param mib MIB information.
	 * @throws Exception 
     */
	public void create(SnmpKeysMib mib) throws Exception{	
		
		String MKey = (mib.getOidsPosition("2.1")).toString();
		if(Integer.parseInt(mib.getOidsPosition("1.3").toString())> MKey.length()/2){
			throw new Exception("k");
		}
		byte[] MasterKey = MKey.getBytes();
		MSKeys MSK = getInstance();
		MSK.updateMValues(MasterKey,mib);
		 //System.out.println("\n ZAZAZAZAZAZAZAZA");
		MSK.GenZa();
		 // System.out.println("\n ZBZBZBZBZBZBZB");
		MSK.GenZb();
		// System.out.println("\n ZCZCZCZCZCZCZCZC");
		MSK.GenZc();
		// System.out.println("\n ZDZDZDZDZDZDZDZD");
		MSK.GenZd();
		//System.out.println("\n ZZZZZZZZZZZZZZZZ");
		MSK.GenZ();
		
	}

	public void printArray(byte arr[], int n)
    {
		System.out.print("|| ");
        for (int i = 0; i < n; i++) {
        	int v = arr[i]& 0xFF;
        	System.out.print(v + " ");
        }
      
        System.out.print("|| \n");
    }
	/**
     * Generates a key C based on the current state of matrices Z.
     *
     * @return The generated key C.
     */
	public byte[] generateKeyC() {
		MSKeys MSK = getInstance();
		int n = MSK.N;
		int seed = n + MSK.Z[0][0];
        Random rand = new Random(seed);
        int i = rand.nextInt(0,MSK.K-1);
        byte[] Zi = MSK.Z[i];
		seed = MSK.Z[i][0];
		rand = new Random(seed);
        int j = rand.nextInt(0,MSK.K-1);
        byte[] Zj = new byte[MSK.K];
        for (int k = 0; k < MSK.K; k++) {
            Zj[k] = MSK.Z[k][j];
        }
        byte[] C = new byte[K];
        for (int k = 0; k < MSK.K; k++) {
            C[k] = (byte) (Zi[k] ^ Zj[k]);
        }
		
        return C;
    }
	/**
     * Updates the matrix Z in a continuous loop based on MIB information.
     *
     * @param mib MIB information.
     */
	public void updateMatrix(SnmpKeysMib mib) {
        while (true) {
			String stringValue = mib.getOidsPosition("1.4").toString();
			int finalT = Integer.parseInt(stringValue);
			MSKeys MSK = getInstance();
			for(int j=0;j<MSK.K;j++){
				int i = new Random().nextInt(MSK.K);
				
				MSK.Z[j] = MSK.rotate(MSK.Z[j], i);
			}	
			MSK.Z=transpose(MSK.Z);
			for(int j=0;j<MSK.K;j++){
				int i = new Random().nextInt(MSK.K);
			
				MSK.Z[j] = MSK.rotate(MSK.Z[j], i);
			}
		
			MSK.Z=transpose(MSK.Z);
			try {
				MSK.N++;	
				Thread.sleep(finalT);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		
        }
    }

	
}
