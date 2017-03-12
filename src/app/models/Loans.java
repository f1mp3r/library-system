package app.models;

import app.utils.QueryBuilder;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Thez on 3/1/2017.
 */
public class Loans extends Model {
    public Loans() {
        super();
        this.table = "loans";
    }

    public int getOnLoanCount(String userId) {
        int rowCount = 0;

        try {
            String countQuery = String.format("select count(*) from loans where user_id = '%s' AND returned = 0", userId);
            System.out.println(countQuery);
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(countQuery);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            rowCount = resultSet.getInt("count(*)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rowCount;
    }

    public boolean checkForSameBook(String userId, String bookId) {
        try {
            String sameBookQuery = String.format("select count(*) from loans where user_id = '%s' AND book_id = '%s' AND returned = 0", userId, bookId);
            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(sameBookQuery);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int rowCount = resultSet.getInt("count(*)");

            if (rowCount >= 1) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public HashMap getByUserAndBook(String userId, String bookId) {
        HashMap<String, String> result = new HashMap();
        try {
            QueryBuilder query = new QueryBuilder("loans");
            String selectQuery = query
                    .select("@loans.id as `id`")
                    .joinInner("users", "users.id = loans.user_id")
                    .joinInner("books", "books.id = loans.book_id")
                    .where("id", "=", bookId, "books")
                    .where("id", "=", userId, "users")
                    .where("returned", "=", "0", "loans")
                    .build();

            PreparedStatement statement = (PreparedStatement) this.connection.getConnection().prepareStatement(selectQuery);

            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnLabel(i);
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

        return result;
    }
}
