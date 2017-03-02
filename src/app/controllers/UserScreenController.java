package app.controllers;

import app.models.Books;
import app.models.Loans;
import app.models.Users;
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
    private Tab tabBooks;
    @FXML
    private Label idLabel;
    @FXML
    private Label loggedInName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tabBooks.setOnSelectionChanged(t -> twg.setTable("books", tableBooks));
        idLabel.setText(Integer.toString(users.getLoggedInUserID()));
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

        tableBooks.setOnMouseClicked(event -> {


        });


    }


    @FXML
    void onLoadPress(ActionEvent event) {
        twg.setTable("books", tableBooks);

    }
}
