package rollingcodeauthentication;

/* TX (Transmitter) has its ID, IV and the ID of the reader it is linked to */
public class Transmitter  {
    private short linkedReaderID;
    private int[] sharedKey;
    private long txID;
    private long txIV;
    private XTEA xtea;
    private Packet sentRequestPacket;
    private Reader linkedReader;
                
    /* Initialize TX with random ID and IV, unlinked to reader */
    public Transmitter() {
        this.txID = RandomBits.random64(); // 62 random Bits
        this.txIV = RandomBits.random64();
    }
    
    /* Initialize TX with random ID and IV, linked the Reader in argument */
    public Transmitter(Reader reader) {
        linkedReader = reader;
        this.txID = RandomBits.random64();
        this.txIV = RandomBits.random64();
        this.linkedReaderID = reader.linkTransmitter(this.txID, this.txIV);
        this.sharedKey = reader.getXTEAKey();   
        this.xtea = new XTEA(this.sharedKey);
    }
    
    /* Get this TX's ID */
    public long getID() {
       return this.txID;
    }
    
    public void setCurrentID(long newID) {
       this.txID = newID;
       linkedReader.updateID(this.txID, newID, this.txIV);
    }
    
    /* Get the reader ID that this TX is linked to */
    public short getReaderID() {
       return this.linkedReaderID;
    }
    
    public void setReaderID(short readerID) {
       this.linkedReaderID = readerID;
    }
    
    public long getCurrentIV() {
       return this.txIV;
    }
    
    public void setCurrentIV(long newIV) {
       this.txIV = newIV;
       linkedReader.updateIV(this.txID, this.txIV);
    }
    
    public String getSharedKeyString() {
        StringBuilder keyString = new StringBuilder(this.sharedKey.length);
        for (int i = 0; i < this.sharedKey.length; i++) {
            keyString.append(Integer.toString(this.sharedKey[i]));
        }
        return keyString.toString();
    }
    
    /* Sets an int to all four keys */
    public void setSharedKey(int key) {
        for (int i = 0; i < 4; i++) {
            this.sharedKey[i] = key;
        }
    }
    
    
    /* Generate request packet with messages if arg is true, without if false*/
    public Packet getRequestPacket(boolean sendMessages) {
       if (sendMessages) System.out.println("Fetching Request Packet from Transmitter");
       int ctr = 15;
       long[] Ivs = new long[ctr];
       // Encrypt IV+i for i=0-256 and psuedo-randomly select one
       if (sendMessages) System.out.println("Encrypting IVs+i for i = 0, 1, 2, .. 256 and selecting one to include in response packet");
       for (int i = 1; i < ctr; i++) {
           Ivs[i] = xtea.encrypt((long) this.txIV + i);
       }
       
       // Psuedo-randomly select an encrypted IV and XOR it with TX ID
       int randomIndex = 0 + (int)(Math.random() * ((ctr - 0) + 1));
       if (sendMessages)
            System.out.println("Selecting psuedo-random IV+i and XOR it with transmitter ID");
       long unpredictableSequence = Ivs[randomIndex] ^ this.txID;
       System.out.println(unpredictableSequence);
       if (sendMessages) System.out.println("Creating packet with unpredictable sequence");
       /* Request Packet contains: TX ID, Reader ID, and IV+i encrypted IV */
       this.sentRequestPacket = new Packet(this.txID,
                          this.linkedReaderID,
                          unpredictableSequence);
       if (sendMessages) System.out.println("Packet sent with Trasmitter ID, Reader ID, and Encrypted IV+i");
       return sentRequestPacket;
    }
    
    /* Update TX's IV if linked Reader sends verified response packet  */
    public boolean updateRecord(Packet responsePacket) {
       System.out.println("Response Packet recieved from reader");
       if (responsePacket.getBlock() != this.sentRequestPacket.getBlock()) {
           System.out.print("Updating Transmitter Record with next IV = "  );
           // Update IV to the old IV + 256, as stored in response packet
           this.txIV = xtea.decrypt(responsePacket.getBlock());
           System.out.println(this.txIV);
           return true;
       } 
       
        System.out.println("Not Updating Transmitter Record with next IV");
        return false;
    }
    
}
