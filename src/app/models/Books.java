package app.models;

import app.utils.QueryBuilder;
import app.utils.Screen;
import app.utils.TableViewControls;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

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
            "@copies_in_stock `Copies available`",
    };

    public Books() {
        super();
        this.table = "books";
    }

    public void borrowBook(TableView tableBooks) {

        Books books = new Books();
        Users users = new Users();
        HashMap<String, String> bookUpdate = new HashMap<>();
        Reservations rez = new Reservations();

        Loans loans = new Loans();
        TableViewControls twg = new TableViewControls();
        QueryBuilder queryBooks = new QueryBuilder("books");

        //Gets required values(mostly from tableview)

        int bookId = Integer.parseInt(twg.getRowValue(tableBooks, 0));
        HashMap bookInfo = books.getById(bookId);
        HashMap userInfo = users.getById(Users.getLoggedInUserTableID());


        int copiesAvailable = Integer.parseInt(bookInfo.get("copies_in_stock").toString());
        int loaned = loans.getOnLoanCount(String.valueOf(Users.getLoggedInUserTableID()));
        String bookTitle = bookInfo.get("title").toString();
        int reserved = Integer.parseInt(bookInfo.get("currently_reserved").toString());


        boolean checkForSameBook = loans.checkForSameBook(String.valueOf(Users.getLoggedInUserTableID()), String.valueOf(bookId));
        HashMap rezInfo = rez.getByUserAndBookAndTitle(Integer.toString(Users.getLoggedInUserTableID()), Integer.toString(bookId), "'" + bookTitle + "'");

        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        LocalDate due = date.toLocalDate().plusDays(8);
        int checkReservationId = 0;
        int checkReservationBookId = 0;
        if (rezInfo.containsKey("user_id")) {
            checkReservationId = Integer.parseInt(rezInfo.get("user_id").toString());
            checkReservationBookId = Integer.parseInt(rezInfo.get("book_id").toString());
        } else {

        }
        //If borrow limit is not exceeded and if book is in stock
        if (checkReservationBookId != bookId && checkReservationId != Users.getLoggedInUserTableID() && (loaned <= 10) && copiesAvailable >= 1 && checkForSameBook && reserved < copiesAvailable) {
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
        } else if (loaned >= 10) {
            //Display messages that you have reached maximum borrow limit or if there is no books available
            Screen.popup("INFORMATION", "You are already borrowing the maximum amount of books allowed.");

        } else if (!checkForSameBook) {
            Screen.popup("INFORMATION", "You are already borrowing a copy of this book");
        } else {


            int earliestUserIdToReserve = rez.getEarliestDate(bookTitle);


            if (checkReservationBookId == bookId && checkReservationId == Users.getLoggedInUserTableID() && copiesAvailable > 0 && earliestUserIdToReserve == Users.getLoggedInUserTableID()) {
                Optional<ButtonType> result = Screen.popup("CONFIRMATION", "You are about to borrow your Reserved book:" + "\n" + bookTitle + "\n" + "Is this correct?");
                if (result.get() == ButtonType.OK) {
                    // ... user chose OK
                    // create a loan record for the logged in user and update book info
                    this.updateBookInfoReservation(loans, bookId, date, due, bookUpdate, rezInfo, rez, twg, queryBooks, tableBooks);

                    earliestUserIdToReserve = rez.getEarliestDate(bookTitle);


                }

            } else if (checkReservationBookId == bookId && checkReservationId == Users.getLoggedInUserTableID() && copiesAvailable >= reserved && earliestUserIdToReserve != Users.getLoggedInUserTableID()) {
                Optional<ButtonType> result = Screen.popup("CONFIRMATION", "You are about to borrow your Reserved book:" + "\n" + bookTitle + "\n" + "Is this correct?");
                if (result.get() == ButtonType.OK) {
                    // ... user chose OK
                    // create a loan record for the logged in user and update book info

                    this.updateBookInfoReservation(loans, bookId, date, due, bookUpdate, rezInfo, rez, twg, queryBooks, tableBooks);
                    earliestUserIdToReserve = rez.getEarliestDate(bookTitle);


                }
            } else {
                if (!rezInfo.containsKey("user_id")) {
                    Optional<ButtonType> result = Screen.popup("CONFIRMATION", "The book" + "\n" + bookTitle + "\n" + "Is not available, But you can reserve it, by pressing okay");
                    if (result.get() == ButtonType.OK) {
                        // ... user chose OK
                        rez.reserve(bookTitle, date, Users.getLoggedInUserTableID(), bookId, bookUpdate, books);
                    }
                } else {
                    Screen.popup("INFORMATION", "Book is not available yet, but you have already rezerved it");
                }
            }


        }
    }

    public void updateBookInfoReservation(Loans loans, int bookId, Date date, LocalDate due, HashMap bookUpdate, HashMap rezInfo, Reservations rez, TableViewControls twg, QueryBuilder queryBooks, TableView tableBooks) {

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
        bookUpdate.put("currently_reserved", "currently_reserved -1");
        this.update(bookUpdate, bookId);

        HashMap deleteInfo = new HashMap();
        deleteInfo.put("id", rezInfo.get("id"));
        rez.delete(deleteInfo);
        twg.setTable(queryBooks.select(Books.memberVisibleFields).build(), tableBooks);
        //end of loan creation
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
            HashMap dates = loans.getByColumn("book_id", Integer.toString(bookId));
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
            Screen.exception(e);
        }
    }

    /**
     * Seed library with books
     */
    public void seedBooks() {
        ArrayList<HashMap> books = new ArrayList<>();

        books.add(new HashMap() {{
            put("isbn", "1408708981");
            put("title", "Fantastic Beasts and Where to Find Them");
            put("location", "C17");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "J.K. Rowling");
        }});
        books.add(new HashMap() {{
            put("isbn", "1408855682");
            put("title", "Harry Potter and the Goblet of Fire: 4/7 (Harry Potter 4)");
            put("location", "D18");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "J.K. Rowling");
        }});
        books.add(new HashMap() {{
            put("isbn", "1408855690");
            put("title", "Harry Potter and the Order of the Phoenix: 5/7 (Harry Potter 5)");
            put("location", "A10");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "J.K. Rowling");
        }});
        books.add(new HashMap() {{
            put("isbn", "1408855704");
            put("title", "Harry Potter and the Half-Blood Prince: 6/7 (Harry Potter 6)");
            put("location", "A14");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "J.K. Rowling");
        }});
        books.add(new HashMap() {{
            put("isbn", "1408855712");
            put("title", "Harry Potter and the Deathly Hallows: 7/7 (Harry Potter 7)");
            put("location", "A16");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "J.K. Rowling");
        }});
        books.add(new HashMap() {{
            put("isbn", "0007548230");
            put("title", "A Game of Thrones (A Song of Ice and Fire, Book 1)");
            put("location", "A10");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "George R.R. Martin");
        }});
        books.add(new HashMap() {{
            put("isbn", "0241246105");
            put("title", "The Man in the High Castle");
            put("location", "C10");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "Philip K. Dick");
        }});
        books.add(new HashMap() {{
            put("isbn", "1544118953");
            put("title", "The Secret of Spellshadow Manor");
            put("location", "A13");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "Bella Forrest");
        }});
        books.add(new HashMap() {{
            put("isbn", "1408886812");
            put("title", "Norse Mythology");
            put("location", "D04");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "Neil Gaiman");
        }});
        books.add(new HashMap() {{
            put("isbn", "1410464199");
            put("title", "The Humans");
            put("location", "S23");
            put("copies_in_stock", "15");
            put("currently_on_loan", "0");
            put("currently_reserved", "0");
            put("authors", "Matt Haig");
        }});

        books.forEach(this::insert);
    }
}