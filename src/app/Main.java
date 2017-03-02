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

    @Override
    public void start(Stage primaryStage) throws Exception {

        Users users = new Users();
        Books books = new Books();
        HashMap bookData = new HashMap();
        HashMap<String, String> bookupdate = new HashMap<>();


//                  add a book
//        "4656566","Soup", "Kitchen",3,0,"elo"
//        books.insert(new HashMap() {{
//            put("title", "test");
//            put("isbn", "5555");
//            put("location", "Testo");
//            put("authors", "Tester");
//            put("copies_in_stock", 100);
//        }});






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
