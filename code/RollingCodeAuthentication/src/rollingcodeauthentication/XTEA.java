package rollingcodeauthentication;

import java.math.BigInteger;

/* XTEA Encryption and Decryption algorithms */
public class XTEA {
    private int rounds = 32;
    private int delta = -1640531527; 
    private int[] keys;
    
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
            rightHalf += keys[(int) ((sum & 0x1933) >>> 11)] + sum ^ leftHalf + (leftHalf << 4 ^ leftHalf >>> 5); 
        }
        
        long encrypted = (long)leftHalf << 32 | rightHalf & 0xFFFFFFFFL;
        
//        System.out.println("Encrypted: ");
//        System.out.println(Long.toBinaryString(encrypted));
        
        return encrypted;
    }
        
    /* Takes 64-bit block and returns the XTEA decrypted bits */
    public long decrypt(long block) {
        // Split 64 bits into right and left halves and convert to integers
        int leftHalf = (int)(block >> 32);
        int rightHalf = (int)block;
        
        // Initialize sum and decrypt using XTEA algorithm
        long sum = -957401312; // Different for decryption
        for (int i = 0; i < rounds; i++) { 
            rightHalf -= keys[(int) ((sum & 0x1933) >>> 11)] + sum ^ leftHalf + (leftHalf << 4 ^ leftHalf >>> 5); 
            sum -= delta; 
            leftHalf -= (rightHalf << 4 ^ rightHalf >>> 5) + rightHalf ^ keys[(int) (sum & 0x3)] + sum; 
        }
        
        long decrypted = (long)leftHalf << 32 | rightHalf & 0xFFFFFFFFL;
        
//        System.out.println("Decrypted: ");
//        System.out.println(Long.toBinaryString(decrypted));
        
        return decrypted;
    }
        
}

