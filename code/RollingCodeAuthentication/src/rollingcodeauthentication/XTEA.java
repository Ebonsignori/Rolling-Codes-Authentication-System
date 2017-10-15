package rollingcodeauthentication;

/**
 *
 * @author Evan
 */
public class XTEA {
    
    public static Bits encrypt(Bits set, Bits key) {
//        // Split Bits into two 32 bit halves
//        BitSet LHS = block.get(0, 31);
//        BitSet RHS = block.get(31, 62);
//               
//        // Combine Halves
//        byte[] lhBytes = LHS.toByteArray();
//        byte[] rhBytes = RHS.toByteArray();
//        byte[] combined = new byte[lhBytes.length + rhBytes.length];
//
//        for (int i = 0; i < combined.length; ++i)
//        {
//            combined[i] = i < lhBytes.length ? lhBytes[i] : rhBytes[i - lhBytes.length];
//        }
//        
//        return BitSet.valueOf(combined);
          return set;
    }
    
    public static Bits decrypt(Bits set, Bits key) {
//        // Split Bits into two 32 bit halves
//        BitSet LHS = block.get(0, 31);
//        BitSet RHS = block.get(31, 62);
//               
//        // Combine Halves
//        byte[] lhBytes = LHS.toByteArray();
//        byte[] rhBytes = RHS.toByteArray();
//        byte[] combined = new byte[lhBytes.length + rhBytes.length];
//
//        for (int i = 0; i < combined.length; ++i)
//        {
//            combined[i] = i < lhBytes.length ? lhBytes[i] : rhBytes[i - lhBytes.length];
//        }
//        
//        return BitSet.valueOf(combined);
          return set;
    }
}