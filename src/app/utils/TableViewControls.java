package app.utils;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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


}

