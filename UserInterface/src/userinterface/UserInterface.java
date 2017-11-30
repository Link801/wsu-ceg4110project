package userinterface;

import javafx.scene.paint.Color;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javafx.application.*;
import static javafx.application.Application.launch;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
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
 * @author Jon, Sam
 */
public class UserInterface extends Application {

    private List<File> _images;
    private String _user="default";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        launch(args);
    }

    @Override
    /**
     * The main driver of the application.
     */
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
            createUser(secondaryStage);
        });
        loginButton.setOnAction((ActionEvent event) -> {
            login(tertiaryStage);
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
     * Sends one or more images to SeeFood via HTTP POST. It then writes
     * results to a user file.
     */
    private void exportImages() {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://34.236.92.140");

        for (File file : _images) {
            System.out.println(file.getName());

            MultipartEntity entity = new MultipartEntity(); //Yes, this method is deprecated. Please don't mess with it.
            entity.addPart("file", new FileBody(file));

            post.setEntity(entity);
            HttpResponse response = null;
            try {
                response = client.execute(post);
            } catch (IOException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (response!=null) {
                HttpEntity responseEnt = response.getEntity();
                try {
                    //Get values from SeeFood output
                    String str=EntityUtils.toString(responseEnt);
                    //Get rid of the brackets at either end of the returned string
                    str=str.replaceAll("\\[\\[", "");
                    str=str.replaceAll("\\]\\]", "");
                    String[] arr=str.split("  ");
                    //For some reason, non-negative left values (values for images that do contain food)
                    //have a space in front of them. Get rid of this.
                    arr[0]=arr[0].trim();
                    for(int i=0;i<arr.length ;i++){
                        System.out.println(i+": "+arr[i]);
                    }
                    //Parse these strings to doubles
                    double left=Double.parseDouble(arr[0]);
                    double right=Double.parseDouble(arr[1]);
                    
                    //Record the results to a user file
                    Archived archive=new Archived(file.getName(), left, right, new Date());
                    writeToFile(archive);

                } catch (IOException | ParseException ex) {
                    //IOException is thrown when running multiple images, but
                    //functionality of program doesn't seem to be affected.
                }
            }
        }
    }
    /**
     * Read archive information from file. Currently just reads sample input.
     * @throws IOException 
     */
    private static void readFromFile() throws IOException {
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
    
    /**
     * Writes information to a file. Currently just writes hard-coded output.
     * @param arc - An object of the custom Archived class (see below)
     */
    private void writeToFile(Archived arc) {
        String writeToFileName = "archive\\"+_user+"_archive.txt";
        FileWriter fw=null;
        BufferedWriter bw=null;
        try {
            fw=new FileWriter(writeToFileName, true);
            bw=new BufferedWriter(fw);
            bw.newLine();
            bw.write(arc.toString());
        } catch (IOException e) {
            System.out.println("The file" + writeToFileName + "cannot be written to");
        } finally {
            closer(fw, bw);
        }
    }
    
    /**
     * Creates a personal file for a new user. All images that user exports
     * to SeeFood are logged in their own personal file.
     * @param secondaryStage - A new window for the interface.
     */
    private void createUser(Stage secondaryStage) {
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
        Label repeatuserNameLabel = new Label("Re-enter UserName:");
        final TextField repeatuserNameTextField = new TextField();
        

        
        Label passwordLabel = new Label("Password:");
        final PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Your Password");
        
        Label repeatpasswordLabel = new Label("Re-enter Password:");
        final PasswordField repeatpasswordField = new PasswordField();
        repeatpasswordField.setPromptText("Your Password");
        
        Button createNewAccountButton = new Button("Create New Account");
        final Text createAccountMessage = new Text();
        createNewAccountButton.setOnAction((ActionEvent event)->{
            
            boolean errors=false;
            String username=null;
            String pw=null;
            FileWriter fw=null;
            BufferedWriter bw=null;
            FileWriter fw2=null;
            BufferedWriter bw2=null;

            /*Assign username if
            - it is equal to the string in the repeat field
            - AND it is greater than 6 characters
            */
            String entered=userNameTextField.getText();
            if(entered.equals(repeatuserNameTextField.getText()) 
                    && entered.length()>=6){
                username=entered;
            } else {
                errors=true;
            }
            
            /*Assign password if
            - it is equal to the string in the repeat field
            - AND it is at least 8 characters
            */          
            entered=passwordField.getText();
            if(entered.equals(repeatpasswordField.getText()) 
                    && entered.length()>=8){
                pw=entered;
                
            } else {
                errors=true;
            }
            
            //Write info to a new file
            if(!errors){
                try {
                    fw=new FileWriter("users\\"+username+"_info.txt");
                    bw=new BufferedWriter(fw);
                    //String content=username+"\n"+pw;
                    bw.write(username);
                    bw.newLine();
                    bw.write(pw);
                    bw.newLine();
                    
                    //Create an empty archive file for the user
                    fw2=new FileWriter("archive\\"+username+"_archive.txt");
                    bw2=new BufferedWriter(fw2);
                    bw2.write("Filename"+"\t\t\t"+"Food?"+"\t\t"+"Confidence"+"\t\t"+"Date Tested");
                } catch (IOException ex) {
                    System.out.println("Something went wrong...");
                } finally {
                    closer(fw, bw);
                    closer(fw2, bw2);
                }
                createAccountMessage.setText("Your Account has been created.");
                createAccountMessage.setFill(Color.RED);
            } else {
                createAccountMessage.setText("Errors with creating your Account");
                createAccountMessage.setFill(Color.RED);
            }
        });
        
        gridPane.add(text, 0, 0);
        gridPane.add(userNameLabel, 0, 1);
        gridPane.add(userNameTextField, 0, 2);
        gridPane.add(repeatuserNameLabel, 0, 3);
        gridPane.add(repeatuserNameTextField, 0, 4);
        gridPane.add(passwordLabel, 0, 5);
        gridPane.add(passwordField, 0, 6);
        gridPane.add(repeatpasswordLabel, 0, 7);
        gridPane.add(repeatpasswordField, 0, 8);
        gridPane.add(createNewAccountButton, 0, 9);
        gridPane.add(createAccountMessage, 0, 10);

        Pane rootGroup = new VBox(50);
        rootGroup.getChildren().addAll(gridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));
        secondaryStage.setScene(new Scene(rootGroup));
        secondaryStage.show();
    }
    
    /**
     * Let's user log into an existing account.
     * @param tertiaryStage - A new window for the interface
     */
    private void login(Stage tertiaryStage) {
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
        final PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Your Password");
        
        final Text loginMessage = new Text();
        Button loginToAccountButton = new Button("Login");
        
        loginToAccountButton.setOnAction((ActionEvent event)->{ 
            
            FileReader fr=null;
            BufferedReader br=null;
            
            try {
                fr =new FileReader("users\\"+userNameTextField.getText()+"_info.txt");
                br=new BufferedReader(fr);
                
                String user=br.readLine();
                String pw=br.readLine();
                
                if(!pw.equals(passwordField.getText())){
                    loginMessage.setText("Incorrect password");
                    loginMessage.setFill(Color.RED);
                } else {
                    loginMessage.setText("Login successful");
                    loginMessage.setFill(Color.RED);
                    _user=user;
                }
                
            } catch (IOException ex) {
                loginMessage.setText("No such username");
                loginMessage.setFill(Color.RED);
            } finally {
               closer(fr, br);
            }
        });
        gridPane.add(text, 0, 0);
        gridPane.add(userNameLabel, 0, 1);
        gridPane.add(userNameTextField, 0, 2);
        gridPane.add(passwordLabel, 0, 3);
        gridPane.add(passwordField, 0, 4);
        gridPane.add(loginToAccountButton, 0, 5);
        gridPane.add(loginMessage, 0, 6);

        Pane rootGroup = new VBox(50);
        rootGroup.getChildren().addAll(gridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));
        tertiaryStage.setScene(new Scene(rootGroup));
        tertiaryStage.show();
    }
    
    /**
     * Wrapper function to use less lines closing FileWriter and BufferedWriter objects
     * @param fw - a FileWriter object
     * @param bw - a BufferedWriter object
     */
    private void closer(FileWriter fw, BufferedWriter bw){
        try {
            if (bw!=null){
                bw.close();
            }
            if (fw!=null){
                fw.close();
            }
        } catch (IOException ex) {
            System.out.println("Something went wrong");
        }
    }
    
    /**
     * Wrapper method for closing FileReader and BufferedReader objects
     * @param fr - a FileReader object
     * @param br - a BufferedReader object
     */
    private void closer(FileReader fr, BufferedReader br){
         try {
            if(fr!=null){
                fr.close();
            }
            if(br!=null){
                br.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Class to store the archival information of each image sent to SeeFood
     *
     * @author Sam
     */
    private class Archived {

        private String _filename;
        private double _leftval;
        private double _rightval;
        private Date _date;

        /**
         * Constructor
         *
         * @param filename - name of image file
         * @param food - is or is not food
         * @param con - confidence level of SeeFood
         * @param date - date these values were generated
         */
        public Archived(String filename, double left, double right, Date date) {
            _filename=filename;
            _leftval=left;
            _rightval=right;
            _date=date;
        }
        /**
         * Determines whether or not the archived image contained food. If return
         * is positive, it does. If not, it isn't.
         * @return whether or not image contains food.
         */
        private String isFood(){
            if(_leftval-_rightval>=0){
                return "Yes";
            } else {
                return "No";
            }
        }
        /**
         * Using the double value that was passed to the constructor, create a
         * string that reflects how sure SeeFood is of its decision
         * @return the confidence level as a string
         */
        private String confidenceLevel(){
            String str;
            double foodness=_leftval-_rightval;
            if(foodness>2.0 || foodness<-2.0){
                str="Strong";
            } else if(foodness>1.0 || foodness<-1.0){
                str="Medium";
            } else {
                str="Weak";
            }
            return str;
        }
        
        /**
         * Shortens longer image filenames so they don't ruin
         * the txt file's formatting
         * @return a potentially shorted filename
         */
        private String trimFileName(){
            if(_filename.length()>8){
                return _filename.substring(0, 7)+"...";
            }
            return _filename;
        }
        @Override
        /**
         * Overrides toString method to output contents of object, each separated by
         * 2 tabs.
         * @return a formatted string of the object's values
         */
        public String toString() {
            return trimFileName()+"\t\t\t"+isFood()+ "\t\t" +confidenceLevel()+"\t\t"+_date.toString();
        }
    }
}


