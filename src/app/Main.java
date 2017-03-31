package app;

import app.models.Books;
import app.models.Users;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Users users = new Users();
        Books books = new Books();
        HashMap bookData = new HashMap();
        HashMap<String, String> bookUpdate = new HashMap<>();

//        QueryBuilder query = new QueryBuilder("loans");
//        System.out.println( query.select("*").where("student_id", "=", Users.getLoggedInStudentId()).where("returned", "=", "no").build());
//        String searchKey = "%book%";
//        System.out.println(
//            query.select("title", "authors").where("title", "LIKE", searchKey).orWhere("authors", "LIKE", searchKey).build()
//        );
//        query.update(bookUpdate).where("id", "4").build();
//
//                  add a book
//
//        books.insert(new HashMap() {{
//            put("title", "fer4");
//            put("isbn", "999");
//            put("location", "Tesrsto");
//            put("authors", "Testesrr");
//            put("copies_in_stock", 0);
//        }});


//           add a loan record
//        Loans loans = new Loans();
//        loans.insert(new HashMap() {
//            {
//                put("user_id", 61);
//                put("date_borrowed", "2017-02-01");
//                put("date_due", "2016-03-28");
//                put("book_id", 1);
//            }
//        });


        // region make an admin
//       users.insert(new HashMap() {{
//
//            put("student_id", "qwerty");
//            put("password", "qwerty");
//            put("first_name", "Tololod");
//            put("last_name", "serdo");
//            put("email", "souazddap@kitchen.com");
//            put("phone_number", "2112141");
//            put("permission", true);
//        }});

        // endregion

//        try {
        Parent root = FXMLLoader.load(getClass().getResource("../views/ChooseScreen.fxml"));
        primaryStage.setTitle("The Librarian");
        primaryStage.setScene(new Scene(root, 380, 320));
        primaryStage.show();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

    }
}
