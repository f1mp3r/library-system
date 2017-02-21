package app.controllers;

import app.UserMethods;
import app.models.Users;
import app.utils.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;

/**
 * Created by Chris on 21.2.2017 г..
 */
public class UserAuthenticationController {

    @FXML
    private Button login;
    @FXML
    private TextField usertext;
    @FXML
    private PasswordField passwordtext;
    @FXML
    private Label label;
    @FXML
    private TableColumn<UserMethods, String> id;
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

    private String username;
    private String password;
    UserMethods userMethods = new UserMethods();
    Users users;
    Screen screen;

    public UserAuthenticationController() {
        screen = new Screen(this.getClass());
        users = new Users();
    }

    @FXML
    public void onLoginButtonClick(ActionEvent event) throws IOException {
        username = usertext.getText().toString();
        password = passwordtext.getText().toString();

        if (users.login(username, password)) {
            label.setText("Logged In");
            // todo: remove when not needed
            System.out.println("yay");
            ((Node) (event.getSource())).getScene().getWindow().hide();

            this.loadLibraryHomeScreenView();
        } else {
            label.setText("Invalid Login");
            // todo: remove when not needed
            System.out.println("nay");
        }
    }

    public void loadLibraryHomeScreenView() throws IOException {
        this.screen.loadView("MainScreen", "Navigation");
    }

    @FXML
    void onRegisterButtonClick(ActionEvent event) throws IOException {
        this.screen.loadView("RegScreen", "Registration");
    }

    @FXML
    void onRegisterSubmit(ActionEvent event) throws IOException {
        userMethods.addUser(reg1.getText(), reg6.getText(), reg2.getText(), reg3.getText(), reg4.getText(), reg5.getText(), false);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Registration was successful ");
        alert.showAndWait();
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
