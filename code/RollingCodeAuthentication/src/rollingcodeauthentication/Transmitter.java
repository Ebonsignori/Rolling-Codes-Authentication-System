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
public class Transmitter extends Reader {
    static int transmitter_number = 0;
    private BitSet transmitter_ID;
    private BitSet transmitter_IV;
                
    /* Initialize TX with speicifc ID and IV */
    public Transmitter() {
        if (transmitter_number > Reader.numOfTransmitters) {
            System.out.print("Exeeded number of Transmitters");
            System.exit(1);
        } else {
            this.transmitter_ID = (BitSet)Reader.ID_IVs[transmitter_number][0];
            this.transmitter_IV = (BitSet)Reader.ID_IVs[transmitter_number][1];
            transmitter_number++;
        }
    }
    
      public BitSet[] getRequestPacket() {
        BitSet[] packet = {this.transmitter_ID, Reader.readerID, XTEA.encrypt(this.transmitter_IV)};
        return packet;
     }
    
}
