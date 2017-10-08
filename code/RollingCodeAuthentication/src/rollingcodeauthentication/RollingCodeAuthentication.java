package rollingcodeauthentication;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.BitSet;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/* GUI for Rolling Code Authentication */
public class RollingCodeAuthentication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create a reader and populate it with hard coded number of TXs
        int numOfTx = 10;
        Reader reader = new Reader(numOfTx);
        Transmitter[] TXs = new Transmitter[numOfTx];
        String[] txNames = new String[10];
        // Populate reader with transmitters and generate names for ListView
        for (int i = 0; i < numOfTx; i++) {
            TXs[i] = new Transmitter(reader);
            txNames[i] = "TX #" + Integer.toString(i + 1);
        }

        // Create List to view transmitters
        ListView<String> txList = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList(txNames);
        txList.setItems(items);
        txList.setPrefHeight(233);
        txList.setPrefWidth(40);
        
        // Create a packet request button to initiate authentication process
        Button btnSendTX = new Button();
        btnSendTX.setText("Press Send to begin Authentication.");
        btnSendTX.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int selected = txList.getFocusModel().getFocusedIndex();
                if (selected > 0 && selected < numOfTx) {
                    try {
                    Transmitter tx = TXs[selected];
                    BitSet[] requestPacket = tx.getRequestPacket(true);
                    BitSet[] responsePacket = reader.getResponsePacket(requestPacket);
                    tx.updateRecord(responsePacket);
                    System.out.println("Authentication Completed Sucessfully.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Authentication Ended With Error.");
                    }
                }
            }
        });
        
        // Create console to output status of the authentication        
        TextArea authLogConsole = new TextArea();
        Console console = new Console(authLogConsole);
        PrintStream ps = new PrintStream(console, true);
        System.setOut(ps);
        System.setErr(ps);
        authLogConsole.setEditable(false);
        authLogConsole.setText("Send Request Pakcet to Begin Authentication \n");   
        authLogConsole.setStyle("-fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick; -fx-font-size: 12px;");
        authLogConsole.setPrefHeight(115);
        authLogConsole.setPrefWidth(40);
        
        // Populate left VBox with list of transmitters, a button, and a console
        VBox leftVBox = new VBox();
        leftVBox.getChildren().addAll(txList, btnSendTX);
        VBox.setVgrow(txList, Priority.ALWAYS);
        
        // Create List to view Reader(s)
        ListView<String> readerList = new ListView<String>();
        ObservableList<String> readerItems = FXCollections.observableArrayList(new String[]{"Reader1"});
        readerList.setItems(readerItems);
        readerList.setPrefHeight(50);
        
        // Populate left VBox with reader and a console
        VBox rightVBox = new VBox();
        rightVBox.getChildren().add(readerList);
        VBox.setVgrow(readerList, Priority.ALWAYS);
        
        BorderPane root = new BorderPane();
        root.setLeft(leftVBox);
        root.setRight(rightVBox);
        root.setBottom(authLogConsole);
        
        Scene scene = new Scene(root, 600, 400);
        
        primaryStage.setTitle("Rolling Code Authentication");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
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
