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
        HashMap<String, ObservableList> bookupdate = new HashMap<>();


                  //add a book
        // books.addBook("4656566","Soup", "Kitchen",3,0,"elo");

        // region make an admin
     //users.addUser("random", "random", "Admin", "Admin","666666", "admin@llibrary.com", true);

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
