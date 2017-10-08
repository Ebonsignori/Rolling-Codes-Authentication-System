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
public class MyBitSet extends BitSet{
    
    /* Returns a BitSet of n random bits */
    public static BitSet randomBits(int n) {
        BitSet randomIV = new BitSet(n);
        for(int i = 0; i < n; i++) {
           boolean randomBit = true;
           if ((int)Math.round(Math.random()) == 1) {
               randomBit = true;
           } else {
               randomBit = false;
           }
           randomIV.set(i, randomBit);
        }
        return randomIV;
    }
    
    /* Returns BitSet that is sum of the passed BitSet and an integer */
    public static BitSet addInt(BitSet bitset, int x) {
        return null;
    }
    
    /* Returns BitSet that is sum of the passed BitSet and an integer */
    public static BitSet addSets(BitSet set1, BitSet set2) {
        return null;
    }
    
    /* Returns BitSet that is shifted left by n */
    public static BitSet shiftLeft(BitSet set, int n) {
        return null;
    }
    
    /* Returns BitSet that is shifted right by n */
    public static BitSet shiftRight(BitSet set, int n) {
        return null;
    }
}
