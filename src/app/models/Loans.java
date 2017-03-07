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


    public boolean checkForSameBook(String student_id, String isbn) {
       int rowcount;
       boolean result = true;
        try {
            String sameBookQuery = String.format("select count(*) from loans where student_id = '%s' AND isbn = '%s' AND returned='no'", student_id, isbn);
            System.out.println(sameBookQuery);
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(sameBookQuery);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            rowcount = resultSet.getInt("count(*)");

            if( rowcount >=1) {
                result = false;
            }

        } catch (SQLException e) {

        }
        return result;


    }
}
