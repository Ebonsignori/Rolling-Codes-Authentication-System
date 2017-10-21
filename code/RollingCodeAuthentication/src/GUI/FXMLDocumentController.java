package GUI;

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

public class FXMLDocumentController implements Initializable {
    
    public ArrayList<Reader> RDs = new ArrayList<Reader>();
    public ArrayList<Transmitter> TXs = new ArrayList<Transmitter>();
    private int numOfReaders = 0;
    private int numOfTransmitters = 0;
    private boolean txIsSelected = false;
    private boolean rdIsSelected = false;
    
    @FXML
    public ListView<String> txList;
    public ListView<String> rdList;
    public NumberOfTXTextField numOfTXText;
    public Button addReaderBtn;
    
    @FXML
    public TextArea logConsole;
    public Button authenticateBtn;
    public CheckBox printProgress;
    public CheckBox printValues;
    
    @FXML
    public Label txIDLabel;
    public Label currentIVLabel;
    
    @FXML
    public Label currentTxID;
    public Label currentRdID;
    public Label currentKey;
    public Label currentIV;
    
    @FXML
    public TextField readerIDField;
    public TextField transmitterIDField;
    public TextField keyField;
    public TextField ivField;
    
    @FXML
    public Group leftArrow;
    public Group rightArrow;
    
    
    @FXML
    public void addReader() {
        if (numOfReaders < 5) {
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

            ObservableList<String> txItems = FXCollections.observableArrayList(txNames);
            txList.getItems().addAll(txItems);
            rdList.getItems().add("Reader #" + Integer.toString(this.numOfReaders));

            String[] colorArr = new String[]{"derive(blue, 50%)", "derive(red, 50%)", "derive(yellow, 75%)", "derive(palegreen, 50%)", "derive(orange, 50%)"};
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
                                for (int i = 1; i <= numOfReaders; i++) {
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
    
    @FXML
    public void selectTX() {
        if (!txList.getItems().isEmpty()) {
            int index = txList.getFocusModel().getFocusedIndex();
            Transmitter tx = TXs.get(index);
            txIsSelected = true;

            // Display current tx values
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
    
    @FXML
    public void selectRD() {
        if (!rdList.getItems().isEmpty()) {
            int index = rdList.getFocusModel().getFocusedIndex();
            Reader rd = RDs.get(index);
            rdIsSelected = true;

            // Display current rd values
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
    
    @FXML
    public void authenticate() {
        int txIndex = txList.getFocusModel().getFocusedIndex();
        int rdIndex = rdList.getFocusModel().getFocusedIndex();
        if (txIndex > -1 && rdIndex > -1) {
            try {
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
                
                if (reader == null) {
                        logConsole.setText("Selected transmitter is not linked to the selected reader.");
                        colorRightArrow("fail");
                        colorLeftArrow("fail");
                } else {
                    boolean printingValues = printValues.isSelected();
                    boolean printingProgress = printProgress.isSelected();
                    logConsole.setText("Authentication Begin \n");
                    Packet requestPacket = tx.getRequestPacket(printingProgress, printingValues);
                    colorLeftArrow("success");
                    if (printingValues) {
                        System.out.println("Request Packet: ");
                        System.out.println(requestPacket.toString());
                    }
                    Packet responsePacket = reader.getResponsePacket(requestPacket, printingProgress, printingValues);
                    boolean isSuccessful = tx.updateRecord(responsePacket, printingProgress, printingValues);
                    
                    if (printingValues) {
                        System.out.println("Response Packet: ");
                        System.out.println(responsePacket.toString());
                    }

                    if (isSuccessful) {
                        System.out.println("Authentication Completed Sucessfully");
                        colorRightArrow("success");
                    }
                    else {
                        System.out.println("Authentication Failed");
                        colorRightArrow("fail");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Authentication Ended With Error.");
            }  
        }
    }
    
    public void colorLeftArrow(String status) {
        for(Node line : leftArrow.getChildren()) {
            if (status.equals("success")) {
                line.setStyle("-fx-stroke: green;");
            } else if (status.equals("fail")) {
                line.setStyle("-fx-stroke: red;");
            } else {
                line.setStyle("-fx-stroke: black;");
            }
        }
    }
    
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
        // Clear ListViews
        txList.getItems().clear();
        rdList.getItems().clear();
        // Clear arrow colors
        colorLeftArrow("");
        colorRightArrow("");
        // Set Console Text
        logConsole.setText("Stored Values Cleared. Reset Complete.");      
    }
    
    public void clearConsole() {
        logConsole.setText("");      
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Create console to output status of the authentication        
        Console console = new Console(logConsole);
        PrintStream ps = new PrintStream(console, true);
        System.setOut(ps);
        System.setErr(ps);
        logConsole.setText("First, add a reader with linked transmitters. \n"
                +          "Next, select two and press authenticate to begin authentication."); 
        
        readerIDField.setDisable(true);
        transmitterIDField.setDisable(true);
        keyField.setDisable(true);
        ivField.setDisable(true);  
    }
    
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
