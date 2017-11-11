/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import java.io.*;
import java.util.*;
import javafx.application.*;
import static javafx.application.Application.launch;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 *
 * @author Jon
 */
public class UserInterface extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) {
        launch (args);
    }

    @Override
    public void start (Stage primaryStage) {
        final FileChooser fc=new FileChooser ();

        primaryStage.setTitle ("SeeFood AI User Interface");
        Button imageButton=new Button ("Import Images");
        imageButton.setOnAction ((ActionEvent event)->{
            List<File> images=fc.showOpenMultipleDialog (primaryStage);
            if (images!=null) {
                int i=0;
                for (File file:images) {
                    System.out.println ("image "+i);
                    i++;
                }
            }
        });
        
        final GridPane inputGridPane=new GridPane ();
       
        GridPane.setConstraints (imageButton,0,0);
        inputGridPane.setHgap (6);
        inputGridPane.setVgap (6);
        inputGridPane.getChildren ().addAll (imageButton);
        
        final Pane rootGroup=new VBox (12);
        rootGroup.getChildren ().addAll (inputGridPane);
        rootGroup.setPadding (new Insets (12,12,12,12));
        primaryStage.setScene (new Scene (rootGroup));
        primaryStage.show ();
    }

    
}
>>>>>>> origin/userInterface
