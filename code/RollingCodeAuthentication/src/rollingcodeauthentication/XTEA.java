/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rollingcodeauthentication;

import java.util.BitSet;

/**
 *
 * @author Evan
 */
public class XTEA {
    
    public static BitSet encrypt(BitSet set, BitSet key) {
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
    
    public static BitSet decrypt(BitSet set, BitSet key) {
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