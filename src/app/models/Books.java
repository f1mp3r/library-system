package app.models;

import app.utils.Screen;
import app.utils.TableViewControls;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Thez on 2/25/2017.
 */
public class Books extends Model {
    public Books() {
        super();
        this.table = "books";
    }


    public void borrowBook(TableView tableBooks) {
        Loans loans = new Loans();
        TableViewControls twg = new TableViewControls();
         //Gets required values(mostly from tableview)
        int id = Integer.parseInt(twg.getRowValue(tableBooks, 0));
        int copiesAvailable = Integer.parseInt(twg.getRowValue(tableBooks, 4));
        int loaned = loans.getOnLoanCount(Users.getLoggedInStudentId());
        String bookTitle = twg.getRowValue(tableBooks, 2);
        String isbn = twg.getRowValue(tableBooks, 1).toString();


        //If borrow limit is not exceeded and if book is in stock
        if (loaned <= 10 && copiesAvailable >= 1) {
            //Display a popup box asking to confirm choice
            Optional<ButtonType> result = Screen.popup("CONFIRMATION", "You are about to borrow:" + "\n" + bookTitle + "\n" + "Is this correct?");
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                // create a loan record for the logged in user and update book info
                loans.insert(new HashMap() {
                    {
                        put("student_id", Users.getLoggedInStudentId());
                        put("isbn", isbn);
                        put("date_borrowed", date);
                    }
                });
                Books books = new Books();
                HashMap<String, String> bookupdate = new HashMap<>();
                bookupdate.put("copies_in_stock", "copies_in_stock -1");
                bookupdate.put("currently_on_loan", "currently_on_loan +1");
                books.update(bookupdate, id);
                twg.setTable("books", tableBooks);
                //end of loan creation
            } else {
                // ... user chose CANCEL or close
            }

            //Display messages that you have reached maximum borrow limit or if there is no books available
        } else if (loaned >= 10 & copiesAvailable >= 1) {
            Screen.popup("INFORMATION", "You are already borrowing the maximum amount of books allowed.");
            System.out.println("potato");
        } else {
            Screen.popup("INFORMATION", "There are no books available");

        }

    }
}