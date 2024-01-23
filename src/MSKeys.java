import java.util.Random;
public class MSKeys {

	//private byte M[];
	private int K;
	private byte Z[][];
	private byte M1[];
	private byte M2[];
	private byte Za[][];
	private byte Zb[][];
	private byte Zc[][];
	private byte Zd[][];
	private static MSKeys single_instance;
	
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
 
	public void printArray(byte arr[], int n)
    {
		System.out.print("|| ");
        for (int i = 0; i < n; i++) {
        	int v = arr[i]& 0xFF;
        	System.out.print(v + " ");
        }
      
        System.out.print("|| \n");
    }
	
	private void updateMValues(byte key[]){
		this.K = key.length/2;
		this.M1= new byte[K];
		this.M2= new byte[K];
		for(int i=0; i<K; i++) {
			this.M1[i]= key[i];
		}
		for(int i=0; i<K; i++) {
			this.M2[i]= key[i+K];
		}	
	}

	private void updateZa(){
		byte temp[]= new byte [this.K];
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


	private void updateZb(){
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
	private void updateZc() {
		this.Zc= new byte [K][K];
		for(int i=0; i < this.K; i++) {
			for (int j = 0; j < this.K; j++)
			{
				this.Zc[i][j]= this.Random();
			}
		}
		/*for(int i=0; i < this.K; i++) {
			printArray(this.Zc[i],this.K);
			}*/
	}
	
	private void updateZd() {
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
	 private void updateZ() {
		 this.Z= new byte [K][K];
		 
		  for (int i = 0; i < this.K; i++) {
	            for (int j = 0; j < this.K; j++) {
	                this.Z[i][j] = (byte) (this.Za[i][j] ^ this.Zb[i][j] ^ this.Zc[i][j] ^ this.Zd[i][j]);
	            }
	        }
		 
		   for (int i = 0; i < K; i++) {      
	            //	printArray(this.Z[i],this.K);     
	        }
		 
	 }

	public void update(byte key[]){
		
		this.updateMValues(key);
		 //System.out.println("\n ZAZAZAZAZAZAZAZA");
		this.updateZa();
		 // System.out.println("\n ZBZBZBZBZBZBZB");
		this.updateZb();
		// System.out.println("\n ZCZCZCZCZCZCZCZC");
		this.updateZc();
		// System.out.println("\n ZDZDZDZDZDZDZDZD");
		this.updateZd();
		//System.out.println("\n ZZZZZZZZZZZZZZZZ");
		this.updateZ();
		System.out.println("matrizes atualizadas");
	
	}
	private MSKeys(byte arr[]){
		update(arr);
	}
	public static synchronized MSKeys getInstance(byte arr[]){
		if (single_instance == null){
			single_instance = new MSKeys(arr);
			System.out.println("primeiro");
		}
		return single_instance;
	}
}

