package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ControllerForTable implements Initializable {

    @FXML
    private TableView<UserDetails> tableUser;

    @FXML
    private TableColumn<UserDetails, String> c1;

    @FXML
    private TableColumn<UserDetails, String> c2;

    @FXML
    private TableColumn<UserDetails, String> c3;

    @FXML
    private TableColumn<UserDetails, String> c4;

    @FXML
    private TableColumn<UserDetails, String> c5;

    @FXML
    private TableColumn<UserDetails, String> c6;
    @FXML
    private TableColumn<UserDetails, String> c7;


    @FXML
    private TextField idfield;

    @FXML
    private TextField passwordfield;

    @FXML
    private TextField firstnamefield;

    @FXML
    private TextField lastnamefield;

    @FXML
    private TextField phonenumberfield;

    @FXML
    private TextField emailfield;
    private Button btnLoad;

    private ObservableList<UserDetails> data;

    UserMethods userMethods = new UserMethods();
    Connection dc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }

    @FXML
    void loadDataFromDatabase(ActionEvent event) {
        Connection conn = userMethods.getConnection();
        data = FXCollections.observableArrayList();

        try {
            ResultSet rs = conn.createStatement().executeQuery("select * from users");
            while (rs.next()) {
                data.add(new UserDetails(rs.getString("studentid"), rs.getString("password"),
                        rs.getString("firstname"), rs.getString("lastname"), rs.getString("phonenumber"), rs.getString("email"), rs.getBoolean("permission")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        c1.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        c2.setCellValueFactory(new PropertyValueFactory<>("password"));
        c3.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        c4.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        c5.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        c6.setCellValueFactory(new PropertyValueFactory<>("email"));
        c7.setCellValueFactory(new PropertyValueFactory<>("permission"));

        tableUser.setItems(null);
        tableUser.setItems(data);


        tableUser.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                String gottenId = tableUser.getSelectionModel().getSelectedItem().getStudentID();
                String gottenpassword = tableUser.getSelectionModel().getSelectedItem().getPassword();
                String gottenfirstname = tableUser.getSelectionModel().getSelectedItem().getFirstName();
                String gottenlastname = tableUser.getSelectionModel().getSelectedItem().getLastName();
                String gottenphonenumber = tableUser.getSelectionModel().getSelectedItem().getPhoneNumber();
                String gottenemail = tableUser.getSelectionModel().getSelectedItem().getEmail();
                idfield.setText(gottenId);
                passwordfield.setText(gottenpassword);
                firstnamefield.setText(gottenfirstname);
                lastnamefield.setText(gottenlastname);
                phonenumberfield.setText(gottenphonenumber);
                emailfield.setText(gottenemail);


            }
        });


    }


    @FXML
    void updateInfo(ActionEvent event) {

        try {
            Connection myConn;
            UserMethods userMethods = new UserMethods();
            myConn = userMethods.getConnection();


            String query = "UPDATE users SET studentid = ?, "
                    + "password = ?, "
                    + "firstname = ?, "
                    + "lastname = ?, "
                    + "phonenumber = ?, "
                    + "email = ? "
                    + "WHERE studentid = ?";

            PreparedStatement pst;
            pst = myConn.prepareStatement(query);
            pst.setString(1, idfield.getText());
            pst.setString(2, passwordfield.getText());
            pst.setString(3, firstnamefield.getText());
            pst.setString(4, lastnamefield.getText());
            pst.setString(5, phonenumberfield.getText());
            pst.setString(6, emailfield.getText());
            pst.setString(7, idfield.getText());
            pst.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    void removeUser(ActionEvent event) {

        try {
            Connection myConn;
            UserMethods userMethods = new UserMethods();
            myConn = userMethods.getConnection();


            String query = "delete from users where studentID=?";
            PreparedStatement pst;
            pst = myConn.prepareStatement(query);
            pst.setString(1, idfield.getText());
            pst.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


