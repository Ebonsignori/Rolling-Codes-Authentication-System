/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rollingcodeauthentication;

import java.util.Random;

/**
For generating random bits as represented by primitives in Java
 */
public class RandomBits {
    private static final short SHORT_MAX = Short.MAX_VALUE;
    private static final int INT_MAX = Integer.MAX_VALUE;
    private static final long LONG_MAX = Long.MAX_VALUE;
    private static final short SHORT_MIN = Short.MIN_VALUE;
    private static final int INT_MIN = Integer.MIN_VALUE;
    private static final long LONG_MIN = Long.MIN_VALUE;
    
    public static Random rand = new Random(); // For all pseudo random operations
    
    /* Returns random 16 bits represented as a short */
    public static short random16() {
        return (short) rand.nextInt(65536);
    }
    
    public static int random32() {
        return (int) rand.nextInt(INT_MAX);
    }
    
    public static long random64() {
        return (long)(rand.nextDouble() * Long.MAX_VALUE);
    }
    
    public static int[] random32s(int amount) {
        int[] result = new int[amount];
        for (int i = 0; i < amount; i++) {
            result[i] = RandomBits.random32();
        }
        return result;
    }
}
