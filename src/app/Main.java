package app;

import app.models.Books;
import app.models.Loans;
import app.models.Users;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.HashMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Users users = new Users();
        Books books = new Books();
        HashMap bookData = new HashMap();
        HashMap<String, String> bookupdate = new HashMap<>();


//                  add a book
//        "4656566","Soup", "Kitchen",3,0,"elo"
//        books.insert(new HashMap() {{
//            put("title", "4443143532444");
//            put("isbn", "5431413523412");
//            put("location", "Tesrto");
//            put("authors", "Testerr");
//            put("copies_in_stock", 100);
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
//            put("student_id", "random");
//            put("password", "random");
//            put("first_name", "Tololo");
//            put("last_name", "sero");
//            put("email", "soup@kitchen.com");
//            put("phone_number", "2112141");
//            put("permission", true);
//        }});

        // endregion

        Parent root = FXMLLoader.load(getClass().getResource("../views/ChooseScreen.fxml"));
        primaryStage.setTitle("The Librarian");
        primaryStage.setScene(new Scene(root, 380, 320));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
