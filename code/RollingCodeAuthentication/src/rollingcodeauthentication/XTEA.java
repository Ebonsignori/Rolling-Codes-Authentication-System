package rollingcodeauthentication; // Package for main application logic 

/* 
 * XTEA Encryption and Decryption algorithms. Includes TEA implementations 
 * as well for potential option of using TEA instead of XTEA
 */
public class XTEA {
    private int rounds = 64;
    private long delta = Long.parseLong("2654435769"); 
    private int[] keys;
    
    /* Initalize XTEA Object with key */
    public XTEA(int[] key) {
        this.keys = key;
    }
    
    /* Takes 64-bit block and returns the XTEA encrypted bits */
    public long encrypt(long block) { 
        // Split 64 bits into right and left halves and convert to integers
        int leftHalf = (int)(block >> 32);
        int rightHalf = (int)block;     
        
        // Initialize sum and encrypt using XTEA algorithm
        long sum = 0;
        for (int i = 0; i < rounds; i++) { 
            leftHalf += (rightHalf << 4 ^ rightHalf >>> 5) + rightHalf ^ keys[(int) (sum & 0x3)] + sum; 
            sum += delta; 
            rightHalf += keys[(int) ((sum >>> 11) & 3)] + sum ^ leftHalf + (leftHalf << 4 ^ leftHalf >>> 5); 
        }
        
        long encrypted = (long)leftHalf << 32 | rightHalf & 0xFFFFFFFFL;
                
        return encrypted;
    }
            
    /* Takes 64-bit block and returns the XTEA decrypted bits */
    public long decrypt(long block) {
        // Split 64 bits into right and left halves and convert to integers
        int leftHalf = (int)(block >> 32);
        int rightHalf = (int)block;
        
        // Initialize sum and decrypt using XTEA algorithm
        long sum = rounds * delta; // Different for decryption
        for (int i = 0; i < rounds; i++) { 
            rightHalf -= keys[(int) ((sum >>> 11) & 3)] + sum ^ leftHalf + (leftHalf << 4 ^ leftHalf >>> 5); 
            sum -= delta; 
            leftHalf -= (rightHalf << 4 ^ rightHalf >>> 5) + rightHalf ^ keys[(int) (sum & 0x3)] + sum; 
        }
        
        long decrypted = (long)leftHalf << 32 | rightHalf & 0xFFFFFFFFL;
                
        return decrypted;
    }
    
    /* Takes 64-bit block and returns the TEA encrypted bits */
    public long TEAEncrypt(long block) { 
        // Split 64 bits into right and left halves and convert to integers
        int L = (int)(block >> 32);
        int R = (int)block;     
        
        // Initialize sum and encrypt using TEA algorithm
        long sum = 0;
        long delta = Long.parseLong("2654435769");
        for (int i = 0; i < 32; i++) { 
            sum += delta; 
            L += (((R << 4) + keys[0]) ^ (R + sum) ^ (R >> 5) + keys[1]);
            R += (((L << 4) + keys[2]) ^ (L + sum) ^ (L >> 5) + keys[3]);
        }
        
        long encrypted = (long)L << 32 | R & 0xFFFFFFFFL;
               
        return encrypted;
    }
    
    /* Takes 64-bit block and returns the XTEA encrypted bits */
    public long TEADecrypt(long block) { 
        // Split 64 bits into right and left halves and convert to integers
        int L = (int)(block >> 32);
        int R = (int)block;     
        
        // Initialize sum and encrypt using TEA algorithm
        long delta = Long.parseLong("2654435769");
        long sum = delta << 5;
        
        for (int i = 0; i < 32; i++) { 
            R -= (((L << 4) + keys[2]) ^ (L + sum) ^ (L >> 5) + keys[3]);
            L -= (((R << 4) + keys[0]) ^ (R + sum) ^ (R >> 5) + keys[1]);
            sum -= delta; 
        }
        
        long decrypted = (long)L << 32 | R & 0xFFFFFFFFL;
               
        return decrypted;
    }
        
}

