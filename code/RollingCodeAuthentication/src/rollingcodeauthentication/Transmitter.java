package rollingcodeauthentication;

import java.util.BitSet;

/* TX (Transmitter) has its ID, IV and the ID of the reader it is linked to */
public class Transmitter extends Reader {
    private BitSet linkedReaderID = null;
    private BitSet sharedXTEAKey = null;
    private BitSet txID = null;
    private BitSet txIV = null;
                
    /* Initialize TX with random ID and IV, unlinked to reader */
    public Transmitter() {
        this.txID = MyBitSet.randomBits(62);
        this.txIV = MyBitSet.randomBits(62);
    }
    
    /* Initialize TX with random ID and IV, linked the Reader in argument */
    public Transmitter(Reader reader) {
        this.txID = MyBitSet.randomBits(62);
        this.txIV = MyBitSet.randomBits(62);
        this.linkedReaderID = reader.linkTransmitter(this.txID, this.txIV);
        this.sharedXTEAKey = reader.getXTEAKey();     
    }
    
    /* Get this TX's ID */
    public BitSet getID() {
       return this.txID;
    }
    
    /* Get the reader ID that this TX is linked to */
    public BitSet getReaderID() {
       return this.linkedReaderID;
    }
    
    public void setReaderID(BitSet readerID) {
       this.linkedReaderID = readerID;
    }
    
    /* Generate request packet with messages if arg is true, without if false*/
    public BitSet[] getRequestPacket(boolean sendMessages) {
       if (sendMessages) {
            System.out.println("Fetching Request Packet from Transmitter");
            System.out.println("Encrypting Transmitter IV");
       }
       /* Request Packet contains: TX ID, Reader ID, and encrypted TX IV */
       BitSet[] packet = {this.txID,
                          this.linkedReaderID,
                          XTEA.encrypt(this.txIV, this.sharedXTEAKey)};
       System.out.println("Packet sent with Trasmitter ID, Reader ID, and encrypted IV");
       return packet;
    }
    
    /* Update TX's IV if linked Reader sends verified response packet  */
    public void updateRecord(BitSet[] responsePacket) {
       System.out.println("Response Packet recieved. \n"
               + "Updating Transmitter Record with next IV");
       if (responsePacket != this.getRequestPacket(false)) {
           // Update IV to the old IV + 256, as stored in response packet
           this.txIV = XTEA.decrypt(responsePacket[2], this.sharedXTEAKey);
       } 
       return;
    }
    
}
