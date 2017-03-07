package app.controllers;

import app.models.Users;
import app.utils.QueryBuilder;
import app.utils.TableViewControls;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import javafx.scene.control.TableRow;
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

    QueryBuilder queryBooks = new QueryBuilder("books");
    QueryBuilder queryUsers = new QueryBuilder("users");
    QueryBuilder queryLoans = new QueryBuilder("loans");

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        tabUserList.setOnSelectionChanged(t -> twg.setTable(queryUsers.select().build(), tableUser));

//        tableUser.setOnMouseClicked(event -> twg.getRowId(tableUser));

        tableUser.setRowFactory(tv -> {
            TableRow<Users> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                    Users rowData = row.getItem();
                    System.out.println(row.getItem());
                    twg.getRowValue(tableUser, 0);
                }
            });
            return row;
        });

        tabBooksStaff.setOnSelectionChanged(event -> tabBooksStaff.setOnSelectionChanged(t -> twg.setTable(queryBooks.select().build(), tableBooksForStaff)));

        tableBooksForStaff.setOnMouseClicked(event -> twg.getRowValue(tableBooksForStaff, 0));






    }

    @FXML
    void generateTable(ActionEvent event) {
        twg.setTable(queryUsers.select().build(), tableUser);


    }

    @FXML
    void updateBookInfo(ActionEvent event) {


    }


}
