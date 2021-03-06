package app.controllers;

import app.models.Books;
import app.models.Loans;
import app.models.Users;
import app.utils.InputValidation;
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

            Optional<String> result = Screen.makeSingleInputDialog(changeEmail, "Enter your new e-mail here.", "Email Change", "Email Change", "Please enter your new e-mail.");
            result.ifPresent(input -> {
                if (InputValidation.isValidEmailAddress(input) && InputValidation.lengthCheck(input)) {
                    HashMap updateData = new HashMap();
                    HashMap checkForEmail = users.getByColumn("email", input);
                    if (checkForEmail.size() == 0 || Integer.parseInt(checkForEmail.get("id").toString()) == Users.getLoggedInUserTableID()) {
                        updateData.put("email", ("'" + input + "'"));

                        if (1 == users.update(updateData, Users.getLoggedInUserTableID())) {
                            Screen.popup("INFORMATION", "You have successfully updated your e-mail address!");
                        }
                    } else {
                        Screen.popup("WARNING", "This e-mail is already in use.");
                    }
                } else {
                    Screen.popup("WARNING", InputValidation.getErrorList());
                    InputValidation.errorList.clear();
                }
            });
        });

        changePhone.setOnAction((ActionEvent a) -> {
            Optional<String> result = Screen.makeSingleInputDialog(changePhone, "Enter new phone number here.", "Phone Number Change", "Phone Number Change", "Please enter your new phone number.");

            result.ifPresent(input -> {
                if (InputValidation.isValidPhoneNumber(input) && InputValidation.lengthCheck(input)) {
                    HashMap updateData = new HashMap();
                    updateData.put("phone_number", "'" + input + "'");

                    if (1 == users.update(updateData, Users.getLoggedInUserTableID())) {
                        Screen.popup("INFORMATION", "You have successfully updated your phone number!");
                    }
                } else {
                    Screen.popup("WARNING", InputValidation.getErrorList());
                    InputValidation.errorList.clear();
                }
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
