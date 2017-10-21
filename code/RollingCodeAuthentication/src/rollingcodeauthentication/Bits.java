package rollingcodeauthentication;

import java.math.BigInteger;
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
    
    public Bits(int[] bits) {
        this.bits = bits;
        this.length = bits.length;
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
            n = this.bits.length + (n);
        }
        System.arraycopy(this.bits, n, result, 0, this.bits.length-n);
        System.arraycopy(this.bits, 0, result, this.bits.length-n, n);
        
        this.bits = result;
    }
    
    /* Returns left shifted Bits by n */
    public Bits shiftedLeft(int n) {
        if (n < 0) {
            System.out.print("Can't shift by negatives.");
            return null;
        } else if (n >= this.length - 1) {
            n -= this.length;
        }
        
        int[] result = new int[this.length];
        System.arraycopy(this.bits, n, result, 0, this.length-n);
        System.arraycopy(this.bits, 0, result, this.length-n, n);
        
        return new Bits(result);
    }
    
    /* Returns right shifted Bits by n */
    public Bits shiftedRight(int n) {
        if (n < 0) {
            System.out.print("Can't shift by negatives.");
            return null;
        } else if (n > this.length ) {
            n %= this.length;
            int[] result = new int[this.length];
            System.arraycopy(this.bits, 0, result, n, this.length-n);
            System.arraycopy(this.bits, this.length-n, result, 0, n);

            return new Bits(result);
        } else {
            int[] result = new int[this.length];
            System.arraycopy(this.bits, 0, result, n, this.length-n);
            System.arraycopy(this.bits, this.length-n, result, 0, n);

            return new Bits(result);
        }
    }
    
    /* Adds this Bits object with passed Bits object */
    public void add(Bits bits2) {
        // Convert Bits objects to their string representations
        String bitStr1 = this.toString();
        String bitStr2 = bits2.toString();
        // Parse representations into integers and sum them
       // Parse representations into bigInts and sum them
        BigInteger intResult = new BigInteger(bitStr1, 2).add(new BigInteger(bitStr2, 2));
        // Parse result back into string and convert string into Bits
        String strResult = intResult.toString(2);
        this.stringToBits(strResult);
    } 
    
    public int[] addBin(Bits bitsIn) {
        int[] bits2 = bitsIn.getBits();
        int bits2Len = bits2.length;
        int [] sum = new int [Math.min(this.length, bits2Len)+1];
        
        for(int i =0; i < sum.length; i++) {
            sum[i]= this.bits[i] + bits2[i];
        }
        return sum;
    }
    
    /* Adds this Bits object with passed Bits object. Return new Bits Object*/
    public Bits added(Bits bits2) {
        // Convert Bits objects to their string representations
        String bitStr1 = this.toString();
        String bitStr2 = bits2.toString();
        // Parse representations into bigInts and sum them
        BigInteger intResult = new BigInteger(bitStr1, 2).add(new BigInteger(bitStr2, 2));
        // Parse result back into string and convert string into Bits
        String strResult = intResult.toString(2);
        return new Bits(strResult);
    }
    
    /* Subtracts this Bits object with passed Bits object */
    public void subtract(Bits bits2) {
        // Convert Bits objects to their string representations
        String bitStr1 = this.toString();
        String bitStr2 = bits2.toString();
        // Parse representations into bigInts and sum them
        BigInteger intResult = new BigInteger(bitStr1, 2).subtract(new BigInteger(bitStr2, 2)).abs();
        // Parse result back into string and convert string into Bits
        String strResult = intResult.toString(2);
        this.stringToBits(strResult);
    } 
    
    /* Subtracts this Bits object with passed Bits. Return new Bits Object*/
    public Bits subtracted(Bits bits2) {
        // Convert Bits objects to their string representations
        String bitStr1 = this.toString();
        String bitStr2 = bits2.toString();
        // Parse representations into bigInts and sum them
        BigInteger intResult = new BigInteger(bitStr1, 2).subtract(new BigInteger(bitStr2, 2));
        // Parse result back into string and convert string into Bits
        String strResult = intResult.toString(2);
        return new Bits(strResult);
    } 
    
    /* XORs this Bits object with passed Bits object */
    public void xor(Bits bits2) {
        int bits2Length = bits2.getLength();
        int max = Math.max(this.length, bits2Length);
        if (this.length < bits2Length) {
            this.pad(max);
        } else {
            bits2 = bits2.padded(max);
        }
        
        for (int i = 0; i < this.length; i++) {
            this.bits[i] = this.bits[i] ^ bits2.bitAt(i);
        }
    } 
    
    /* Xor this Bits object with passed Bits object. Return new Bits Object */
    public Bits xored(Bits bits2) {
        Bits result = null;
        if (this.length < bits2.getLength()) {
            result = new Bits(this.bits);
        } else {
            result = new Bits(bits2.getBits());
        }
        
        for (int i = 0; i < Math.min(this.length, bits2.length); i++) {
            result.set(i, this.bits[i] ^ bits2.bitAt(i));
        }
        return result;
    } 
    
    /* And this Bits object with passed Bits object */
    public void and(Bits bits2) {        
        for (int i = 0; i < this.length; i++) {
            this.bits[i] = this.bits[i] & bits2.bitAt(i);
        }
    } 
    
    /* And this Bits object with passed Bits object. Return new Bits Object */
    public Bits anded(Bits bits2) {
        Bits result = null;
        if (this.length < bits2.getLength()) {
            result = new Bits(this.bits);
        } else {
            result = new Bits(bits2.getBits());
        }
        
        for (int i = 0; i < Math.min(this.length, bits2.length); i++) {
            result.set(i, this.bits[i] & bits2.bitAt(i));
        }
        return result;
    } 
    
    /* Returns padded bits so that they are length 'n' */
    public Bits padded(int n) {
        if (this.length < n) {
            int[] result = new int[n];
            System.arraycopy(this.bits, 0, result, n-this.length, this.length);
            return new Bits(result);
        } else {
            System.out.println("Padding length less than bit length");
            return new Bits(this.bits);
        }
    }
    
    /* Pad's these bits (add 0 to leftmost) so that they are length 'n' */
    public void pad(int n) {
        if (this.length < n) {
            int[] result = new int[n];
            System.arraycopy(this.bits, 0, result, n-this.length, this.length);
            this.bits = result;
            this.length = n;
        } else {
            System.out.println("Padding length less than bit length");
        }
    }
    
    /* Returns left-most trimmed bits so that they are length 'n' */
    public Bits trimmed(int n) {
        if (this.length > n) {
            int[] result = new int[n];
            System.arraycopy(this.bits, n-this.length, result, 0, this.length - n);
            return new Bits(result);
        } else {
            System.out.println("Trimmed length bigger than bit length");
            return new Bits(this.bits);
        }
    }
    
//    /* Trims these left-most bits to length 'n' */
//    public void trim(int n) {
//        if (this.length > n) {
//            int[] result = new int[n];
//            System.arraycopy(this.bits, this.length - n, result, 0, n);
//            this.bits = result;
//            this.length = n;
//        } else {
//            System.out.println("Trimmed length bigger than bit length");
//        }
//    }
    
    /* Trims these right-most bits to length 'n' */
    public void trim(int n) {
        if (this.length > n) {
            int[] result = new int[n];
            System.arraycopy(this.bits, 0, result, 0, n);
            this.bits = result;
            this.length = n;
        } else {
            System.out.println("Trimmed length bigger than bit length");
        }
    }
    
    /* Sets the bit at index to val */
    public void set(int index, int val) {
        this.bits[index] = val;
    }
    
    /* Swaps bit index 1 with bit index 2 */
    public void swap(int i1, int i2) {
        int temp = this.bits[i1];
        this.bits[i1] = this.bits[i2];
        this.bits[i2] = temp;
    }
      
    /* Returns bit at index 'x' */
    public int bitAt(int x) {
        return this.bits[x];
    }
        
    /* Return bit array */
    public int[] getBits() {
        return this.bits;
    }
    
    /* Return new Bits object which is a subset of these Bits from x1 to x2 */
    public Bits subset(int x1, int x2) {
        if (x2 > this.length || x1 < 0) {
            System.out.print("Subset indexes must be in range");
            return null;
        }
        int[] result = new int[x2 - x1];
        System.arraycopy(this.bits, x1, result, 0, x2 - x1);
        return new Bits(result);
    }
    
     /* Returns bit at index 'x' */
    public int getLength() {
        return this.length;
    }
    
    /* Returns integer of bits */
    public int toInteger() {
        return Integer.parseInt(this.toString(), 2);
    }
    
    /* Returns byte array of bits */
    public byte[] toByteArray() {
        return (new BigInteger(this.bits.toString(), 2)).toByteArray();
    }
    
    /* Returns long (64-bit) representation of Bits */
    public long toLong() {
        return Long.parseLong(this.bits.toString(), 2);
    }
    
    /* Returns hex string of bits */
    public String toHexString() {
        return (new BigInteger(this.toString(), 2)).toString(16);
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
    
//    public static void main(String[] args) {
//        Bits b1 = new Bits(25);
//        System.out.println(b1.toArrString());
//        System.out.println(b1.subset(0, 5).toArrString());
//    }
    
}
