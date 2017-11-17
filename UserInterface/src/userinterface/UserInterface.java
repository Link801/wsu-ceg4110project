/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication12;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private final String _ip="127.0.0.1:5000";   //IP address of SeeFood VM (placeholder)
    private final String _path="C:\\Users\\ecslogon\\Downloads\\ceg4110_project\\CEG4110_SeeFood\\test.py";
    private final String _charset="UTF-8";
    
    private List<File> _images;
    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) {
        System.setProperty("java.net.preferIPv4Stack" , "true");
        launch (args);
//        System.out.println(System.getProperty("user.dir"));
    }

    @Override
    public void start (Stage primaryStage) {
        final FileChooser fc=new FileChooser ();
        primaryStage.setTitle ("SeeFood AI User Interface");
        Button imageButton=new Button ("Import Images");
        Button exportButton=new Button ("Send Images to SeeFood");
        //When image button is pressed, a FileChooser should load up and add all selected images to a list
        imageButton.setOnAction ((ActionEvent event) -> {
            _images=fc.showOpenMultipleDialog (primaryStage);
            if (_images!=null) {
                int i=0;
                //loop to verify that all selected images are added
                for (File file:_images) {
                    System.out.println ("image "+i);
                    i++;
                }
            }
        });
        
        exportButton.setOnAction ((ActionEvent event) -> {
            try {
                exportImages();
            } catch (IOException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        final GridPane inputGridPane=new GridPane ();
       
        GridPane.setConstraints (imageButton,0,0);
        GridPane.setConstraints (exportButton,0,1);
        inputGridPane.setHgap (6);
        inputGridPane.setVgap (6);
        inputGridPane.getChildren ().addAll (imageButton, exportButton);
        
        final Pane rootGroup=new VBox (12);
        rootGroup.getChildren ().addAll (inputGridPane);
        rootGroup.setPadding (new Insets (12,12,12,12));
        primaryStage.setScene (new Scene (rootGroup));
        primaryStage.show ();
    }
    /**
     * Sends one or more images to SeeFood via HTTP POST.
     * @throws MalformedURLException
     * @throws IOException 
     */
    private void exportImages() throws MalformedURLException, IOException{       
        //InetAddress host=InetAddress.getByName(_ip);
//        System.out.println(InetAddress.getByName(_ip));
        URL url=new URL("http://34.236.92.140");
        HttpURLConnection con=(HttpURLConnection) url.openConnection();
        String output;
        
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "multipart/form-data");
        
        FileChannel in;
        WritableByteChannel out;
        
        con.setDoOutput(true);  //this must be set to true in order to work
        con.setDoInput(true);
        
        for(File file:_images){
            in=new FileInputStream(file).getChannel();
            out=Channels.newChannel(con.getOutputStream());
            
            in.transferTo(0, file.length(), out);
            
            output=readResultsToString(con);
            
            //Output the result from SeeFood
            //Later on, this result should be stored for each image
            if(output!=null){
                System.out.println(output);
            } else {
                System.out.println("There was an error in the connection.");
            }
            in.close();
            out.close();
        }       
        con.disconnect();
    }
    
    /**
     * Helper method to exportImages(). Should get response from server
     * and append contents to string.
     * @param con - the active http connection
     * @return response from the server
     */
    private String readResultsToString(HttpURLConnection con){
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
             
        try {          
            is=new BufferedInputStream(con.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String inputLine="";
            while((inputLine=br.readLine())!=null){
                sb.append(inputLine);
            }
            result=sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }
}
