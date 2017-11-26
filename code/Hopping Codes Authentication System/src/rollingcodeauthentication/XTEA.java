package rollingcodeauthentication; // Package for main application logic

/*
 * XTEA Encryption and Decryption algorithms. Includes TEA implementations
 * as well for potential option of using TEA instead of XTEA
 */
public class XTEA {
    private int rounds = 64;
    private long[] delta = new long[rounds];
    private int[] keys;

    /* Initalize XTEA Object with key and set initial value of delta */
    public XTEA(int[] key) {
        this.keys = key;
        delta[0] = Long.parseLong("2654435769");
    }

    /* Takes 64-bit block and returns the XTEA encrypted bits */
    public long encrypt(long block) {
        int[] leftHalf = new int[64];
        int[] rightHalf = new int[64];

        // Split 64 bits into right and left halves and convert to integers
        leftHalf[0] = (int)(block >> 32);
        rightHalf[0] = (int)block;

        // Encrypt using XTEA algorithm
        int r = 0;
        for (int i = 0; i < rounds - 1; i++) {
            r = i + 1;
            delta[i] = ((i+1) / 2 ) * delta[0];
            // Left half[i] + round_function(right[i], Key[2i-1
            leftHalf[i+1] = leftHalf[i] + ((rightHalf[i] << 4 ^ rightHalf[i] >>> 5) + rightHalf[i] ^ (int) delta[i] + keys[keyIndex(r)]);
            rightHalf[i+1] = rightHalf[i] + ((leftHalf[i+1] << 4 ^ leftHalf[i+1] >>> 5) + leftHalf[i+1] ^ (int) delta[i+1] + keys[keyIndex(r)]);
        }

        long encrypted = (long)leftHalf[rounds-1] << 32 | rightHalf[rounds-1] & 0xFFFFFFFFL;

        return encrypted;
    }

    /* Takes 64-bit block and returns the XTEA decrypted bits */
    public long decrypt(long block) {
        int[] leftHalf = new int[64];
        int[] rightHalf = new int[64];

        // Split 64 bits into right and left halves and convert to integers
        leftHalf[0] = (int)(block >> 32);
        rightHalf[0] = (int)block;

        // Decrypt using XTEA algorithm
        int r = 0;
        for (int i = 0; i < rounds - 1; i++) {
            r = i + 1;
            delta[i] = ((i+1) / 2) * delta[0];
            rightHalf[i+1] = rightHalf[i] - ((leftHalf[i] << 4 ^ leftHalf[i] >>> 5) + leftHalf[i] ^ (int) delta[i] + keys[keyIndex(r)]);
            leftHalf[i+1] = leftHalf[i] - ((rightHalf[i+1] << 4 ^ rightHalf[i+1] >>> 5) + rightHalf[i+1] ^ (int) delta[i+1] + keys[keyIndex(r)]);
        }

        long decrypted = (long)leftHalf[rounds-1] << 32 | rightHalf[rounds-1] & 0xFFFFFFFFL;

        return decrypted;
    }

    public int keyIndex(int round) {
        if (round % 2 != 0) {
            return (int) delta[(round-1)/2] & 0x3;
        } else {
            return (int) delta[round/2 >>> 11] & 0x3;
        }
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