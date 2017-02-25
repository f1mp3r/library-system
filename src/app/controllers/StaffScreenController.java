package app.controllers;

import app.utils.TableViewControls;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Thez on 2/23/2017.
 */


public class StaffScreenController implements Initializable {

    TableViewControls twg = new TableViewControls();
    @FXML
    private TableView tableUser;
    @FXML
    private Tab tabUserList;
    @FXML
    private TextField idfield;
    @FXML
    private TextField passwordfield;
    @FXML
    private TextField firstnamefield;
    @FXML
    private TextField lastnamefield;
    @FXML
    private TextField phonenumberfield;
    @FXML
    private TextField emailfield;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        tabUserList.setOnSelectionChanged(t -> twg.setTable("users", tableUser));

        tableUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                twg.getRowInfoUsers(tableUser, idfield,emailfield,firstnamefield,lastnamefield,phonenumberfield);

                }

            });



    }

    @FXML
    void generateTable(ActionEvent event) {
        twg.setTable("users", tableUser);


    }


}
