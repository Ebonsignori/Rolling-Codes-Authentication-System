package rollingcodeauthentication;

import java.util.Arrays;

/* Class for representing a series of bits */
public class Bits {
    int[] bits;
    int length;
       
    /* Initalize a random bit array of size n */
    public Bits(int n, boolean isRandom) {
        this.length = n;
        this.bits = new int[this.length];
        if (isRandom) {
            this.randomize();
        }
    }
    
    /* Initalize a bit array from string */
    public Bits(String bitStr) {
        stringToBits(bitStr);
    }
    
    /* Initalize a bit array from an integer */
    public Bits(int bitInt) {
        this.stringToBits(Integer.toBinaryString(bitInt));  
    }
    
    /* Randomizes each bit in bits */
    public void randomize() {
        for (int i = 0; i < this.length; i++) {
           bits[i] = (int)Math.round(Math.random());
        }
    }
    
    /* Takes binary string input and represents it as Bits object */
    public void stringToBits(String bitStr) {
        this.length = bitStr.length();
        this.bits = new int[this.length];
        for (int i = 0; i < this.length; i++) {
            this.bits[i] = Character.getNumericValue(bitStr.charAt(i));
        }
    }
    
    /* Pass a positive or negative n for respective right or left bit shift */
    public void shift(int n) {
        int[] result = new int[this.length];
        if (n < 0) {
            n = this.bits.length + (n-1);
        }
        System.arraycopy(this.bits, n, result, 0, this.bits.length-n);
        System.arraycopy(this.bits, 0, result, this.bits.length-n, n);
        
        this.bits = result;
    }
    
    /* Adds this Bits object with passed Bits object */
    public void add(Bits bits2) {
        // Convert Bits objects to their string representations
        String bitStr1 = this.toString();
        String bitStr2 = bits2.toString();
        // Parse representations into integers and sum them
        int intResult = Integer.parseInt(bitStr1, 2) +
                        Integer.parseInt(bitStr2, 2);
        // Parse result back into string and convert string into Bits
        String strResult = Integer.toBinaryString(intResult);
        this.stringToBits(strResult);
    } 
    
    /* XORs this Bits object with passed Bits object */
    public void xor(Bits bits2) {
        if (this.length != bits2.getLength()) {
            System.out.print("Lengths must be the same for XOR operation");
            return;
        }
        
        for (int i = 0; i < this.length; i++) {
            this.bits[i] = this.bits[i] ^ bits2.bitAt(i);
        }
    } 
      
    /* Returns bit at index 'x' */
    public int bitAt(int x) {
        return this.bits[x];
    }
        
    /* Return bit array */
    public int[] getBits() {
        return this.bits;
    }
    
     /* Returns bit at index 'x' */
    public int getLength() {
        return this.length;
    }
    
    /* Returns integer of bits */
    public int toInteger() {
        return Integer.parseInt(this.toString());
    }
    
    /* Returns string in array form of bits */
    public String toArrString() {
        return Arrays.toString(this.bits);
    }
    
    /* Returns binary string of Bits. i.e. [1,0,1,0] becomes "1010" */
    @Override
    public String toString() {
        StringBuilder binString = new StringBuilder(this.length);
        for (int i = 0; i < this.length; i++) {
            binString.append(bits[i]);
        }
        return binString.toString();
    }
    
}
