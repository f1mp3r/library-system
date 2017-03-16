package app.models;

import app.utils.QueryBuilder;
import app.utils.Screen;
import app.utils.TableViewControls;
import com.mysql.jdbc.PreparedStatement;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by Thez on 2/25/2017.
 */
public class Books extends Model {
    public static String[] memberVisibleFields = new String[]{
            "@books.id as `#`",
            "@isbn as `ISBN`",
            "@title as `Title`",
            "@authors as `Authors`",
            "@location as `Location in library`",
            "@copies_in_stock `Copies available`"
    };

    public Books() {
        super();
        this.table = "books";
    }

    public void borrowBook(TableView tableBooks) {
        Books books = new Books();
        HashMap<String, String> bookUpdate = new HashMap<>();

        Loans loans = new Loans();
        TableViewControls twg = new TableViewControls();
        QueryBuilder queryBooks = new QueryBuilder("books");

        //Gets required values(mostly from tableview)

        int bookId = Integer.parseInt(twg.getRowValue(tableBooks, 0));
        HashMap bookInfo = books.getById(bookId);

        int copiesAvailable = Integer.parseInt(bookInfo.get("copies_in_stock").toString());
        int loaned = loans.getOnLoanCount(String.valueOf(Users.getLoggedInUserTableID()));
        String bookTitle = bookInfo.get("title").toString();

        boolean checkForSameBook = loans.checkForSameBook(String.valueOf(Users.getLoggedInUserTableID()), String.valueOf(bookId));

        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        LocalDate due = date.toLocalDate().plusDays(8);

        //If borrow limit is not exceeded and if book is in stock
        if ((loaned <= 10) && copiesAvailable >= 1 && checkForSameBook) {
            //Display a popup box asking to confirm choice
            Optional<ButtonType> result = Screen.popup("CONFIRMATION", "You are about to borrow:" + "\n" + bookTitle + "\n" + "Is this correct?");
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                // create a loan record for the logged in user and update book info
                loans.insert(new HashMap() {
                    {
                        put("user_id", Users.getLoggedInUserTableID());
                        put("book_id", bookId);
                        put("date_borrowed", date);
                        put("date_due", due);
                    }
                });

                bookUpdate.put("copies_in_stock", "copies_in_stock -1");
                bookUpdate.put("currently_on_loan", "currently_on_loan +1");
                books.update(bookUpdate, bookId);
                twg.setTable(queryBooks.select(Books.memberVisibleFields).build(), tableBooks);
                //end of loan creation
            }
        } else if (loaned >= 10 & copiesAvailable >= 1) {
            //Display messages that you have reached maximum borrow limit or if there is no books available
            Screen.popup("INFORMATION", "You are already borrowing the maximum amount of books allowed.");
        } else if (!checkForSameBook) {
            Screen.popup("INFORMATION", "You are already borrowing a copy of this book");
        } else {
            Screen.popup("INFORMATION", "There are no books available");
        }
    }

    public void returnBook(TableView userLoansTable) {
        Users users = new Users();
        Books books = new Books();
        Loans loans = new Loans();
        TableViewControls twg = new TableViewControls();
        QueryBuilder queryLoans = new QueryBuilder("loans");

        int bookId = Integer.parseInt(twg.getRowValue(userLoansTable, 0));
        HashMap loanInfo = loans.getByUserAndBook(String.valueOf(Users.getLoggedInUserTableID()), String.valueOf(bookId));
        HashMap book = books.getById(bookId);

        String bookTitle = book.get("title").toString();
        int getBookId = Integer.parseInt(books.getById(bookId).get("id").toString());

        HashMap updateLoans = new HashMap<>();
        HashMap<String, String> bookUpdate = new HashMap<>();
        HashMap<String, String> userAddDebt = new HashMap<>();

        Date date = new Date(Calendar.getInstance().getTime().getTime());

        Optional<ButtonType> result = Screen.popup("CONFIRMATION", "You are about to return:" + "\n" + bookTitle + "\n" + "Do you wish to proceed?");

        if (result.get() == ButtonType.OK) {
            // set the loan record to returned and update book stock
            updateLoans.put("returned", "'1'");
            updateLoans.put("date_returned", "'" + date.toLocalDate() + "'");
            loans.update(updateLoans, Integer.parseInt(loanInfo.get("id").toString()));

            bookUpdate.put("copies_in_stock", "copies_in_stock +1");
            bookUpdate.put("currently_on_loan", "currently_on_loan -1");
            books.update(bookUpdate, getBookId);

            //check if book was overdue, if so add debt to the user.
            HashMap dates = loans.getByColumn("id", Integer.toString(bookId));
            LocalDate borrowDate = LocalDate.parse(dates.get("date_borrowed").toString());
            LocalDate returnedDate = LocalDate.parse(dates.get("date_returned").toString());
            long daysPassed = DAYS.between(borrowDate, returnedDate);

            if (daysPassed > 8) {
                userAddDebt.put("debt", "8.00");
                users.update(userAddDebt, users.getLoggedInUserTableID());

                Screen.popup("WARNING", "A fine has been applied to your account for Overdue Books \n" +
                        "You can pay and get it removed at the Library Desk");

                //refresh table
            } else {
                Screen.popup("INFORMATION", "Thanks! Please place the book on the trolley");
            }
        }

        twg.setTable(users.getLoanedBooksQuery(), userLoansTable);
    }

    public void refreshTable(TableViewControls table, TableView tableView, TextField searchField, boolean forceEmpty) {
        String searchKey = "%" + searchField.getText() + "%";
        QueryBuilder queryBooks = new QueryBuilder("books");

        try {
            QueryBuilder query = queryBooks.select(Books.memberVisibleFields);
            if (!searchField.getText().isEmpty() && !forceEmpty) {
                query.where("title", "LIKE", searchKey).orWhere("authors", "LIKE", searchKey);
            }

            table.setTable(query.build(), tableView);
        } catch (java.lang.Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void addBook(TableView tableBooks) {

        final boolean[] notANumber = {false};

        Dialog dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");
// Set the button types.
        ButtonType addButtontype = new ButtonType("Add Book", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtontype, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField title = new TextField();
        title.setPromptText("Book Title");

        TextField author = new TextField();
        author.setPromptText("Book Author");

        TextField isbn = new TextField();
        isbn.setPromptText("ISBN");

        TextField location = new TextField();
        location.setPromptText("Location");

        TextField copies = new TextField();
        copies.setPromptText("Copies");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(title, 1, 0);

        grid.add(new Label("Author:"), 0, 1);
        grid.add(author, 1, 1);

        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(isbn, 1, 2);

        grid.add(new Label("Location:"), 0, 3);
        grid.add(location, 1, 3);

        grid.add(new Label("Copies:"), 0, 4);
        grid.add(copies, 1, 4);

//        Activate add button when all fields have text
        Node addButton = dialog.getDialogPane().lookupButton(addButtontype);
        BooleanBinding booleanBind = title.textProperty().isEmpty()
                .or(author.textProperty().isEmpty())
                .or(isbn.textProperty().isEmpty())
                .or(location.textProperty().isEmpty())
                .or(copies.textProperty().isEmpty());

        addButton.disableProperty().bind(booleanBind);

        dialog.getDialogPane().setContent(grid);
        dialog.show();
        //focus on title when dialog opens
        Platform.runLater(() -> title.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtontype) {
                System.out.println(title.getText());
                Books books = new Books();
                HashMap newBook = new HashMap();
                newBook.put("title", title.getText());
                newBook.put("authors", author.getText());
                newBook.put("isbn", isbn.getText());
                newBook.put("location", location.getText());
                try {
                newBook.put("copies_in_stock", Integer.parseInt(copies.getText()));
                } catch (NumberFormatException e) {
                       notANumber[0] = true;

                }
                // insert book if duplicate isbn does not exist else display a warning
                if (isbnCheck(isbn.getText()) == 0 && notANumber[0] == false) {

                        books.insert(newBook);
                        QueryBuilder queryBooks = new QueryBuilder("books");
                        TableViewControls twg = new TableViewControls();
                        twg.setTable(queryBooks.select(Books.memberVisibleFields).build(), tableBooks);

                }else if(notANumber[0] == true){
                    Screen.popup("WARNING", "The copies field should contain a number");

                }
                else {
                    Screen.popup("WARNING", "A Book with the same isbn already exists");
                }

            }
            return null;
        });

    }

    // if isbnCheck returns more than 0, a book already exists in the table
    public int isbnCheck(String isbn) {
        int rowCount = 0;

        try {
            String countQuery = String.format("select count(*) from books where isbn = '%s'", isbn);
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(countQuery);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            rowCount = resultSet.getInt("count(*)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rowCount;
    }
}