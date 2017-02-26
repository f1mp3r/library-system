package app.models;

import app.utils.ConnectionManager;
import com.mysql.jdbc.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Chris on 21.2.2017 г..
 */
public class Model {
    protected ConnectionManager connection;
    protected String table;
    /**
     * Skip these fields on select results
     */
    protected ArrayList<String> hiddenFields;

    Model() {
        this.connection = ConnectionManager.getInstance();
        this.hiddenFields = new ArrayList<>();
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
//            System.out.println(statement.asSql());

            statement.execute();
            rowsChanged = statement.getUpdateCount();
            statement.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rowsChanged;
    }

}
