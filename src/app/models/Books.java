package app.models;

import app.utils.QueryBuilder;
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
        QueryBuilder queryBooks = new QueryBuilder("books");

        //Gets required values(mostly from tableview)

        int bookId = Integer.parseInt(twg.getRowValue(tableBooks, 0));
        HashMap bookInfo = books.getById(bookId);


        int copiesAvailable = Integer.parseInt(bookInfo.get("copies_in_stock").toString());
        int loaned = loans.getOnLoanCount(Users.getLoggedInStudentId());
        String bookTitle = bookInfo.get("title").toString();
        String isbn = bookInfo.get("isbn").toString();


        String email = Users.getLoggedInStudentEmail();

        boolean checkForSameBook =loans.checkForSameBook(Users.getLoggedInStudentId(),isbn);

        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        LocalDate due = date.toLocalDate().plusDays(8);




        //If borrow limit is not exceeded and if book is in stock
        if ((loaned <= 10) && copiesAvailable >= 1  && (checkForSameBook==true)) {
            //Display a popup box asking to confirm choice
            Optional<ButtonType> result = Screen.popup("CONFIRMATION", "You are about to borrow:" + "\n" + bookTitle + "\n" + "Is this correct?");
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
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
                twg.setTable(queryBooks.select().build(), tableBooks);
                //end of loan creation
            } else {
                // ... user chose CANCEL or close
            }

            //Display messages that you have reached maximum borrow limit or if there is no books available
        } else if (loaned >= 10 & copiesAvailable >= 1) {
            Screen.popup("INFORMATION", "You are already borrowing the maximum amount of books allowed.");
            System.out.println("potato");
        } else if ((isbn.equals(isbn) && checkForSameBook==false)) {
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

        int loanId = Integer.parseInt(twg.getRowValue(userLoansTable, 0));
        HashMap loanInfo = loans.getById(loanId);

        String bookTitle = loanInfo.get("title").toString();
        String isbn = loanInfo.get("isbn").toString();
        int getBookId = Integer.parseInt(books.getByColumn("isbn", isbn).get("id").toString());

        HashMap updateLoans = new HashMap<>();
        HashMap<String, String> bookupdate = new HashMap<>();
        HashMap<String, String> userAddDebt = new HashMap<>();




        Date date = new Date(Calendar.getInstance().getTime().getTime());

        Optional<ButtonType> result = Screen.popup("CONFIRMATION", "You are about to return:" + "\n" + bookTitle + "\n" + "Do you wish to proceed?");

        if (result.get() == ButtonType.OK) {

            // ... user chose OK
            // set the loan record to returned and update book stock


            updateLoans.put("returned", "'1'");
            updateLoans.put("date_returned", "'" + date.toLocalDate() + "'");
            loans.update(updateLoans, loanId);

            bookupdate.put("copies_in_stock", "copies_in_stock +1");
            bookupdate.put("currently_on_loan", "currently_on_loan -1");
            books.update(bookupdate, getBookId);

            //check if book was overdue, if so add debt to the user.
            //refresh table

            twg.setTable(queryLoans.select().build(), userLoansTable);
            HashMap dates = loans.getByColumn("id", Integer.toString(loanId));
            LocalDate borrowDate = LocalDate.parse(dates.get("date_borrowed").toString());
            LocalDate returnedDate = LocalDate.parse(dates.get("date_returned").toString());
            long daysPassed = DAYS.between(borrowDate, returnedDate);
            if (daysPassed > 8) {

                userAddDebt.put("debt", "8.00");
                users.update(userAddDebt, users.getLoggedInUserTableID());

                Screen.popup("WARNING", "A fine has been applied to your account for Overdue Books \n" +
                        "You can pay and get it removed at the Library Desk");

                //refresh table
                twg.setTable(queryLoans.select().where("student_id", users.getLoggedInStudentId()).where("returned", "no").build(), userLoansTable);

            } else {
                twg.setTable(queryLoans.select().where("student_id", users.getLoggedInStudentId()).where("returned", "no").build(), userLoansTable);
                Screen.popup("INFORMATION", "Thanks! Please place the book on the trolley");
            }

        } else {
            // ... user chose CANCEL or close
        }
    }
}