package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    private Button login;
    @FXML
    private TextField usertext;
    @FXML
    private PasswordField passwordtext;
    @FXML
    private Label label;
    private String username;
    private String password;
    @FXML private TableColumn<UserMethods, String> id;

    @FXML
    private TextField reg2;

    @FXML
    private TextField reg3;

    @FXML
    private TextField reg4;

    @FXML
    private TextField reg5;

    @FXML
    private TextField reg6;

    @FXML
    private TextField reg1;
    UserMethods userMethods = new UserMethods();



    @FXML
    public void loginuser(ActionEvent event) throws IOException {
        username = usertext.getText().toString();
        password = passwordtext.getText().toString();

        if (userMethods.login(usertext.getText(), passwordtext.getText())) {
            label.setText("Logged In");
            System.out.println("yay");
            ((Node) (event.getSource())).getScene().getWindow().hide();

            sample();
        } else {
            label.setText("Invalid Login");
            System.out.println("nay");
        }

    }



    public void sample() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(
                "../FXML's/MainScreen.fxml"));
        Stage errorStage = new Stage();
        errorStage.setScene(new Scene(root));
        errorStage.setTitle("Navigation");
        errorStage.centerOnScreen();
        errorStage.show();



    }

    @FXML
    void register(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource(
                "../FXML's/RegScreen.fxml"));
        Stage errorStage = new Stage();
        errorStage.setScene(new Scene(root));
        errorStage.setTitle("Registration");
        errorStage.centerOnScreen();
        errorStage.show();


    }

    @FXML
    void registerfinish(ActionEvent event) throws IOException {


        userMethods.addUser(reg1.getText(),reg6.getText(),reg2.getText(),reg3.getText(),reg4.getText(),reg5.getText(),false);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Registration was successful ");
        alert.showAndWait();
        ((Node) (event.getSource())).getScene().getWindow().hide();

    }



}



