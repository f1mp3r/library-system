package app.controllers;


import app.models.Users;
import app.utils.InputValidation;
import app.utils.Screen;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by Chris on 21.2.2017 г..
 */
public class UserAuthenticationController {

    @FXML
    private Button loginButtonx;
    @FXML
    private TextField studentIdField, staffusertext;
    @FXML
    private PasswordField passwordField, staffPasswordField;
    @FXML
    private Label infoBox;

    // todo: add descriptive names
    @FXML
    private TextField reg2, reg3, reg4, reg5, reg6, reg1;

    private String username;
    private String password;

    Users users;
    Screen screen;

    public UserAuthenticationController() {
        screen = new Screen(this.getClass());
        users = new Users();
        /*
        passwordField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                this.login(keyEvent, false);
            }
        });
        */
    }

    private void login(Event event, boolean admin) {
        this.username = admin ? staffusertext.getText().toString() : studentIdField.getText().toString();
        this.password = admin ? staffPasswordField.getText().toString() : passwordField.getText().toString();
        boolean login = admin ? this.users.loginStaff(this.username, this.password) : this.users.loginUser(this.username, this.password);

        if (login) {
            ((Node) (event.getSource())).getScene().getWindow().hide();

            try {
                this.loadLibraryHomeScreenView();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            this.infoBox.setText("Invalid Login");
        }
    }

    @FXML
    public void onLoginButtonClick(ActionEvent event) throws IOException {
        this.login(event, false);
    }

    @FXML
    public void onStaffLoginButtonClick(ActionEvent event) throws IOException {
        this.login(event, true);
    }

    public void loadLibraryHomeScreenView() throws IOException {
        this.screen.loadView("MainScreenUser", "Navigation");
    }

    public void loadLibraryHomeScreenViewForStaff() throws IOException {
        this.screen.loadView("MainScreenStaff", "Navigation");
    }

    @FXML
    void onRegisterButtonClick(ActionEvent event) throws IOException {
        this.screen.loadView("RegScreen", "Registration");
    }

    @FXML
    void onRegisterSubmit(ActionEvent event) throws IOException {
        if (InputValidation.isValidEmailAddress(reg5.getText()) && InputValidation.isValidPassword(reg6.getText())
                && !reg1.getText().isEmpty() && !reg2.getText().isEmpty()
                && !reg3.getText().isEmpty() && !reg4.getText().isEmpty()) {

            HashMap newUser = new HashMap();
            newUser.put("student_id", reg1.getText());
            newUser.put("password", reg6.getText());
            newUser.put("first_Name", reg2.getText());
            newUser.put("last_Name", reg3.getText());
            newUser.put("phone_number", reg4.getText());
            newUser.put("email", reg5.getText());
            newUser.put("permission", false);

            //check if email is in use
            int emailExistCheck = users.getByColumn("email", reg5.getText()).size();

            if(emailExistCheck == 0) {
                ///email not in use, continue
                users.insert(newUser);
                Screen.popup("CONFIRMATION", "Registration Successful");
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } else {
                //a user was found with that email
                Screen.popup("WARNING", "Email already in use");
            }
        } else {
            Screen.popup("ERROR", InputValidation.getErrorList());
            InputValidation.getErrorList().clear();
        }
    }

    @FXML
    void onMemberLogin(ActionEvent event) throws IOException {
        this.screen.loadView("UserLogin", "Registration");
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void onStaffLogin(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        this.screen.loadView("StaffLogin", "StaffLogin");
    }
}
