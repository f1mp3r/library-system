package app.models;

import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by Thez on 3/1/2017.
 */
public class Loans extends Model {
    public Loans() {
        super();
        this.table = "loans";
    }

    public int getOnLoanCount(String columnValue) {
        int rowcount = 0;
        try {
            String countQuery = String.format("select count(*) from loans where student_id = '%s' AND returned='no'", columnValue);
            System.out.println(countQuery);
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(countQuery);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            rowcount = resultSet.getInt("count(*)");


        } catch (SQLException e) {

        }
        return rowcount;
    }


}
