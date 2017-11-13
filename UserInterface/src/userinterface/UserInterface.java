/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.*;
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
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Jon
 */
public class UserInterface extends Application {
    
    private List<File> _images;
    private final String _user = "user";   //this should be replaced with the .pem filename
    private final String _pw = "password";   //this is also a placeholder
    private final String _ip = "34.225.62.156";    //IP address of SeeFood VM
    private final int port = 22;    //SFTP usually runs on port 22
    private JSch jsch;

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
            for (File file:_images) {
                jsch = new JSch();
                try {
                    Session session = jsch.getSession(_user, _ip, port);
                    session.setPassword(_pw);
                    
                    session.setConfig("StrictHostKeyChecking", "no");
                    System.out.println("Establishing Connection...");
                    
                    session.connect();
                    
                    System.out.println("Connection established.");
                    System.out.println("Creating SFTP Channel.");
                    
                    ChannelSftp sftpChannel =
                            (ChannelSftp) session.openChannel("sftp");
                    sftpChannel.connect();
                    
                    HttpClient httpclient = new DefaultHttpClient();
                    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                    HttpPost httppost = new HttpPost(_ip);
                    MultipartEntity mpEntity = new MultipartEntity();
                    ContentBody cbFile = new FileBody(file, "image/jpeg");
                    mpEntity.addPart("userfile", cbFile);
                    httppost.setEntity(mpEntity);
                    
                    System.out.println("executing request " + httppost.getRequestLine());
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity resEntity = response.getEntity();
                    
                    System.out.println(response.getStatusLine());
                    if (resEntity != null) {
                      System.out.println(EntityUtils.toString(resEntity));
                    }
                    if (resEntity != null) {
                      resEntity.consumeContent();
                    }

                    httpclient.getConnectionManager().shutdown();
                    sftpChannel.disconnect();
                    session.disconnect();
                } catch (JSchException | IOException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
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
}