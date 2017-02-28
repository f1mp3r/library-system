package app;

import app.models.Books;
import app.models.Users;
import javafx.application.Application;
import javafx.collections.ObservableList;
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
        System.out.println(books.getById(1));


                  //add a book
//        "4656566","Soup", "Kitchen",3,0,"elo"
//        books.insert(new HashMap() {{
//            put("title", "Random book");
//            put("isbn", "46543841385");
//        }});
/*
        // region make an admin
//     users.addUser("random", "random", "Admin", "Admin","666666", "admin@llibrary.com", true);


        // endregion

        Parent root = FXMLLoader.load(getClass().getResource("../views/ChooseScreen.fxml"));
        primaryStage.setTitle("The Librarian");
        primaryStage.setScene(new Scene(root, 380, 320));
        primaryStage.show();
*/

    }

    public static void main(String[] args) {
        launch(args);
    }
}
