package app.utils;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.controlsfx.control.table.TableFilter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TableViewControls {
    ConnectionManager conn;
    private ObservableList<ObservableList> data;

    public void setTable(String query, TableView tableView) {
        data = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = conn.getInstance().getConnection().prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            tableView.getColumns().clear();

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;

                TableColumn<ObservableList<String>, String> col = new TableColumn(rs.getMetaData().getColumnLabel(i + 1));
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
                tableView.getColumns().addAll(col);
            }

            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            tableView.setItems(data);
            TableFilter.forTableView(tableView).apply();
            conn.getInstance().getConnection().close();
        } catch (Exception e) {
            Screen.exception(e, "Error while building table");
        }
    }

    public String getRowValue(TableView tableUser, int rowNumber) {
        Object row1 = new Object();

        if (tableUser.getSelectionModel().getSelectedItem() != null) {
            TableView.TableViewSelectionModel selectionModel = tableUser.getSelectionModel();
            ObservableList selectedCells = selectionModel.getSelectedCells();

            TablePosition tablePosition = (TablePosition) selectedCells.get(0);

            tablePosition.getTableView().getSelectionModel().getTableView().getId();

            Object getbothvalue = tableUser.getSelectionModel().getSelectedItem().toString();
            //gives you first column value..

            row1 = getbothvalue.toString().split(",")[rowNumber].substring(1);
        }

        return row1.toString();
    }
}

