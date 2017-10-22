package rollingcodeauthentication; // Package for main application logic 

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/* Main method and Application entry point */
public class RollingCodeAuthentication extends Application {
    
    /* Get FXML document as Pane and set it to scene */
    @Override
    public void start(Stage primaryStage)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(getClass().getResource("/GUI/FXMLDocument.fxml"));
            primaryStage.setTitle("Rolling Code Authentication");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
    
    /* Main method for starting GUI loop */
    public static void main(String[] args) {
        launch(args);    
    }  
    
}
