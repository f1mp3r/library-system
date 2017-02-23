package app.utils;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chris on 22.2.2017 г..
 */
public class Screen {
    protected Class context;

    public Screen(Class context) {
        this.context = context;
    }

    public void loadView(String viewFile, String title) {
        try {
            Parent root = null;
            root = FXMLLoader.load(this.context.getResource("../../views/" + viewFile + ".fxml"));
            Stage errorStage = new Stage();
            errorStage.setScene(new Scene(root));
            errorStage.setTitle(title);
            errorStage.centerOnScreen();
            errorStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void popup(String type, ArrayList<String> message) {
        System.out.println(message);
        Alert alert = new Alert(Alert.AlertType.valueOf(type));
        alert.setContentText(message.toString().substring(1).replaceFirst("]", "").replace(", ", ""));
        alert.showAndWait();
    }

    public static void popup(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.valueOf(type));
        alert.setContentText(message);
        alert.showAndWait();
    }
}
