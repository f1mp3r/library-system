package app.models;

import app.utils.ConnectionManager;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Chris on 21.2.2017 г..
 */
public class Model {
    protected ConnectionManager connection;
    protected String table;
    protected ArrayList<String> columns;
    /**
     * Skip these fields on select results
     */
    protected ArrayList<String> hiddenFields;

    Model() {
        this.connection = ConnectionManager.getInstance();
        this.hiddenFields = new ArrayList<>();
        this.columns = new ArrayList<>();
    }

    public int insert(HashMap data) {
        int rowsChanged = 0;
        try {
            String insertQuery = String.format(
                    "INSERT INTO `" + this.table + "` (%s) VALUES (%s)",
                    String.join(", ", data.keySet()),
                    String.join(", ", Collections.nCopies(data.size(), "?"))
            );
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(insertQuery);

            Set set = data.entrySet();
            Iterator i = set.iterator();

            int k = 1;
            while (i.hasNext()) {
                Map.Entry mapElement = (Map.Entry) i.next();
                statement.setObject(k++, mapElement.getValue());
            }

            statement.execute();
            rowsChanged = statement.getUpdateCount();
            statement.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rowsChanged;
    }

    public int update(HashMap data, int id) {
        int rowsChanged = 0;
        try {
            String updateQuery = String.format(
                    "UPDATE `" + this.table + "` SET %s WHERE id = %d", "%s",
                    id
            );
            System.out.println(updateQuery);
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(updateQuery);

            Set set = data.entrySet();
            Iterator i = set.iterator();
            String fields = "";

            while (i.hasNext()) {
                Map.Entry mapElement = (Map.Entry) i.next();
                fields += String.format("`%s` = %s",
                        mapElement.getKey(),
                        mapElement.getValue()
                );

                if (i.hasNext()) {
                    fields += ", ";
                }

            }
            updateQuery = String.format(updateQuery, fields);
            System.out.println(updateQuery);

            statement.executeUpdate(updateQuery);
            rowsChanged = statement.getUpdateCount();
            statement.getConnection().close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return rowsChanged;
    }

    /**
     * Returns table entry if such exists matching the column and value
     *
     * @param column
     * @param id
     * @return
     */
    public HashMap getByColumn(String column, String id) {
        HashMap<String, String> result = new HashMap();
        try {
            String selectQuery = String.format("SELECT * FROM `" + this.table + "` WHERE `%s` = '%s'", column, id);
            System.out.println(selectQuery);
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(selectQuery);

            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    if (this.hiddenFields.contains(columnName)) {
                        continue;
                    }

                    result.put(columnName, resultSet.getString(i));

                }
            }

            statement.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        return result;

    }


    /**
     * Returns table entry by given ID
     *
     * @param id
     * @return
     */
    public HashMap getById(int id) {
        return this.getByColumn("id", String.valueOf(id));
    }

    public ArrayList<HashMap> getAll() {
        String query = String.format("SELECT * FROM `%s`", this.table);
        System.out.println(query);
        return null;
    }

    public String escape(String variable) {
        return "'" + variable + "'";
    }

    public String toString() {
        return String.join(",", this.columns);
    }
}