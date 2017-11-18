/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication12;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Jon
 */
public class UserInterface extends Application {
    private List<File> _images;
    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) {
        System.setProperty("java.net.preferIPv4Stack" , "true");
        launch (args);
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
        HttpClient client=HttpClients.createDefault();
        HttpPost post=new HttpPost("http://34.236.92.140");
        
//        URL url=new URL("http://34.236.92.140");
//        HttpURLConnection con=(HttpURLConnection) url.openConnection();
//
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//        con.setRequestProperty("Content-Type", "multipart/form-data");
//
//        con.setDoOutput(true);  //this must be set to true in order to work
//        con.setDoInput(true);
//        
        for(File file:_images){
            System.out.println(file.getName());

            MultipartEntity entity=new MultipartEntity();
            entity.addPart("file", new FileBody(file));
            
            post.setEntity(entity);
            HttpResponse response=client.execute(post);
            
            if (response!=null) {
                HttpEntity responseEnt=response.getEntity();
                System.out.println(EntityUtils.toString(responseEnt));
            }
            
//            StringBuilder builder = new StringBuilder();
//            builder.append(con.getResponseCode())
//                   .append(" ")
//                   .append(con.getResponseMessage())
//                   .append("\n");
//
//            Map<String, List<String>> map = con.getHeaderFields();
//            for (Map.Entry<String, List<String>> entry : map.entrySet()){
//                if (entry.getKey() == null) 
//                    continue;
//                builder.append( entry.getKey())
//                       .append(": ");
//
//                List<String> headerValues = entry.getValue();
//                Iterator<String> it = headerValues.iterator();
//                if (it.hasNext()) {
//                    builder.append(it.next());
//
//                    while (it.hasNext()) {
//                        builder.append(", ")
//                               .append(it.next());
//                    }
//                }
//
//                builder.append("\n");
//            }
//
//            System.out.println(builder);
        }       
//        con.disconnect();
    }
}
