package app.utils;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class TableViewControls {


    ConnectionManager conn;
    private ObservableList<ObservableList> data;

    public void setTable(String tableName, TableView setTable) {
        data = FXCollections.observableArrayList();


        try {

            String SQL = ("SELECT * from" + " " + tableName);
            PreparedStatement prepstm;
            prepstm = conn.getInstance().getConnection().prepareStatement(SQL);

            ResultSet rs = prepstm.executeQuery();
            setTable.getColumns().clear();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {

                final int j = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j).toString()));


                setTable.getColumns().addAll(col);
                //System.out.println("Column [" + i + "] ");
            }

            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                // System.out.println("Row [1] added " + row);

                data.add(row);

            }

            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added " + row);
                data.add(row);


            }
            setTable.setItems(data);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Table");
        }


    }


    public void updateInfoUsers(TextField studentidfield, TextField passwordfield, TextField firstnamefield,
                                TextField lastnamefield, TextField phonenumberfield, TextField emailfield, TextField id) {

        try {

            String query = "UPDATE users SET student_id = ?, "
                    + "password = ?, "
                    + "first_name = ?, "
                    + "last_name = ?, "
                    + "phone_number = ?, "
                    + "email = ? "
                    + "WHERE student_id = ?";

            PreparedStatement pst;
            pst = conn.getInstance().getConnection().prepareStatement(query);

            pst.setString(1, studentidfield.getText());
            pst.setString(2, passwordfield.getText());
            pst.setString(3, firstnamefield.getText());
            pst.setString(4, lastnamefield.getText());
            pst.setString(5, phonenumberfield.getText());
            pst.setString(6, emailfield.getText());
            pst.setString(7, id.getText());
            pst.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getRowInfoUsers(TableView tableUser, TextField idfield, TextField emailfield, TextField phonenumberfield,
                                TextField firstnamefield, TextField lastnamefield) {
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
}
