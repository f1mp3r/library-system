package app.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.print.DocFlavor;
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

    public static Optional<ButtonType> exception(Exception e) {
        return Screen.exception(e, "");
    }

    public static Optional<ButtonType> exception(Exception e, String customMessage) {
        return Screen.popup("ERROR", customMessage.isEmpty() ? e.getMessage() : customMessage);
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
            Screen.exception(e);
        }
    }

    // method dialog, return dialog
    public static Dialog dialogEditBook(String title, String headerText) {

        Dialog dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        return dialog;
    }

    public static  Optional<String> makeSingleInputDialog(MenuItem objectOfChange, String textfieldPrompt, String title, String headertext, String contentText) {
            TextInputDialog dialog = new TextInputDialog(textfieldPrompt);
            dialog.setTitle(title);
            dialog.setHeaderText(headertext);
            dialog.setContentText(contentText);
            Optional<String> newInput = dialog.showAndWait();
//            newInput.ifPresent(input -> input = forReturn);

        return newInput;
    }
}
