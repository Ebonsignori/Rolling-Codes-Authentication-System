package testing;

import rollingcodeauthentication.XTEA;

import java.util.Random;

/* A class with only a main method for testing the XTEA Class. */
    public class XTEATest {
    // Code for generating random 16, 32, and 64 bits.
    public static void main(String[] args) {
        Random rand = new Random(); // For all pseudo random operations

        // Generate random 128 bit key as array of 4 32 bit integers
        int[] key = new int[4];
        for (int i = 0; i < 4; i++) {
            key[i] = rand.nextInt(Integer.MAX_VALUE);
        }

        // Initialize xtea object with random key
        XTEA xtea = new XTEA(key);

        // Generate random 64 bits to act as Transmitter IV
        long block = (long) (rand.nextDouble() * Long.MAX_VALUE);
        System.out.println("Initial IV Value");
        System.out.println(Long.toBinaryString(block));

        // Encrypt 64 bits
        long encrypted = xtea.encrypt(block);
        System.out.println("\nEncrypted IV Value");
        System.out.println(Long.toBinaryString(encrypted));
        // Decrypt 64 bits
        long decrypted = xtea.decrypt(encrypted);
        System.out.println("\nDecrypted IV Value");
        System.out.println(Long.toBinaryString(decrypted));

        System.out.println("\nXTEA Works: ");
        System.out.println(decrypted == block);
    }

}

