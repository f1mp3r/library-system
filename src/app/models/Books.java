package app.models;

import app.utils.Screen;
import app.utils.TableViewControls;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by Thez on 2/25/2017.
 */
public class Books extends Model {

    public Books() {
        super();
        this.table = "books";
    }


    public void borrowBook(TableView tableBooks) {
        Books books = new Books();
        HashMap<String, String> bookupdate = new HashMap<>();
        Loans loans = new Loans();
        TableViewControls twg = new TableViewControls();
        //Gets required values(mostly from tableview)
        int bookId = Integer.parseInt(twg.getRowValue(tableBooks, 0));
        int copiesAvailable = Integer.parseInt(twg.getRowValue(tableBooks, 4));
        int loaned = loans.getOnLoanCount(Users.getLoggedInStudentId());
        String bookTitle = twg.getRowValue(tableBooks, 2);
        String isbn = twg.getRowValue(tableBooks, 1).toString();


        HashMap check = loans.getByColumn("isbn", isbn);
        String checkForSameBook;

        if (check.get("returned") != null) {
            checkForSameBook = check.get("returned").toString();

        } else {
            checkForSameBook = "yes";

        }


        //If borrow limit is not exceeded and if book is in stock
        if ((loaned <= 10) && copiesAvailable >= 1 && (isbn.equals(isbn) & checkForSameBook.equals("yes"))) {
            //Display a popup box asking to confirm choice
            Optional<ButtonType> result = Screen.popup("CONFIRMATION", "You are about to borrow:" + "\n" + bookTitle + "\n" + "Is this correct?");
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                LocalDate due = date.toLocalDate().plusDays(8);


                // create a loan record for the logged in user and update book info
                loans.insert(new HashMap() {
                    {
                        put("student_id", Users.getLoggedInStudentId());
                        put("isbn", isbn);
                        put("date_borrowed", date);
                        put("title", bookTitle);
                        put("date_due", due);
                    }
                });

                bookupdate.put("copies_in_stock", "copies_in_stock -1");
                bookupdate.put("currently_on_loan", "currently_on_loan +1");
                books.update(bookupdate, bookId);
                twg.setTable("books", tableBooks, false, null);
                //end of loan creation
            } else {
                // ... user chose CANCEL or close
            }

            //Display messages that you have reached maximum borrow limit or if there is no books available
        } else if (loaned >= 10 & copiesAvailable >= 1) {
            Screen.popup("INFORMATION", "You are already borrowing the maximum amount of books allowed.");
            System.out.println("potato");
        } else if ((isbn.equals(isbn) && checkForSameBook.equals("no"))) {
            Screen.popup("INFORMATION", "You are already borrowing a copy of this book");

        } else {

            Screen.popup("INFORMATION", "There are no books available");

        }

    }


    public void returnBook(TableView userLoansTable) {
        Books books = new Books();
        Loans loans = new Loans();

        TableViewControls twg = new TableViewControls();
        int loanId = Integer.parseInt(twg.getRowValue(userLoansTable, 0));
        HashMap loanInfo = loans.getById(loanId);
        HashMap updateLoans = new HashMap<>();
        HashMap<String, String> bookupdate = new HashMap<>();


        String bookTitle = loanInfo.get("title").toString();
        String isbn = loanInfo.get("isbn").toString();

        int getBookId = Integer.parseInt(books.getByColumn("isbn", isbn).get("id").toString());
        Date date = new Date(Calendar.getInstance().getTime().getTime());

        Optional<ButtonType> result = Screen.popup("CONFIRMATION", "You are about to return:" + "\n" + bookTitle + "\n" + "Do you wish to proceed?");
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            // set the loan record to returned and update book stock
            System.out.println(date.toLocalDate().toString());

            updateLoans.put("returned", "'1'");
            updateLoans.put("date_returned", "'" + date.toLocalDate() + "'");
            loans.update(updateLoans, loanId);

            bookupdate.put("copies_in_stock", "copies_in_stock +1");
            bookupdate.put("currently_on_loan", "currently_on_loan -1");
            books.update(bookupdate, getBookId);

            //check if book was overdue, if so add debt to the user.
            HashMap dates = loans.getByColumn("id", Integer.toString(loanId));
            LocalDate borrowDate = LocalDate.parse(dates.get("date_borrowed").toString());
            LocalDate returnedDate = LocalDate.parse(dates.get("date_returned").toString());
            long daysPassed = DAYS.between(borrowDate, returnedDate);
            twg.setTable("loans", userLoansTable, true, Users.getLoggedInStudentId());
            if (daysPassed > 8) {

                HashMap<String, String> userAddDebt = new HashMap<>();
                userAddDebt.put("debt", "8.00");
                Users users = new Users();
                users.update(userAddDebt, users.getLoggedInUserTableID());
                Screen.popup("WARNING", "A fine has been applied to your account for Overdue Books \n" +
                        "You can pay and get it removed at the Library Desk");

            } else {
                Screen.popup("INFORMATION", "Thanks! Please place the book on the trolley");
                twg.setTable("loans", userLoansTable, true, Users.getLoggedInStudentId());
            }
//
        } else {
            // ... user chose CANCEL or close
        }
    }
}