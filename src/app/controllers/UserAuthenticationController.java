package app.controllers;


import app.models.Loans;
import app.models.Users;
import app.utils.InputValidation;
import app.utils.Screen;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLDataException;
import java.util.HashMap;

/**
 * Created by Chris on 21.2.2017 г..
 */
public class UserAuthenticationController {

    private Users users;
    private Screen screen;
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
    private TextField firlstNameField, lastNameField, phoneNumberField, emailField, registerPasswordField, registerStudentIdField;
    private String username;
    private String password;

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
            Loans loans = new Loans();
            try {
                if (admin) {
                    this.loadLibraryHomeScreenViewForStaff();
                } else {
                    this.loadLibraryHomeScreenView();

                    if (loans.checkIfDue(Users.getLoggedInUserTableID())) {
                        Screen.popup("WARNING", "You have Books Overdue");
                    }
                }
            } catch (IOException e) {
                Screen.exception(e);
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
    void onRegisterSubmit(ActionEvent event) throws SQLDataException {
        HashMap newUser = new HashMap();
        newUser.put("student_id", registerStudentIdField.getText());
        newUser.put("password", registerPasswordField.getText());
        newUser.put("first_Name", firlstNameField.getText());
        newUser.put("last_Name", lastNameField.getText());
        newUser.put("phone_number", phoneNumberField.getText());
        newUser.put("email", emailField.getText());
        newUser.put("permission", this.users.getCount() == 0); // if there are no users, the first user is an admin

        if (this.users.validate(newUser)) {
            // check if email is in use
            int emailExistCheck = users.getByColumn("email", emailField.getText()).size();
            HashMap checkStudentID = users.getByColumn("student_id", registerStudentIdField.getText());

            if (emailExistCheck == 0 & (!checkStudentID.containsKey("student_id"))) {
                // email not in use, continue
                users.insert(newUser);
                Screen.popup("CONFIRMATION", "Registration Successful");
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } else if (checkStudentID.containsKey("student_id")) {
                Screen.popup("WARNING", "Student-ID already in use");
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
