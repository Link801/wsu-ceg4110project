<<<<<<< HEAD
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaApplication9;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
/**
 *
 * @author Jon
 */
public class UserInterface extends Application{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
//    public UserInterface(){
//        JFrame userInterface = new JFrame();
//        userInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        userInterface.setTitle("SeeFood AI UserInterface");
//        userInterface.setSize(800, 750);
//        userInterface.setLocationRelativeTo(null);
//        JButton userButton = new JButton("Add Image to Client");
//        userButton.addActionListener(new ActionListener(){
//           @Override 
//            public void actionPerformed(ActionEvent event) {
//              
//            }
//        });
//        userInterface.add(userButton,BorderLayout.SOUTH);
//        userInterface.setVisible(true);
//    }

    @Override
    public void start(Stage primaryStage){
       final FileChooser fc = new FileChooser();
       primaryStage.setTitle("SeeFood AI User Interface");
       Button imageButton = new Button("Import Images");
       imageButton.setOnAction((ActionEvent event) -> {
           List<File> images = fc.showOpenMultipleDialog(primaryStage);
           if(images != null){
               int i = 0;
               for(File file : images){
                   System.out.println("image " + i);
                   i++;
               }
           }
       });
       
       final GridPane inputGridPane = new GridPane();
       GridPane.setConstraints(imageButton, 0, 0);
       inputGridPane.setHgap(6);
       inputGridPane.setVgap(6);
       inputGridPane.getChildren().addAll(imageButton);
       
       final Pane rootGroup = new VBox(12);
       rootGroup.getChildren().addAll(inputGridPane);       
       rootGroup.setPadding(new Insets(12, 12, 12, 12));
       
       primaryStage.setScene(new Scene(rootGroup));
       primaryStage.show();
    }
}
=======
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
