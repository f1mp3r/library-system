package app.controllers;

import app.utils.TableViewControls;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Thez on 2/23/2017.
 */


public class StaffScreenController implements Initializable {

    TableViewControls twg = new TableViewControls();

    @FXML
    private TextField bookidtextfield;

    @FXML
    private TextField booktitlefield;

    @FXML
    private TextField bookauthorfield;

    @FXML
    private TextField bookisbnfield;

    @FXML
    private TextField booklocationfield;

    @FXML
    private TextField bookcopiesfield;
    @FXML
    private TableView tableUser;
    @FXML
    private Tab tabUserList;
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

    @FXML
    private TableView tableBooksForStaff;
    @FXML
    private Tab tabBooksStaff;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        tabUserList.setOnSelectionChanged(t -> twg.setTable("users", tableUser));

        tableUser.setOnMouseClicked(event -> twg.getRowId(tableUser));

        tabBooksStaff.setOnSelectionChanged(event -> tabBooksStaff.setOnSelectionChanged(t -> twg.setTable("books", tableBooksForStaff)));

        tableBooksForStaff.setOnMouseClicked(event -> twg.getRowId(tableBooksForStaff));






    }

    @FXML
    void generateTable(ActionEvent event) {
        twg.setTable("users", tableUser);


    }

    @FXML
    void updateBookInfo(ActionEvent event) {


    }


}
