/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rollingcodeauthentication;

import java.util.BitSet;
import java.util.List;

/**
 *
 * @author Evan
 */
class Reader {
    protected static int numOfTransmitters = 10;
    protected static BitSet readerID = randomBits(16);  // Initialize Reader ID
    protected static BitSet[] transmitterIDs;
    protected static BitSet[] IVs;
    protected static Object[][] ID_IVs = new Object[10][2];
    
    /* Initialize Values */
    public Reader() {
        // Ititialize IVs of 10 Transmitters
        this.IVs = new BitSet[numOfTransmitters];
        for (int i = 0; i < numOfTransmitters; i++){
            this.IVs[i] = randomBits(64);
        }
        // Ititialize IDs for 10 Transmitters
        this.transmitterIDs = new BitSet[numOfTransmitters];
        for (int i = 0; i < numOfTransmitters; i++){
            this.transmitterIDs[i] = randomBits(64);
        }
        // Put Transmitter ID and IV pairs in a 2D array
        for (int i = 0; i < numOfTransmitters; i++){
            this.ID_IVs[i][0] = this.transmitterIDs[i];
            this.ID_IVs[i][1] = this.IVs[i];
        }
    }
    
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
    
     /* Takes in request packet and returns response packet if verified */
     public BitSet[] getResponsePacket(BitSet[] requestPacket) {
        if (requestPacket[1] == this.readerID) {
            // Verify that transmitter_ID is in system, get corresponding IV
            BitSet transmitter_ID = requestPacket[0];
            BitSet actual_TX_IV = null;
            System.out.println(transmitter_ID);
            for (int i = 0; i < this.numOfTransmitters; i++) {
                System.out.println(this.ID_IVs[i][0]);
                if(this.ID_IVs[i][0] == transmitter_ID) {
                    actual_TX_IV = (BitSet)this.ID_IVs[i][1];
                } 
            }
            if (actual_TX_IV == null) {
                System.out.print("TX Not in Reader System");
            }
            // Extract unpredicted sequences and pattern match 256 decrypted 
            BitSet unpredictable_sequence = requestPacket[2];
            BitSet apparent_TX_IV = XTEA.decrypt(unpredictable_sequence);
            
            if (apparent_TX_IV == actual_TX_IV) {
                // Acutal Should be added with 256 here
                System.out.print("It's a match!");
                BitSet[] packet = {transmitter_ID, this.readerID, actual_TX_IV};
                return packet;
            } else {
                // Return Encoded and Incorrect IV to TX
                BitSet[] packet = {transmitter_ID, this.readerID, apparent_TX_IV};
                return packet;
            }
        } else {
            System.out.print("TX Sent Request to the Wrong Reader");
        }
        BitSet[] packet = null;
        return packet;
     }
      
}