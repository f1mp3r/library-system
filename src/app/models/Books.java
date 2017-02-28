package app.models;

import java.util.HashMap;

/**
 * Created by Thez on 2/25/2017.
 */
public class Books extends Model {


        public Books() {
            super();
            this.table = "books";

//            this.columns.add("id");
//            this.columns.add("isbn");
//            this.columns.add("title");
//            this.columns.add("location");
//            this.columns.add("copies_in_stock");
//            this.columns.add("currently_on_load");
//            this.columns.add("authors");
        }


    public void addBook(String isbn, String title, String location, int copies_in_stock,
                        int currently_on_loan, String author) {
        try {


            Books books = new Books();
            HashMap bookData = new HashMap();


            bookData.put("isbn", isbn);
            bookData.put("title", title);
            bookData.put("location",location);
            bookData.put("copies_in_stock", copies_in_stock);
            bookData.put("currently_on_loan", currently_on_loan);
            bookData.put("authors", author);
            books.insert(bookData);




        } catch(Exception exc) {

        }
    }
}
