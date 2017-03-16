package app.controllers;

import app.models.Books;
import app.models.Users;
import app.utils.QueryBuilder;
import app.utils.Screen;
import app.utils.TableViewControls;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffScreenController implements Initializable {


    QueryBuilder queryUsers = new QueryBuilder("users");
    TableViewControls twg = new TableViewControls();
    Books books = new Books();



    @FXML
    private Tab tabBooks;

    @FXML
    private TextField searchFieldBooks;

    @FXML
    private Tab tabUsers;

    @FXML
    private TableView<?> tableUsers;
    @FXML
    private TableView  tableBooks;

    @FXML
    private TextField searchFieldUsers;

    private Screen screen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.screen = new Screen(this.getClass());

        searchFieldUsers.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                this.refreshTable(false);
            }
        });

        searchFieldUsers.setOnKeyReleased(keyEvent -> {
            this.refreshTable(false);
        });

        this.refreshTable(false);


        searchFieldBooks.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                books.refreshTable(twg, tableBooks, searchFieldBooks, false);
            }
        });

        searchFieldBooks.setOnKeyReleased(keyEvent -> {
            books.refreshTable(twg, tableBooks, searchFieldBooks, false);
        });

        this.refreshTable(false);
        books.refreshTable(twg, tableBooks, searchFieldBooks, false);


        tableBooks.setRowFactory(tv -> {
            TableRow<Books> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    //edit selected Book

                }
            });

            return row;
        });

    }

    @FXML
    void onLoadPressUsers(ActionEvent event) {

    }

    @FXML
    void searchButtonUsers(ActionEvent event) {

    }



    @FXML
    void clearButtonBooks(ActionEvent event) {

    }

    @FXML
    void searchButtonBooks(ActionEvent event) {

    }

    @FXML
    void addBook(ActionEvent event) {
        books.addBook(tableBooks);


    }


    private void refreshTable(boolean forceEmpty) {
        String searchKey = "%" + searchFieldUsers.getText() + "%";

        try {
            QueryBuilder query = queryUsers.select(Users.memberVisibleFields);
            if (!searchFieldUsers.getText().isEmpty() && !forceEmpty) {
                query.where("first_name", "LIKE", searchKey).orWhere("last_name", "LIKE", searchKey).orWhere("student_id", "LIKE", searchKey).orWhere("email", "LIKE", searchKey);
            }

            twg.setTable(query.build(), tableUsers);
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
