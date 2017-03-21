package app;

import app.models.Books;
import app.models.Users;
import app.utils.QueryBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {

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

//        books.insert(new HashMap() {{
//            put("title", "fer4");
//            put("isbn", "999");
//            put("location", "Tesrsto");
//            put("authors", "Testesrr");
//            put("copies_in_stock", 1);
//        }});

//           add a loan record
//        Loans loans = new Loans();
//        loans.insert(new HashMap() {
//            {
//                put("student_id", "lopas");
//                put("isbn", "65464456");
//                put("date_borrowed", "2017-02-01");
//                put("title", "hahaha");
//                put("date_due", "2016-04-01");
//            }
//        });


        // region make an admin
//       users.insert(new HashMap() {{
//            put("student_id", "randomztest");
//            put("password", "randomtest");
//            put("first_name", "Tololo");
//            put("last_name", "sero");
//            put("email", "souazp@kitchen.com");
//            put("phone_number", "2112141");
//            put("debt", 200);
//            put("permission", false);
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

    public static void main(String[] args) {
        launch(args);
    }
}
