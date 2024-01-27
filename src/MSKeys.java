import java.util.Random;
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

	private void GenZa(){
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
	private void GenZc() {
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
	 private void GenZ() {
		 this.Z= new byte [K][K];
		 
		  for (int i = 0; i < this.K; i++) {
	            for (int j = 0; j < this.K; j++) {
	                this.Z[i][j] = (byte) (this.Za[i][j] ^ this.Zb[i][j] ^ this.Zc[i][j] ^ this.Zd[i][j]);
	            }
	        }
		 
		   for (int i = 0; i < K; i++) {      
	            //	printArray(this.Z[i],this.K);     
	        }
			N++;		 
	 }

	public void create(SnmpKeysMib mib){
		String MKey = (mib.getOidsPosition("2.1")).toString();
		byte[] MasterKey = MKey.getBytes();
		MSKeys MSK = getInstance();
		MSK.updateMValues(MasterKey);
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
		for (int i = 0; i < K; i++) {      
				printArray(this.Z[i],this.K);     
		}
		System.out.println("matrizes criadas");
	
	}
	/*private byte[] generateKeyC() {
        Random rand = new Random();
        int i = rand.nextInt(N + Z[0][0]) % K;
        byte[] Zi = Z[i];
        int j = rand.nextInt(Z[i][0]) % K;
        byte[] Zj = new byte[K];
        for (int k = 0; k < K; k++) {
            Zj[k] = Z[k][j];
        }

        byte[] C = new byte[K];
        for (int k = 0; k < K; k++) {
            C[k] = (byte) (Zi[k] ^ Zj[k]); // xor entre Zi* e Zj*
        }
        return C;
    }*/
	public void updateMatrix(SnmpKeysMib mib) {
        while (true) {
			String stringValue = mib.getOidsPosition("1.4").toString();
			int finalT = Integer.parseInt(stringValue);
			MSKeys MSK = getInstance();
			int i = new Random().nextInt(MSK.K-1);
			System.out.println(i);
			MSK.Z[i]=MSK.rotate(MSK.Z[i], i);
			for ( i = 0; i < MSK.K; i++) {      
	            	printArray(MSK.Z[i],MSK.K);     
	        }
			System.out.println();
            try {
                Thread.sleep(finalT);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
	private MSKeys(){
		
	}
	public synchronized static MSKeys getInstance(){
		if (single_instance == null){
			single_instance = new MSKeys();
			System.out.println("primeiro");
		}
		return single_instance;
	}
}

