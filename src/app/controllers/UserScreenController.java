package app.controllers;

import app.utils.TableViewControls;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Thez on 2/24/2017.
 */
public class UserScreenController implements Initializable {

    @FXML
    private TableView tableBooks;
    @FXML
    private Tab tabBooks;
    TableViewControls twg = new TableViewControls();


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tabBooks.setOnSelectionChanged(t -> twg.setTable("books", tableBooks));

        tableBooks.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {


            }

        });





    }
}
