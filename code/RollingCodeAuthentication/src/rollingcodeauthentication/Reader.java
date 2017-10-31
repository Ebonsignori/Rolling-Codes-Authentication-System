package rollingcodeauthentication; // Package for main application logic 

import java.util.ArrayList;

/* Reader holds the shared ID and IVs of n linked TXs (Transmitters) */
public class Reader {
    private int[] key = RandomBits.random32s(4);
    private short readerID;
    private int numOfLinkedTxs = 0;
    private ArrayList<long[]> txIdsAndIvs;
    private XTEA xtea = new XTEA(this.key);
 
    /* Initialize Reader with just ID */
    public Reader() {
        this.readerID = RandomBits.random16();  
    }
    
    /* Initialize Reader ID and known number of Transmitters to be linked */
    public Reader(int n) {
        this.readerID = RandomBits.random16();
        this.txIdsAndIvs = new ArrayList<long[]>(n); 
    }

    /* Link new TX if it isn't already linked and return this Reader's ID */
    public short linkTransmitter(long txID, long txIV) {
        long[] idAndIv = new long[]{txID, txIV}; 
        if (!txIdsAndIvs.contains((long[])idAndIv)) {
            txIdsAndIvs.add(idAndIv);
            numOfLinkedTxs++;
        } else {
            System.out.println("Reader already TX with this ID");
        }
        return this.readerID;
    }
    
    /* Takes in request packet and returns response packet if verified */
    public Packet getResponsePacket(Packet requestPacket, boolean printingProgress, boolean printingValues) {
        if (printingProgress) System.out.println("Request Packet Recieved by Reader");
        // Verify TX sent request to this reader
        if (requestPacket.getReaderId() == this.readerID) {
             // Verify TX's ID is linked to this reader (in the TX ID list)
             if (printingProgress) System.out.println("Trasmitter ID is in Reader's System");
             long txId = requestPacket.getTxId();
             int[] txIdIvIndexes = this.indexPairOf(txId);
             
             // If TxID is in system
             if (txIdIvIndexes[0] != -1) {
                long actualTxIv = this.txIdsAndIvs.get(txIdIvIndexes[0])[1]; 
                long apparentTxIv;
                long unpredictableSequence = requestPacket.getBlock();
                boolean isValidIV = false;
                
                // Run through 256 possibilites and find match by XOR with known IV and possible IV's
                if (printingProgress) System.out.println("Pattern matching unpredictable sequence with txIV on record");
                for (int i = 0; i < 256; i++) {
                    apparentTxIv = xtea.encrypt((long) actualTxIv + i);
                    isValidIV = (apparentTxIv ^ txId) == unpredictableSequence;
                    if (isValidIV) {
                        break;
                    }
                }

                // Return Response Packet with TX's IV + 256
                if (isValidIV) {
                    if (printingProgress) System.out.println("Authentication Verified: "
                                   + "decrypted IV matches IV on Reader record!");
                    long updatedIV = actualTxIv + 256;
                    this.updateRecord(requestPacket.getTxId(), updatedIV, printingProgress, printingValues);
                    if (printingProgress) System.out.println("Sending Response Packet to Transmitter");
                    return new Packet(requestPacket.getTxId(), 
                                      requestPacket.getReaderId(), 
                                      xtea.encrypt(updatedIV));
                // If no match return the request packet as the response packet
                } else {
                    System.out.println("Authentication Failed: IVs don't match.");
                    return requestPacket;
                }
            } else {
                System.out.println("Authentication Failed: TX ID is not linked to this reader");
            }
        } else {
           System.out.println("Authentication Failed: TX ID is not in this Reader's System");
        }
        return requestPacket;
    }
    
    /* Returns the 2d index of the set in the TX ID and IV list */
    public int[] indexPairOf(long set) {
        for (int i = 0; i < this.numOfLinkedTxs; i++) {
            long[] thisSet = this.txIdsAndIvs.get(i);
            if (thisSet[0] == set) { 
                return new int[]{i, 0};
            } else if (thisSet[1] == set) {
                return new int[]{i, 1};
            }
        }
        return new int[]{-1, -1};
    }
    
    /* Update TX in record identified by argument 1's ID with argument 2's IV */
    public void updateRecord(long txID, long updatedIV, boolean printingProgress, boolean PrintingValues) {
       // Replace old IV with new IV
       if (printingProgress) System.out.println("Updating Reader's IV record with next IV");
       if (PrintingValues) System.out.println("Next IV = IV + 256 = " + Long.toBinaryString(updatedIV));
       this.txIdsAndIvs.set(this.indexPairOf(txID)[0], new long[]{txID, updatedIV}); 
    }
    
    /* Update IV corresponding to txID */
    public void updateIV(long ID, long IV) {
        this.txIdsAndIvs.set(this.indexPairOf(ID)[0], new long[]{ID, IV});
    }
    
    /* Update IV corresponding to txID */
    public void updateID(long oldID, long newID, long IV) {
        this.txIdsAndIvs.set(this.indexPairOf(oldID)[0], new long[]{newID, IV});
    }
     
    /* Get this Reader's ID */
    public short getReaderID() {
        return this.readerID;
    }
    
    /* Set this Reader's ID */
    public void setReaderID(short newID) {
        this.readerID = newID;
    }
    
    /* Get number of TX's linked to this Reader */
    public int getNumOfTx() {
        return this.numOfLinkedTxs;
    }
    
    /* Get this Reader's ID */
    public int[] getXTEAKey() {
        return this.key;
    }
    
    /* This key concatenated into a string */
    public String getSharedKeyString() {
        StringBuilder keyString = new StringBuilder(this.key.length);
        for (int i = 0; i < this.key.length; i++) {
            keyString.append(Integer.toString(this.key[i]));
        }
        return keyString.toString();
    }
    
}