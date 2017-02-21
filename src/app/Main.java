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
        HashMap data = new HashMap();
        data.put("email", "admin@library.com");
        data.put("first_name", "None");
        data.put("last_name", "None");
        data.put("student_id", "");
        data.put("password", Users.hash("random"));
        data.put("permission", true);
//        users.insert(data);
        // endregion

        Parent root = FXMLLoader.load(getClass().getResource("../views/FirstScreen.fxml"));
        primaryStage.setTitle("The Librarian");
        primaryStage.setScene(new Scene(root, 650, 320));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
