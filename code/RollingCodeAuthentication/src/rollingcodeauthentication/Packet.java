package rollingcodeauthentication;

/**
 Packet holds a 64-bit transmitter ID, 16-bit reader ID, and a 64-bit block
 */
public class Packet {
    private long txID;
    private short readerID;
    private long block;
    
    Packet(long txID, short readerID, long block) {
        this.txID = txID;
        this.readerID = readerID;
        this.block = block;
    }
    
    public long getTxId() {
        return this.txID;
    }
    
    public short getReaderId() {
        return this.readerID;
    }
    
    public long getBlock() {
        return this.block;
    }
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(3);
        
        str.append("Tx ID: " + Long.toBinaryString(this.txID) + "\n");
        str.append("Reader ID: " + Integer.toBinaryString((int)this.readerID) + "\n");
        str.append("64-bit Block: " + Long.toBinaryString(this.block));
        
        return str.toString();
    }
}
