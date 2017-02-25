package app;

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
        // region make an admin
        Users users = new Users();

     //  users.addUser("random", "random", "Admin", "Admin","666666", "admin@library.com", false);

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
