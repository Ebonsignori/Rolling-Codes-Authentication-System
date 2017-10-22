package rollingcodeauthentication; // Package for main application logic 

import java.util.Random;

/* For generating random bits as represented by primitives in Java */
public class RandomBits {
    private static final int INT_MAX = Integer.MAX_VALUE;
    private static final long LONG_MAX = Long.MAX_VALUE;
    
    public static Random rand = new Random(); // For all pseudo random operations
    
    /* Returns random 16 bits represented as short primitive */
    public static short random16() {
        return (short) rand.nextInt(65536);
    }
    
    /* Returns random 32 bits represented as int primitive */
    public static int random32() {
        return (int) rand.nextInt(INT_MAX);
    }
    
    /* Returns random 64 bits represented as long primitive */
    public static long random64() {
        return (long)(rand.nextDouble() * LONG_MAX);
    }
    
    /* Returns 'amount' sized array of random 32 bits represented as ints */
    public static int[] random32s(int amount) {
        int[] result = new int[amount];
        for (int i = 0; i < amount; i++) {
            result[i] = RandomBits.random32();
        }
        return result;
    }
}
