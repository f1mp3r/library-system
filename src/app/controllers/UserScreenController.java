package app.controllers;

import app.models.Books;
import app.models.Loans;
import app.models.Users;
import app.utils.QueryBuilder;
import app.utils.Screen;
import app.utils.TableViewControls;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Created by Thez on 2/24/2017.
 */
public class UserScreenController implements Initializable {

    TableViewControls tableViewControls = new TableViewControls();
    Users users = new Users();
    Loans loans = new Loans();
    Books books = new Books();
    QueryBuilder queryBooks = new QueryBuilder("books");
    @FXML
    private TableView tableBooks, userLoansTable;
    @FXML
    private Tab tabBooks;
    @FXML
    private Label idLabel, loggedInName;
    @FXML
    private TextField searchField;
    @FXML
    private MenuItem changePhone;
    @FXML
    private MenuItem changeEmail;
    @FXML
    private Tab profileTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeEmail.setOnAction((ActionEvent a) -> {

            Optional<String> result = Screen.makeSingleInputDialog(changeEmail, "Enter New Email here", "Email Change", "Email Change", "Please enter your new Email");
            result.ifPresent(input -> {

                HashMap updateData = new HashMap();
                int checkForEmail = users.getByColumn("email",input).size();
                if(checkForEmail==0) {
                    updateData.put("email", ("'" + input + "'"));
                    users.update(updateData, Users.getLoggedInUserTableID());
                }else{
                    Screen.popup("WARNING", "Sorry, this email is  already in use");
                }
            });


        });

        changePhone.setOnAction((ActionEvent a) -> {

            Optional<String> result = Screen.makeSingleInputDialog(changePhone, "Enter New Phone number here", "Phone Number Change", "Phone Number Change", "Please enter your new Phone Number");
            result.ifPresent(input -> {
                HashMap updateData = new HashMap();
                updateData.put("phone_number", ("'" + input + "'"));
                users.update(updateData, Users.getLoggedInUserTableID());

            });


        });

        tableViewControls.setTable(this.users.getLoanedBooksQuery(), userLoansTable);
        profileTab.setOnSelectionChanged(t -> tableViewControls.setTable(this.users.getLoanedBooksQuery(), userLoansTable));
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
            if (keyEvent.getCode() == KeyCode.ENTER) {
                this.books.refreshTable(tableViewControls, tableBooks, searchField, false);
            }
        });

        searchField.setOnKeyReleased(keyEvent -> {
            this.books.refreshTable(tableViewControls, tableBooks, searchField, false);
        });

        // load the table initially
        this.books.refreshTable(tableViewControls, tableBooks, searchField, false);
        this.refreshLoanedBooks();
    }

    @FXML
    void searchButton(ActionEvent event) {

    }

    @FXML
    void onLoadPressUsers(ActionEvent event) {
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
    void searchButtonUsers(ActionEvent event) {
        this.books.refreshTable(tableViewControls, tableBooks, searchField, false);
    }
}
