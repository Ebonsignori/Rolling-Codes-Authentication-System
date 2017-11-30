package GUI; // Package for .fxml view and its controller

import GUI.validatetextfields.NumberOfTXTextField;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import rollingcodeauthentication.Packet;
import rollingcodeauthentication.Reader;
import rollingcodeauthentication.Transmitter;

/* Controller class for FXMLDocument.fxml */
public class FXMLDocumentController implements Initializable {
    // Initalize components and variables
    public ArrayList<Reader> RDs = new ArrayList<Reader>(); // All RDs
    public ArrayList<Transmitter> TXs = new ArrayList<Transmitter>(); // All TXs
    private int numOfReaders = 0;
    private int numOfTransmitters = 0;
    private boolean txIsSelected = false; 
    private boolean rdIsSelected = false;
    
    @FXML
    public ListView<String> txList; // Lists all TXs
    public ListView<String> rdList; // Lists all RDs
    public NumberOfTXTextField numOfTXText; // Number of TX to be added with RD
    public Button addReaderBtn; 
    
    @FXML
    public TextArea logConsole; // TextArea that acts as Console
    public Button authenticateBtn; // Begins authentication process
    public CheckBox printProgress; // If selected, progress is printed to Console
    public CheckBox printValues; // If selected, values are printed to Console
    
    @FXML
    public Label txIDLabel;
    public Label currentIVLabel;
    
    // Labels for displaying current TX and RD values
    @FXML
    public Label currentValuesHeader;
    public Label currentTxID;
    public Label currentRdID;
    public Label currentKey;
    public Label currentIV;
    
    // Fields for updating TX values. Have custom extended class validations 
    @FXML
    public TextField readerIDField;
    public TextField transmitterIDField;
    public TextField keyField;
    public TextField ivField;
    
    // Arrows are green upon success, red upon fail, black by default
    @FXML
    public Group leftArrow; 
    public Group rightArrow;
    
    /* 
    * Adds a a maximum of 5 RDs and 1-10 linked TXs. Each TX and its linked RD 
    * are color coded to match each other.
    */
    @FXML
    public void addReader() {
        // Maxiumum of 5 RDs
        if (numOfReaders < 5) {
            // Get number of TX to be linked to RD
            int numOfTX = Integer.parseInt(this.numOfTXText.getText());
            Reader reader = new Reader(numOfTX);
            RDs.add(reader);
            numOfReaders++;

            String[] txNames = new String[numOfTX];

            // Populate reader with transmitters and generate names for ListView
            for (int i = 0; i < numOfTX; i++) {
                numOfTransmitters++;
                TXs.add(new Transmitter(reader));
                txNames[i] = "TX[" + Integer.toString(numOfReaders) + "] #" + Integer.toString(numOfTransmitters);
            }
            
            // Add TX names and linked RD to their corresponding list views
            ObservableList<String> txItems = FXCollections.observableArrayList(txNames);
            txList.getItems().addAll(txItems);
            rdList.getItems().add("Reader #" + Integer.toString(this.numOfReaders));

            // Color codes RD and TXs' by cycling through colorArr
            String[] colorArr = new String[]{"derive(blue, 50%)", "derive(red, 50%)", "yellow", "derive(palegreen, 50%)", "derive(orange, 50%)"};
            txList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                                setStyle("-fx-control-inner-background: " + "derive(white, 50%)" + ";");
                            } else {
                                // Reassign colors upon each add
                                for (int i = 1; i <= numOfReaders; i++) {
                                    // Get TX number by 4th character
                                    if (item.charAt(3) == (char)(i + '0')) {
                                        setText(item);
                                        setStyle("-fx-control-inner-background: " + colorArr[i-1] + ";");
                                    }
                                }
                            }
                        }
                    };
                }
            });

            rdList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                                setStyle("-fx-control-inner-background: " + "derive(white, 50%)" + ";");
                            } else {
                                for (int i = 1; i <= numOfReaders; i++) {
                                    // Get RD number by 9th character
                                     if (item.charAt(8) == (char)(i + '0')) {
                                         setText(item);
                                         setStyle("-fx-control-inner-background: " + colorArr[i-1] + ";");
                                     }
                                }
                            }
                        }
                    };
                }
            });
        } else {
            logConsole.setText("Reader limit exeeded");
        }
    }
    
    /* When a transmitter is selected in the ListView, display its values */
    @FXML
    public void selectTX() {
        if (!txList.getItems().isEmpty()) {
            int index = txList.getFocusModel().getFocusedIndex();
            Transmitter tx = TXs.get(index);
            txIsSelected = true;

            // Display current tx values
            currentValuesHeader.setText("Current Values of Selected TX");
            currentRdID.setText(Short.toString(tx.getReaderID()));
            txIDLabel.setText("Transmitter ID:");
            currentTxID.setText(Long.toString(tx.getID()));
            currentKey.setText(tx.getSharedKeyString()); 
            currentIVLabel.setText("Current IV:");
            currentIV.setText(Long.toString(tx.getCurrentIV()));

            // Allow for tx update fields to be accessed 
            readerIDField.setDisable(false);
            transmitterIDField.setDisable(false);
            keyField.setDisable(false);
            ivField.setDisable(false);         

            // If both RD and TX have been selected, enable authentication button
            if (txIsSelected && rdIsSelected) {
                authenticateBtn.setDisable(false);  
            }
        }
    }
    
    /* When a reader is selected in the ListView, display its values */
    @FXML
    public void selectRD() {
        if (!rdList.getItems().isEmpty()) {
            int index = rdList.getFocusModel().getFocusedIndex();
            Reader rd = RDs.get(index);
            rdIsSelected = true;

            // Display current rd values
            currentValuesHeader.setText("Current Values of Selected RD");
            currentRdID.setText(Short.toString(rd.getReaderID()));
            txIDLabel.setText("Linked Transmitters:");
            currentTxID.setText(Integer.toString(rd.getNumOfTx()));
            currentKey.setText(rd.getSharedKeyString()); 
            currentIVLabel.setText(" ");
            currentIV.setText(" ");  

            // Disable tx update fields to be accessed
            readerIDField.setDisable(true);
            transmitterIDField.setDisable(true);
            keyField.setDisable(true);
            ivField.setDisable(true);

            // If both RD and TX have been selected, enable authentication button
            if (txIsSelected && rdIsSelected) {
                authenticateBtn.setDisable(false);  
            }
        }
    }
    
    // Update reader ID in transmitter. Will unlink TX from RD
    public void updateReaderID(Transmitter tx) {       
        tx.setReaderID(Short.parseShort(readerIDField.getText()));         
    }
    
    // Update reader ID in transmitter. Will unlink TX from RD
    public void updateTransmitterID(Transmitter tx) {
        tx.setCurrentID(Long.parseLong(transmitterIDField.getText()));         
    }
    
    // Updates all four keys with int
    public void updateKey(Transmitter tx) { 
        tx.setSharedKey(Integer.parseInt(keyField.getText()));         
    }
    
    // Updates all four keys with int
    public void updateIV(Transmitter tx) {
        tx.setCurrentIV(Long.parseLong(ivField.getText()));         
    }
    
    @FXML
    public void updateValues() {
        if (txList.getFocusModel().getFocusedIndex() > -1) {
            int index = txList.getFocusModel().getFocusedIndex();
            Transmitter tx = TXs.get(index);
            System.out.println();

            if (!readerIDField.getText().isEmpty()) {
                System.out.println("Updating reader ID");
                updateReaderID(tx);
            } 
            if (!transmitterIDField.getText().isEmpty()) {
                System.out.println("Updating transmitter ID");
                updateTransmitterID(tx);
            }
            
            if (!keyField.getText().isEmpty()) {
                System.out.println("Updating key ID");
                updateKey(tx);
            } 
            
            if (!ivField.getText().isEmpty()) {
                System.out.println("Updating IV");
                updateIV(tx);
            }    
        } else {
            logConsole.setText("Please select a transmitter.");
        }
    }
    
    /* 
     * Method that demonstrates applications main logic takes selected TX and 
     * selected RD and establishes communication through a request and response
     * packet. The packets are both 144-bits and each can be displayed to 
     * Console if displayValues is true (checkbox is selected). For a further 
     * description of the application and the authentication process between TX
     * and RD, view the README.md in the parent directory
    */
    @FXML
    public void authenticate() {
        // Get selected TX and RD indexes
        int txIndex = txList.getFocusModel().getFocusedIndex();
        int rdIndex = rdList.getFocusModel().getFocusedIndex();
        if (txIndex > -1 && rdIndex > -1) {
            try {
                // User index to get transmitter and see if linked reader ID exists
                Reader reader = null;
                Transmitter tx = TXs.get(txIndex);
                for (Reader rd : RDs) {
                    // Check if transmitter ID matches a reader ID
                    if (tx.getReaderID() == rd.getReaderID()) {
                        // Check if the selected reader matches transmitter reader ID
                        if (RDs.get(rdIndex).getReaderID() == tx.getReaderID()) {
                            reader = rd;
                        }                    
                    } 
                }
                // If the reader ID matches no reader, update status to failure
                if (reader == null) {
                        logConsole.setText("Selected transmitter is not linked to the selected reader.");
                        colorRightArrow("fail");
                        colorLeftArrow("fail");
                } else {
                    // Check to see if checkboxes are selected and update values if they are
                    boolean printingValues = printValues.isSelected();
                    boolean printingProgress = printProgress.isSelected();
                    logConsole.setText("Authentication Begin \n");
                    System.out.print("Between RD #" + Integer.toString(rdIndex+1));
                    System.out.println(" and TX #" + Integer.toString(txIndex+1));

                    /* In software we fetch the request packet from TX though the
                     * TX is the one that initalizes the authentication process */
                    Packet requestPacket = tx.getRequestPacket(printingProgress, printingValues);
  
                    colorLeftArrow("success"); // Color arrow green
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
                        colorRightArrow("success");
                    }
                    // Fail message
                    else {
                        System.out.println("Authentication Failed");
                        colorRightArrow("fail");
                    }
                }
                // If unobserved error occurs, notify user.
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Authentication Ended With Error. Please contact admin.");
            }  
        }
    }
    
    /* Colors the left arrow depending on passed status */
    public void colorLeftArrow(String status) {
        // Color each of the three lines that make up the arrow
        for(Node line : leftArrow.getChildren()) {
            if (status.equals("success")) {
                // Color Green (success)
                line.setStyle("-fx-stroke: green;");
            } else if (status.equals("fail")) {
                // Color Red (fail)
                line.setStyle("-fx-stroke: red;");
            } else {
                // Color Black (used in reset)
                line.setStyle("-fx-stroke: black;");
            }
        }
    }
    
    /* Colors the right arrow depending on passed status */
    public void colorRightArrow(String status) {
        for(Node line : rightArrow.getChildren()) {
            if (status.equals("success")) {
                line.setStyle("-fx-stroke: green;");
            } else if (status.equals("fail")) {
                line.setStyle("-fx-stroke: red;");
            } else {
                line.setStyle("-fx-stroke: black;");
            }
        }
    }
    
    /* Reset everything to "restart" application */
    public void resetAll() {
        // Clear all stored values
        RDs.clear();
        TXs.clear();
        numOfReaders = 0;
        numOfTransmitters = 0;
        txIsSelected = false;
        rdIsSelected = false;
        // Disable fields and authentication btn
        readerIDField.setDisable(true);
        transmitterIDField.setDisable(true);
        keyField.setDisable(true);
        ivField.setDisable(true);  
        authenticateBtn.setDisable(true); 
        // Clear current values
        currentRdID.setText("");
        txIDLabel.setText("Transmitter ID:");
        currentTxID.setText("");
        currentKey.setText(""); 
        currentIVLabel.setText("Current IV:");
        currentIV.setText("");
        // Clear Updateable Values
        readerIDField.setText("");
        transmitterIDField.setText("");
        keyField.setText("");
        ivField.setText("");
        // Clear ListViews
        txList.getItems().clear();
        rdList.getItems().clear();
        // Clear arrow colors
        colorLeftArrow("");
        colorRightArrow("");
        // Set Console Text
        logConsole.setText("Stored Values Cleared. Reset Complete.");      
    }
    
    /* Clear console method called by button for clearing the console */
    public void clearConsole() {
        logConsole.setText("");      
    }
    
    /* Initalize Console and diable appropriate fields */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Create console to output status of the authentication        
        Console console = new Console(logConsole);
        PrintStream ps = new PrintStream(console, true);
        System.setOut(ps);
        System.setErr(ps);
        logConsole.setText("First, add a reader with linked transmitters. \n"
                +          "Next, select two and press authenticate to begin authentication."); 
        
        // Disable update fields until TX is selected  
        readerIDField.setDisable(true);
        transmitterIDField.setDisable(true);
        keyField.setDisable(true);
        ivField.setDisable(true);  
    }
    
    /* Redirects System.out to Console TextArea located in the GUI */
    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }

    
}
