/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;
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
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import javafx.application.Application;

/**
 *
 * @author Jon
 */
public class UserInterface extends Application {
private List<File> _images;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main (String[] args) throws IOException {
        WriteToFile();
        ReadFromFile();
        System.setProperty("java.net.preferIPv4Stack" , "true");
        launch (args);
    }

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
            exportImages();
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
     */
    private void exportImages() {       
        HttpClient client=HttpClients.createDefault();
        HttpPost post=new HttpPost("http://34.236.92.140");

        for(File file:_images){
            System.out.println(file.getName());

            MultipartEntity entity=new MultipartEntity();
            entity.addPart("file", new FileBody(file));
            
            post.setEntity(entity);
            HttpResponse response=null;
            try {
                response = client.execute(post);
            } catch (IOException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (response!=null) {
                HttpEntity responseEnt=response.getEntity();
                try {
                    System.out.println(EntityUtils.toString(responseEnt));
                } catch (IOException | ParseException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }       
    }
    public static void ReadFromFile() throws IOException{
        String readFromFileName = "life.txt";
        String lineFromFile = null;
        try{
            FileReader fileReader = new FileReader(readFromFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while(null!= (lineFromFile = bufferedReader.readLine())){
                System.out.println(lineFromFile);
            }
        }catch(FileNotFoundException fileException){
            System.out.println("The file" + readFromFileName + "cannot be opened");
        }
        
    }
    public static void WriteToFile(){
        String writeToFileName = "life.txt";
        
        try{
            FileWriter fileWriter = new FileWriter(writeToFileName);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write("Hello, I am Jonathan Poston.");
                bufferedWriter.write("I am the creator of the User Interface for this project.");
            }
        }catch(IOException ioException){
            System.out.println("The file" + writeToFileName + "cannot be written to");
        }
    }
}

/**
 * Class to store the archival information of each image sent to SeeFood
 * @author Sam
 */
class Archived {
    
    private String _filename;
    private boolean _food;
    private double _con;    //short for "confidence value"
    private Date _date;
    
    /**
     * Constructor
     * @param filename - name of image file
     * @param food - is or is not food
     * @param con - confidence level of SeeFood
     * @param date - date these values were generated
     */
    public Archived(String filename, boolean food, double con, Date date){
        _filename=filename;
        _food=food;
        _con=con;
        _date=date;
    }    
    
    @Override
    /**
     * Overrides toString method to output contents of object, each separated
     * by 2 tabs.
     * @return a formatted string of the object's values
     */
    public String toString(){
        return _filename+"/t/t"+_food+"/t/t"+_con+"/t/t"+_date.toString();
    }
}
