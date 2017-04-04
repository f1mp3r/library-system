package app.controllers;

import app.models.Books;
import app.models.Users;
import app.utils.QueryBuilder;
import app.utils.Screen;
import app.utils.TableViewControls;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.awt.print.Book;
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
    private TableView<?> tableUsers;
    @FXML
    private TableView tableBooks;

    @FXML
    private TextField searchFieldUsers;

    private Screen screen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.screen = new Screen(this.getClass());
        this.refreshTable(false);
        books.refreshTable(twg, tableBooks, searchFieldBooks, false);
        tabBooks.setOnSelectionChanged(t -> books.refreshTable(twg, tableBooks, searchFieldBooks, false));
        ContextMenu menu = new ContextMenu();
        MenuItem removeFineContextMenuItem = new MenuItem("Remove fine");
        MenuItem makeAdminContextMenuItem = new MenuItem("Upgrade user to admin");
        MenuItem removeAdminContextMenuItem = new MenuItem("Remove admin privileges");
        menu.getItems().addAll(removeFineContextMenuItem, makeAdminContextMenuItem, removeAdminContextMenuItem);
        tableUsers.setContextMenu(menu);

        searchFieldUsers.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                this.refreshTable(false);
            }
        });

        searchFieldUsers.setOnKeyReleased(keyEvent -> {
            this.refreshTable(false);
        });

        this.refreshTable(false);

        searchFieldBooks.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                books.refreshTable(twg, tableBooks, searchFieldBooks, false);
            }
        });

        searchFieldBooks.setOnKeyReleased(keyEvent -> {
            books.refreshTable(twg, tableBooks, searchFieldBooks, false);
        });

        tableBooks.setRowFactory(tv -> {
            TableRow<Books> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    //edit selected Book

                    int bookId = Integer.parseInt(twg.getRowValue(tableBooks, 0));
                    HashMap bookData = this.books.getById(bookId);
                    Dialog dialog = Screen.dialogEditBook("Edit book", "Edit: \"" + bookData.get("title").toString() + "\"");
                    this.setupEditFields(dialog, bookData);
                }
            });

            return row;
        });

        //right click on user on staff user list. Option Remove Fine;
        removeFineContextMenuItem.setOnAction(event -> {
            Optional<ButtonType> result = Screen.popup("CONFIRMATION", "Are you sure you want to remove the fine for student " + Users.getLoggedInUserName());

            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                HashMap fine = new HashMap();
                Users users = new Users();
                fine.put("debt", "0");
                int row = Integer.parseInt(twg.getRowValue(tableUsers, 0));
                users.update(fine, row);
                this.refreshTable(false);
            }
        });

        //right click on user on staff user list. Option Make administrator
        makeAdminContextMenuItem.setOnAction(event -> {
            Optional<ButtonType> result = Screen.popup("CONFIRMATION", "Are you sure you want to make " + Users.getLoggedInUserName() + " an admin?");

            if (result.get() == ButtonType.OK) {
                HashMap fine = new HashMap();
                Users users = new Users();
                fine.put("permission", "1");
                int row = Integer.parseInt(twg.getRowValue(tableUsers, 0));
                users.update(fine, row);
                this.refreshTable(false);
            }
        });

        removeAdminContextMenuItem.setOnAction(event -> {
            Optional<ButtonType> result = Screen.popup("CONFIRMATION", "Are you sure you want to remove admin privileges from " + Users.getLoggedInUserName());

            if (result.get() == ButtonType.OK) {
                HashMap fine = new HashMap();
                Users users = new Users();
                fine.put("permission", "0");
                int row = Integer.parseInt(twg.getRowValue(tableUsers, 0));
                users.update(fine, row);
                this.refreshTable(false);
            }
        });
    }


    @FXML
    void addBook(ActionEvent event) {
        Dialog dialog = Screen.dialogEditBook("Add book", "Add new book to the Library");
        this.setupEditFields(dialog, new HashMap());
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
            Screen.exception(e);
        }
    }

    public void setupEditFields(Dialog dialog, HashMap bookData) {
        // Sets the button types.
        ButtonType addButtontype = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtontype, ButtonType.CANCEL);

        // Creates TextFields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField title = new TextField();
        title.setPromptText("Book Title");
        if (!bookData.isEmpty()) {
            title.setText(bookData.get("title").toString());
        }

        TextField author = new TextField();
        author.setPromptText("Book Author");
        if (!bookData.isEmpty()) {
            author.setText(bookData.get("authors").toString());
        }

        TextField location = new TextField();
        location.setPromptText("Location");
        if (!bookData.isEmpty()) {
            location.setText(bookData.get("location").toString());
        }

        TextField copies = new TextField();
        copies.setPromptText("Copies");
        if (!bookData.isEmpty()) {
            copies.setText(bookData.get("copies_in_stock").toString());
        }

        grid.add(new Label("Title:"), 0, 0);
        grid.add(title, 1, 0);

        grid.add(new Label("Author:"), 0, 1);
        grid.add(author, 1, 1);

        TextField isbn = new TextField();
        if (bookData.isEmpty()) {
            isbn.setPromptText("ISBN");
            grid.add(new Label("ISBN:"), 0, 2);
            grid.add(isbn, 1, 2);
        }

        grid.add(new Label("Location:"), 0, 3);
        grid.add(location, 1, 3);

        grid.add(new Label("Copies:"), 0, 4);
        grid.add(copies, 1, 4);


        // Activate edit button when all fields have text
        Node addButton = dialog.getDialogPane().lookupButton(addButtontype);
        BooleanBinding booleanBind = title.textProperty().isEmpty()
                .or(author.textProperty().isEmpty())
                .or(location.textProperty().isEmpty())
                .or(copies.textProperty().isEmpty());

        addButton.disableProperty().bind(booleanBind);

        dialog.getDialogPane().setContent(grid);
        dialog.show();

        addButton.addEventFilter(ActionEvent.ACTION, clickEvent -> {
            try {
                if (this.books.getByColumn("isbn", isbn.getText()).isEmpty()) {
                    HashMap bookChange = new HashMap();
                    if (bookData.isEmpty()) {
                        bookChange.put("isbn", isbn.getText());
                        bookChange.put("title", title.getText());
                        bookChange.put("authors", author.getText());
                        bookChange.put("location", location.getText());
                        bookChange.put("copies_in_stock", Integer.parseInt(copies.getText()));
                    } else {
                        bookChange.put("title", QueryBuilder.escapeValue(title.getText()));
                        bookChange.put("authors", QueryBuilder.escapeValue(author.getText()));
                        bookChange.put("location", QueryBuilder.escapeValue(location.getText()));
                        bookChange.put("copies_in_stock", Integer.parseInt(copies.getText()));
                    }

                    if (bookData.isEmpty()) {
                        this.books.insert(bookChange);
                    } else {
                        this.books.update(bookChange, Integer.parseInt(bookData.get("id").toString()));
                    }

                    QueryBuilder queryBooks = new QueryBuilder("books");
                    twg.setTable(queryBooks.select(Books.memberVisibleFields).build(), tableBooks);

                } else {
                    Screen.popup("WARNING", "The ISBN typed in already exists, please edit the existing entry.");
                    clickEvent.consume();
                }

            } catch (NumberFormatException e) {
                Screen.popup("WARNING", "The 'copies' field should contain a number.");
                clickEvent.consume();
            }
        });

        Platform.runLater(() -> title.requestFocus());
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
}
