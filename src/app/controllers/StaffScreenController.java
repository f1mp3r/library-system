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
                if (tableUser.getSelectionModel().getSelectedItem() != null) {
                    TableView.TableViewSelectionModel selectionModel = tableUser.getSelectionModel();


                    ObservableList selectedCells;
                    selectedCells = selectionModel.getSelectedCells();


                    TablePosition tablePosition = (TablePosition) selectedCells.get(0);


                    tablePosition.getTableView().getSelectionModel().getTableView().getId();

                    Object getbothvalue = tableUser.getSelectionModel().getSelectedItem().toString();
                    //gives you first column value..

                    Object row1 = getbothvalue.toString().split(",")[0].substring(1);
                    Object row2 = getbothvalue.toString().split(",")[1].substring(1);
                    Object row3 = getbothvalue.toString().split(",")[2].substring(1);
                    Object row4 = getbothvalue.toString().split(",")[3].substring(1);
                    Object row5 = getbothvalue.toString().split(",")[4].substring(1);
                    Object row6 = getbothvalue.toString().split(",")[5].substring(1);
                    Object row7 = getbothvalue.toString().split(",")[6].substring(1);
                    Object row8 = getbothvalue.toString().split(",")[7].substring(1);
                    Object row9 = getbothvalue.toString().split(",")[8].substring(1);
                    idfield.setText(row6.toString());
                    emailfield.setText(row2.toString());
                    phonenumberfield.setText(row9.toString());
                    firstnamefield.setText(row4.toString());
                    lastnamefield.setText(row5.toString());

                }

            }
        });


    }

    @FXML
    void generateTable(ActionEvent event) {
        twg.setTable("users", tableUser);


    }


}
