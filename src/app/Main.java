package app;

import app.models.Books;
import app.models.Users;
import app.utils.Screen;
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
        Books books = new Books();
        if (books.getCount() == 0) {
            books.seedBooks();
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../views/ChooseScreen.fxml"));
            primaryStage.setTitle("The Librarian");
            primaryStage.setScene(new Scene(root, 380, 320));
            primaryStage.show();
        } catch (Exception e) {
            Screen.exception(e);
        }
    }
}
