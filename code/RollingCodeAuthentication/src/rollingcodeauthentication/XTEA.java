package rollingcodeauthentication;

import java.math.BigInteger;

/* XTEA Encryption and Decryption algorithms */
public class XTEA {
    
    /* Takes 64-bits and corresponding key then returns the encrypted Bits */
    public static Bits encrypt(Bits bits, Bits key) {
        // Create 64-bit constant, delta
        final Bits DELTA = new Bits("10011110001101110111100110111001");
   
        // Split key into four 32-bit blocks 
        Bits[] keys = new Bits[4];
        for (int i = 0; i < 4; i++) 
            keys[i] = key.subset(32 * i, 32 * (i+1));
        
        // Split 64 bits into right and left halves
        Bits bitsLH = bits.subset(0, 32); 
        Bits bitsRH = bits.subset(32, 64);
        
        // Initalize sum and round key index to 0
        Bits sum = new Bits("00000000000000000000000000000000");
        int rKey = 0;
        
        // XTEA encrytion
        for (int r = 1; r <= 64; r++) { 
            
            if (r % 2 != 0) {
                rKey = DELTA.bitAt((r-1)/2) & 3;
            } else {
                rKey = DELTA.bitAt((r-2)>>11) & 3;
            }
            bitsLH.add(((bitsRH.shiftedLeft(4)).xored(bitsRH.shiftedRight(5))).added(bitsRH).xored(sum.added(keys[rKey]))); 
            sum.add(DELTA);
            bitsRH.add(((bitsLH.shiftedLeft(4)).xored(bitsLH.shiftedRight(5))).added(bitsLH).xored(sum.added(keys[rKey]))); 
        }
        
        // Trim left and right halves and comine them into ciphertext block 
        bitsLH.trim(32);
        bitsRH.trim(32);
        Bits encrypted = new Bits(bitsLH.toString() + bitsRH.toString());
        
        System.out.println("LeftHalf: ");
        System.out.println(bitsLH);
        System.out.println("RightHalf: ");
        System.out.println(bitsRH);
        System.out.println("Encrypted: ");
        System.out.println(encrypted);
        
        return encrypted;
    }
    
    /* Takes 64-bits and corresponding key and returns the decrypted Bits */
    public static Bits decrypt(Bits bits, Bits key) {
        // Create 64-bit constant, delta
        final Bits DELTA = new Bits("10011110001101110111100110111001");
   
        // Split key into four 32-bit blocks 
        Bits[] keys = new Bits[4];
        for (int i = 0; i < 4; i++) 
            keys[i] = key.subset(32 * i, 32 * (i+1));
        
        // Split 64 bits into right and left halves
        Bits bitsLH = bits.subset(0, 32); 
        Bits bitsRH = bits.subset(32, 64);
        
        // Initalize sum and round key index to 0
        Bits sum = new Bits("00000000000000000000000000000000");
        int rKey = 0;
        
        // XTEA decryption
        for (int r = 1; r <= 64; r++) { 
            
            if (r % 2 != 0) {
                rKey = DELTA.bitAt((r-1)/2) & 3;
            } else {
                rKey = DELTA.bitAt((r-2)>>11) & 3;
            }
            bitsRH.subtract(((bitsLH.shiftedLeft(4)).xored(bitsLH.shiftedRight(5))).added(bitsLH).xored(sum.added(keys[rKey])));
            sum.subtract(DELTA);
            bitsLH.subtract(((bitsRH.shiftedLeft(4)).xored(bitsRH.shiftedRight(5))).added(bitsRH).xored(sum.added(keys[rKey]))); 
        }
        
        // Add left and right sides together
        Bits decrypted = bitsLH.added(bitsRH).padded(64);
        
        System.out.println("LeftHalf: ");
        System.out.println(bitsLH);
        System.out.println("RightHalf: ");
        System.out.println(bitsRH);
        System.out.println("Decrypted: ");
        System.out.println(decrypted);
        
        return decrypted;
    }
        
}

