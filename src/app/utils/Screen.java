package app.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


/**
 * Created by Chris on 22.2.2017 г..
 */
public class Screen {
    protected Class context;

    public Screen(Class context) {
        this.context = context;
    }

    public static void popup(String type, ArrayList<String> message) {
        System.out.println(message);
        Alert alert = new Alert(Alert.AlertType.valueOf(type));
        alert.setContentText(String.join("\r\n", message));
        alert.showAndWait();
    }

    public static Optional<ButtonType> popup(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.valueOf(type));
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();

       return result;
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

    // method dialog, return dialog
    public static Dialog dialogEditBook(String title, String headerText) {

        Dialog dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        return dialog;
    }

}
