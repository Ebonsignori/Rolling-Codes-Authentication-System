package rollingcodeauthentication;

import java.util.ArrayList;

/* Reader holds the shared ID and IVs of n linked TXs (Transmitters) */
class Reader {
    private Bits readerID;
    private int numOfLinkedTxs = 0;
    private ArrayList<Bits[]> txIdsAndIvs;
    private Bits sharedXTEAKey = new Bits(128);
 
    /* Initialize Reader with just ID */
    public Reader() {
        this.readerID = new Bits(16);  
    }
    
    /* Initialize Reader ID and known number of Transmitters to be linked */
    public Reader(int n) {
        this.readerID = new Bits(16);
        this.txIdsAndIvs = new ArrayList<Bits[]>(n); 
    }

    /* Link new TX if it isn't already linked and return this Reader's ID */
    public Bits linkTransmitter(Bits txID, Bits txIV) {
        Bits[] idAndIv = new Bits[]{txID, txIV}; 
        if (!txIdsAndIvs.contains((Bits[])idAndIv)) {
            txIdsAndIvs.add(idAndIv);
            numOfLinkedTxs++;
        } else {
            System.out.println("Reader already TX with this ID");
        }
        return this.readerID;
    }
    
    /* Takes in request packet and returns response packet if verified */
    public Bits[] getResponsePacket(Bits[] requestPacket) {
        System.out.println("Request Packet Recieved by Reader");
        // Verify TX sent request to this reader
        if (requestPacket[1] == this.readerID) {
             // Verify TX's ID is linked to this reader (in the TX ID list)
             System.out.println("Trasmitter ID is in Reader's System");
             int[] txIdIvIndexes = this.indexPairOf((Bits)requestPacket[0]);
             if (txIdIvIndexes[0] != -1) {
                Bits actualTxIv = this.txIdsAndIvs.get(txIdIvIndexes[0])[1]; 
                System.out.println("Decrypting IV from request packet");
                Bits apparentTxIv = XTEA.decrypt(requestPacket[2],
                                                   this.sharedXTEAKey);
                // Return Response Packet with TX's IV + 256
                if (apparentTxIv == actualTxIv) {
                    System.out.println("Authentication Verified: "
                                   + "decrypted IV matches IV on Reader record!");
                    //BitSet updatedIV = MyBitSet.addInt(actualTxIV, 256);
                    Bits updatedIV = actualTxIv;
                    this.updateRecord(requestPacket[0], updatedIV);
                    System.out.println("Sending Response Packet to Transmitter");
                    return new Bits[]{requestPacket[0], 
                                        requestPacket[1], 
                                        XTEA.encrypt(updatedIV, 
                                                     this.sharedXTEAKey)};
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
    public int[] indexPairOf(Bits set) {
        for (int i = 0; i < this.numOfLinkedTxs; i++) {
            Bits[] thisSet = this.txIdsAndIvs.get(i);
            if (thisSet[0] == set) { 
                return new int[]{i, 0};
            } else if (thisSet[1] == set) {
                return new int[]{i, 1};
            }
        }
        return new int[]{-1, -1};
    }
    
    /* Update TX in record identified by argument 1's ID with argument 2's IV */
    public void updateRecord(Bits txID, Bits updatedIV) {
       // Replace old IV with new IV
       System.out.println("Updating Reader's IV record with next IV");
       this.txIdsAndIvs.set(this.indexPairOf(txID)[0], new Bits[]{txID, updatedIV}); 
    }
        
    /* Get this Reader's ID */
    public Bits getReaderID() {
        return this.readerID;
    }
    
    /* Get number of TX's linked to this Reader */
    public int getNumOfTx() {
        return this.numOfLinkedTxs;
    }
    
    /* Get this Reader's ID */
    public Bits getXTEAKey() {
        return this.sharedXTEAKey;
    }    
    
}