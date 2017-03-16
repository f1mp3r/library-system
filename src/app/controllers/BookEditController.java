package app.controllers;

import app.models.Books;
import app.utils.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Ed on 3/16/17.
 */
public class BookEditController {


    @FXML
    private TextField bookTitle;

    @FXML
    private TextField bookAuthor;

    @FXML
    private TextField bookIsbn;

    @FXML
    private TextField bookLocation;

    @FXML
    private TextField bookCopies;


    @FXML
    void confirmBookButton(ActionEvent event) {

        Books books = new Books();
        HashMap newBook = new HashMap();
        newBook.put("title", bookTitle.getText());
        newBook.put("authors", bookAuthor.getText());
        newBook.put("isbn", bookIsbn.getText());
        newBook.put("location", bookLocation.getText());
        newBook.put("copies_in_stock",Integer.parseInt(bookCopies.getText()));
        System.out.println(newBook);
        books.insert(newBook);
        ((Node) (event.getSource())).getScene().getWindow().hide();
        

    }
}
