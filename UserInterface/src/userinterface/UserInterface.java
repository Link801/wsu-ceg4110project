/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import javafx.scene.paint.Color;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javafx.application.*;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
    public static void main(String[] args) throws IOException {
        WriteToFile();
        ReadFromFile();
        System.setProperty("java.net.preferIPv4Stack", "true");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        final FileChooser fc = new FileChooser();
        Stage secondaryStage = new Stage();
        Stage tertiaryStage = new Stage();
        primaryStage.setTitle("SeeFood AI User Interface");
        primaryStage.setWidth(500);
        primaryStage.setHeight(400);
        Button imageButton = new Button("Import Images");
        Button exportButton = new Button("Send Images to SeeFood");
        Button newUserButton = new Button("Create New Account");
        Button loginButton = new Button("Login to Your Account");
        //When image button is pressed, a FileChooser should load up and add all selected images to a list
        imageButton.setOnAction((ActionEvent event) -> {
            _images = fc.showOpenMultipleDialog(primaryStage);
            if (_images != null) {
                int i = 0;
                //loop to verify that all selected images are added
                for (File file : _images) {
                    System.out.println("image " + i);
                    i++;
                }
            }
        });

        exportButton.setOnAction((ActionEvent event) -> {
            exportImages();
        });
        newUserButton.setOnAction((ActionEvent event) -> {
            CreateUser(secondaryStage);
        });
        loginButton.setOnAction((ActionEvent event) -> {
            Login(tertiaryStage);
        });

        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(imageButton, 0, 0);
        GridPane.setConstraints(exportButton, 0, 1);
        GridPane.setConstraints(newUserButton, 28, 0);
        GridPane.setConstraints(loginButton, 28, 1);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(imageButton, exportButton);
        inputGridPane.getChildren().addAll(loginButton, newUserButton);
        Pane rootGroup = new VBox(50);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));
        primaryStage.setScene(new Scene(rootGroup));
        primaryStage.show();
    }

    /**
     * Sends one or more images to SeeFood via HTTP POST.
     */
    private void exportImages() {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://34.236.92.140");

        for (File file : _images) {
            System.out.println(file.getName());

            MultipartEntity entity = new MultipartEntity();
            entity.addPart("file", new FileBody(file));

            post.setEntity(entity);
            HttpResponse response = null;
            try {
                response = client.execute(post);
            } catch (IOException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (response != null) {
                HttpEntity responseEnt = response.getEntity();
                try {
                    System.out.println(EntityUtils.toString(responseEnt));
                } catch (IOException | ParseException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void ReadFromFile() throws IOException {
        String readFromFileName = "life.txt";
        String lineFromFile = null;
        try {
            FileReader fileReader = new FileReader(readFromFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (null != (lineFromFile = bufferedReader.readLine())) {
                System.out.println(lineFromFile);
            }
        } catch (FileNotFoundException fileException) {
            System.out.println("The file" + readFromFileName + "cannot be opened");
        }

    }

    public static void WriteToFile() {
        String writeToFileName = "life.txt";

        try {
            FileWriter fileWriter = new FileWriter(writeToFileName);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write("Hello, I am Jonathan Poston.\n");
                bufferedWriter.write("I am the creator of the User Interface for this project.");
            }
        } catch (IOException ioException) {
            System.out.println("The file" + writeToFileName + "cannot be written to");
        }
    }

    public void CreateUser(Stage secondaryStage) {
        secondaryStage.setTitle("Create New Accounnt");
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 50, 50, 50));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20, 20, 20, 30));
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        Text text = new Text();
        InnerShadow is = new InnerShadow();
        is.setOffsetX(2.0f);
        is.setOffsetY(2.0f);
        text.setEffect(is);
        text.setX(20);
        text.setY(100);
        text.setText("Create A New User Account");
        text.setFill(Color.RED);
        text.setFont(Font.font("null", FontWeight.BOLD, 30));

        text.setTranslateX(0);
        text.setTranslateY(0);
        Label userNameLabel = new Label("UserName:");
        final TextField userNameTextField = new TextField();
        Label repeatuserNameLabel = new Label("Renter UserName:");
        final TextField repeatuserNameTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        final TextField passwordTextField = new TextField();
        Label repeatpasswordLabel = new Label("Renter Password:");
        final TextField repeatpasswordTextField = new TextField();
        Button loginToAccountButton = new Button("Create New Account");
        final Label loginMessageLabel = new Label();
        gridPane.add(text, 0, 0);
        gridPane.add(userNameLabel, 0, 1);
        gridPane.add(userNameTextField, 0, 2);
        gridPane.add(repeatuserNameLabel, 0, 3);
        gridPane.add(repeatuserNameTextField, 0, 4);
        gridPane.add(passwordLabel, 0, 5);
        gridPane.add(passwordTextField, 0, 6);
        gridPane.add(repeatpasswordLabel, 0, 7);
        gridPane.add(repeatpasswordTextField, 0, 8);
        gridPane.add(loginToAccountButton, 0, 9);
        gridPane.add(loginMessageLabel, 0, 10);

        Pane rootGroup = new VBox(50);
        rootGroup.getChildren().addAll(gridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));
        secondaryStage.setScene(new Scene(rootGroup));
        secondaryStage.show();

    }

    public void Login(Stage tertiaryStage) {
        tertiaryStage.setTitle("Login");
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 50, 50, 50));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20, 20, 20, 30));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        Text text = new Text();
        InnerShadow is = new InnerShadow();
        is.setOffsetX(2.0f);
        is.setOffsetY(2.0f);
        text.setEffect(is);
        text.setX(20);
        text.setY(100);
        text.setText("Login into Your Account");
        text.setFill(Color.MAGENTA);
        text.setFont(Font.font("null", FontWeight.BOLD, 30));

        text.setTranslateX(0);
        text.setTranslateY(0);
        Label userNameLabel = new Label("UserName:");
        final TextField userNameTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        final TextField passwordTextField = new TextField();
        Button loginToAccountButton = new Button("Login");
        final Label loginMessageLabel = new Label();
        gridPane.add(text, 0, 0);
        gridPane.add(userNameLabel, 0, 1);
        gridPane.add(userNameTextField, 0, 2);
        gridPane.add(passwordLabel, 0, 3);
        gridPane.add(passwordTextField, 0, 4);
        gridPane.add(loginToAccountButton, 0, 5);
        gridPane.add(loginMessageLabel, 0, 6);

        Pane rootGroup = new VBox(50);
        rootGroup.getChildren().addAll(gridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));
        tertiaryStage.setScene(new Scene(rootGroup));
        tertiaryStage.show();

    }

}

/**
 * Class to store the archival information of each image sent to SeeFood
 *
 * @author Sam
 */
class Archived {

    private String _filename;
    private boolean _food;
    private double _con;    //short for "confidence value"
    private Date _date;

    /**
     * Constructor
     *
     * @param filename - name of image file
     * @param food - is or is not food
     * @param con - confidence level of SeeFood
     * @param date - date these values were generated
     */
    public Archived(String filename, boolean food, double con, Date date) {
        _filename = filename;
        _food = food;
        _con = con;
        _date = date;
    }

    @Override
    /**
     * Overrides toString method to output contents of object, each separated by
     * 2 tabs.
     *
     * @return a formatted string of the object's values
     */
    public String toString() {
        return _filename + "/t/t" + _food + "/t/t" + _con + "/t/t" + _date.toString();
    }
}
