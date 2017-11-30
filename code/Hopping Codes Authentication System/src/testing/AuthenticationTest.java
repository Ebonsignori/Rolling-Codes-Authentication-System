package testing;

import rollingcodeauthentication.Packet;
import rollingcodeauthentication.Reader;
import rollingcodeauthentication.Transmitter;

public class AuthenticationTest {
    public static void main(String[] args) {
        try {
            // Create a reader and link it with a transmitter
            Reader reader = new Reader(1);
            Transmitter tx = new Transmitter(reader);

            // Turn both printing options on for troubleshooting
            boolean printingValues = true;
            boolean printingProgress = true;

            /* In software we fetch the request packet from TX though the
             * TX is the one that initializes the authentication process */
            Packet requestPacket = tx.getRequestPacket(printingProgress, printingValues);

            if (printingValues) {
                System.out.println("Request Packet: ");
                System.out.println(requestPacket.toString());
            }

            // Send request packet to reader to get response packet
            Packet responsePacket = reader.getResponsePacket(requestPacket, printingProgress, printingValues);

                    /* Send response packet to TX and update record if the IV is
                     * updated. If it isn't the response packet will be equal to
                     * the request packet */
            boolean isSuccessful = tx.updateRecord(responsePacket, printingProgress, printingValues);

            if (printingValues) {
                System.out.println("Response Packet: ");
                System.out.println(responsePacket.toString());
            }
            // Output result of authentication
            if (isSuccessful) {
                System.out.println("Authentication Completed Sucessfully");
            } else {
                System.out.println("Authentication Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Authentication Ended With Error.");
        }
    }
}
