/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rollingcodeauthentication;

import java.util.Arrays;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Evan
 */
public class RollingCodeAuthentication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        int numOfTX = 10;
        Reader reader = new Reader();
        Transmitter[] TXs = new Transmitter[numOfTX];
        String[] txNames = new String[10];
        for (int i = 0; i < 10; i++) {
            TXs[i] = new Transmitter();
            txNames[i] = "TX #" + (i + 1);
        }
        ListView<String> txList = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList(txNames);
        txList.setItems(items);
        txList.setPrefHeight(233);
        
        Button btnSendTX = new Button();
        btnSendTX.setText("Send Request");
        btnSendTX.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                Transmitter tx = TXs[txList.getFocusModel().getFocusedIndex()];
                reader.getResponsePacket(tx.getRequestPacket());
            }
        });
        
        VBox leftVBox = new VBox();
        leftVBox.getChildren().addAll(txList, btnSendTX);
        VBox.setVgrow(txList, Priority.ALWAYS);
        
        VBox rightVBox = new VBox();
        
        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(leftVBox, rightVBox);
        AnchorPane.setLeftAnchor(leftVBox, 10d);
        AnchorPane.setRightAnchor(rightVBox, 10d);
        
        Scene scene = new Scene(root, 500, 400);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
