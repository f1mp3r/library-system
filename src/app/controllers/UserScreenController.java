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
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Thez on 2/24/2017.
 */
public class UserScreenController implements Initializable {

    TableViewControls twg = new TableViewControls();
    Users users = new Users();
    Loans loans = new Loans();
    Books books = new Books();
    @FXML
    private TableView tableBooks;
    @FXML
    private TableView userLoansTable;
    @FXML
    private Tab tabBooks;
    @FXML
    private Label idLabel;
    @FXML
    private Label loggedInName;
    @FXML
    private TextField searchfield;

    QueryBuilder queryBooks = new QueryBuilder("books");
    QueryBuilder queryUsers = new QueryBuilder("users");
    QueryBuilder queryLoans = new QueryBuilder("loans");


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tabBooks.setOnSelectionChanged(t -> twg.setTable( queryBooks.select().build(), tableBooks));
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


    }


    @FXML
    void onLoadPress(ActionEvent event) {
        twg.setTable(queryBooks.select().build(), tableBooks);

        System.out.println(queryBooks.select().build());
    }

    @FXML
    void refreshUserLoanTable(ActionEvent event) {

        twg.setTable(queryLoans.select().where("student_id", users.getLoggedInStudentId()).where("returned", "no").build(), userLoansTable);
    }

    @FXML
    void searchbutton(ActionEvent event) {
        String searchKey = "%"+searchfield.getText()+"%";
        try{
        twg.setTable(queryBooks.select().where("title", "LIKE", searchKey).orWhere("authors", "LIKE", searchKey).build(), tableBooks);

        System.out.println(queryBooks.select().build());
    }catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
        }
        }

}
