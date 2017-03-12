package app.controllers;

import app.models.Books;
import app.models.Loans;
import app.models.Users;
import app.utils.QueryBuilder;
import app.utils.TableViewControls;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Thez on 2/24/2017.
 */
public class UserScreenController implements Initializable {

    TableViewControls tableViewControls = new TableViewControls();
    Users users = new Users();
    Loans loans = new Loans();
    Books books = new Books();
    @FXML
    private TableView tableBooks, userLoansTable;
    @FXML
    private Tab tabBooks;
    @FXML
    private Label idLabel, loggedInName;
    @FXML
    private TextField searchField;

    QueryBuilder queryBooks = new QueryBuilder("books"),
            queryUsers = new QueryBuilder("users"),
            queryLoans = new QueryBuilder("loans");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabBooks.setOnSelectionChanged(t -> tableViewControls.setTable(queryBooks.select(Books.memberVisibleFields).build(), tableBooks));
        idLabel.setText(Integer.toString(users.getLoggedInUserTableID()));
        loggedInName.setText(users.getLoggedInUserName());

        tableBooks.setRowFactory(tv -> {
            TableRow<Books> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    //Borrow selected Book
                    books.borrowBook(tableBooks);
                }
            });

            return row;
        });

        userLoansTable.setRowFactory(tv -> {
            TableRow<Books> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    //return selected Book
                    System.out.println("Click return");
                    books.returnBook(userLoansTable);
                }
            });

            return row;
        });

        searchField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                this.refreshTable(false);
            }
        });

        // load the table initially
        this.refreshTable(false);
        this.refreshLoanedBooks();
    }

    @FXML
    void onLoadPress(ActionEvent event) {
        tableViewControls.setTable(queryBooks.select(Books.memberVisibleFields).build(), tableBooks);
    }

    @FXML
    void refreshUserLoanTable(ActionEvent event) {
        this.refreshLoanedBooks();
    }

    private void refreshLoanedBooks() {
        tableViewControls.setTable(this.users.getLoanedBooksQuery(), userLoansTable);
    }

    @FXML
    void searchButton(ActionEvent event) {
        this.refreshTable(false);
    }

    private void refreshTable(boolean forceEmpty) {
        String searchKey = "%" + searchField.getText() + "%";

        try {
            QueryBuilder query = queryBooks.select(Books.memberVisibleFields);
            if (!searchField.getText().isEmpty() && !forceEmpty) {
                query.where("title", "LIKE", searchKey).orWhere("authors", "LIKE", searchKey);
            }

            tableViewControls.setTable(query.build(), tableBooks);
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
